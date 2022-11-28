package org.example;


import cn.hutool.json.JSONUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.example.pojo.input.Event;
import org.example.pojo.input.Tags;
import org.example.pojo.input.TagsResult;
import org.example.source.ClickSource;
import org.example.source.ClickSourceTags;

import java.util.*;

public class TagsReduce {

    private static final Integer N = 1000;

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        SingleOutputStreamOperator<Tuple2<String,String>> stream =  env.addSource(new ClickSourceTags())
            .map(new MapFunction<Tags, Tuple2<String, String>>() {
                @Override
                public Tuple2<String, String> map(Tags e) throws Exception {
                    return Tuple2.of(e.tags1, e.tags2);
                }
            });
        HashMap<Integer, List<Event>> ans = new HashMap<>();

        stream.keyBy(data -> true)
                //.print();
            .countWindow(1)
            .aggregate(new AvgPv());
        env.execute();
    }

    public static class AvgPv implements AggregateFunction<Tuple2<String, String>, HashMap<String, Integer>,HashMap<String,Integer> > {
        private HashMap<String, Integer> ans = new HashMap<>();
        private List<TagsResult> list = new ArrayList<>();
        @Override
        public HashMap<String, Integer> createAccumulator() {

            return new HashMap<>();
        }

        @Override
        public HashMap<String, Integer> add(Tuple2<String, String> tuple2, HashMap<String, Integer> map) {
            String tag1=  tuple2.f0.trim();
            String tag2=  tuple2.f1.trim();
            String[] arrTags1 = tag1.split(" ");
            String[] arrTags2 = tag2.split(" ");

            for (String s : arrTags1) {
                if (!s.isEmpty())
                    map.put(s,map.getOrDefault(s, 0) + 1);
            }
            for (String s : arrTags2) {
                if (!s.isEmpty())
                    map.put(s,map.getOrDefault(s, 0) + 1);
            }

            return map;
        }

        @Override
        public HashMap<String, Integer> getResult(HashMap<String, Integer> map) {
            // 合并结果
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String key = entry.getKey();
                if (!key.trim().equals("java")) {
                    if (ans.containsKey(key)) {
                        ans.put(entry.getKey(), ans.get(entry.getKey()) + entry.getValue());
                    } else {
                        ans.put(entry.getKey(), entry.getValue());
                    }
                }
            }

            list = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : ans.entrySet()) {
                list.add(new TagsResult(entry.getKey(), entry.getValue()));
            }
            list.sort(new Comparator<TagsResult>() {
                @Override
                public int compare(TagsResult o1, TagsResult o2) {
                    return o2.getValue() - o1.getValue();
                }
            });
            int to = list.size() > N ? N : list.size();
            list = list.subList(0,to);
            System.out.println("=============================");
            System.out.println(JSONUtil.toJsonStr(list));
            return ans;
        }

        @Override
        public HashMap<String, Integer> merge(HashMap<String, Integer> stringIntegerHashMap, HashMap<String, Integer> acc1) {
            return null;
        }
    }
}

