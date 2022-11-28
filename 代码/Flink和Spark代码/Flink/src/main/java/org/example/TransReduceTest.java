package org.example;


import cn.hutool.json.JSONUtil;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.pojo.input.Event;
import org.example.source.ClickSource;

import java.util.*;

public class TransReduceTest {

    private static final Integer N = 10;

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Tuple5<String,Float,Integer,Integer,Integer>> stream =  env.addSource(new ClickSource())
            .map(new MapFunction<Event, Tuple5<String, Float,Integer,Integer,Integer>>() {
                @Override
                public Tuple5<String, Float,Integer,Integer,Integer> map(Event e) throws Exception {
                    return Tuple5.of(e.proName, e.pullRequests,e.stars,e.forks,e.year);
                }
            });
        HashMap<Integer, List<Event>> ans = new HashMap<>();

        stream.keyBy(data -> true)
            .countWindow(1)
            .aggregate(new AvgPv());
        env.execute();
    }

    public static class AvgPv implements AggregateFunction<Tuple5<String, Float,Integer,Integer,Integer>, HashMap<Integer, List<Event>>,HashMap<Integer, List<Event>> > {

        private  HashMap<Integer, List<Event>> ans = new HashMap<>();

        @Override
        public HashMap<Integer, List<Event>> createAccumulator() {
            return new HashMap<>();
        }

        @Override
        public HashMap<Integer, List<Event>> add(Tuple5<String, Float, Integer, Integer, Integer> tuple5, HashMap<Integer, List<Event>> map) {
            List<Event> events = map.get(tuple5.f4);// 按照年份分区
            if (events == null) {
                events = new ArrayList<>();
            }
            // 添加到累积器中
            events.add(new Event(tuple5.f0,tuple5.f1,tuple5.f2,tuple5.f3,tuple5.f4));
            events.sort(new Comparator<Event>() {
                @Override
                public int compare(Event o1, Event o2) {
                    return  (o2.getPullRequests() > o1.getPullRequests())?1:-1;
                }
            });
            // 只获取前TopN
            int realSize = events.size() > N ? N : events.size(); // 获取前N个
            List<Event> newList = new ArrayList<>();
            for (int i = 0; i < realSize; i++) {
                newList.add(events.get(i));
            }
            map.put(tuple5.f4, newList);
            return map;
        }

        @Override
        public HashMap<Integer, List<Event>> getResult(HashMap<Integer, List<Event>> map) {
            // 从累加器中只获取TopN
            // 将累加器合并计算
            for(Map.Entry<Integer, List<Event>> entry : map.entrySet()) {
                if (!ans.containsKey(entry.getKey())) { // 如果不存在
                    //ans.put(entry.getKey(), entry.getValue());
                    List<Event> events = entry.getValue();
                    events.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event o1, Event o2) {
                            // pullRequests:stars:forks=3:1:2
                            return  (o2.getPullRequests() > o1.getPullRequests())?1:-1;
                        }
                    });
                    int realSize = events.size() > N ? N : events.size(); // 获取前N个
                    List<Event> newList = new ArrayList<>();
                    for (int i = 0; i < realSize; i++) {
                        newList.add(events.get(i));
                    }
                    ans.put(entry.getKey(), newList);
                } else {
                    entry.getValue().addAll(ans.get(entry.getKey()));
                    // 去topN
                    List<Event> events = entry.getValue();
                    events.sort(new Comparator<Event>() {
                        @Override
                        public int compare(Event o1, Event o2) {
                            return  (o2.getPullRequests() > o1.getPullRequests())?1:-1;

                        }
                    });
                    int realSize = events.size() > N ? N : events.size(); // 获取前N个
                    List<Event> newList = new ArrayList<>();
                    for (int i = 0; i < realSize; i++) {
                        newList.add(events.get(i));
                    }
                    ans.put(entry.getKey(), newList);
                }
            }
            System.out.println("===========================================================");
            System.out.println(JSONUtil.toJsonStr(ans));
            return ans;
        }

        @Override
        public HashMap<Integer, List<Event>> merge(HashMap<Integer, List<Event>> map1, HashMap<Integer, List<Event>> map2) {
            return null;
        }
    }
}

