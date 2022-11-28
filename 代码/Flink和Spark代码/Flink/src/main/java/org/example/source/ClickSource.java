package org.example.source;

import cn.hutool.json.JSONUtil;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.example.pojo.input.Event;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;



public class ClickSource implements SourceFunction<Event> {
    // 声明一个布尔变量，作为控制数据生成的标识位
    private Boolean running = true;
    @Override
    public void run(SourceContext<Event> ctx) throws Exception {
        for (int i = 2009; i <= 2022; i++) {
            File file = new File("E:\\Data\\GitHubJavaProject_" + i + ".json");
            try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String line;
                while ((line = br.readLine()) != null) {
                    Event event = JSONUtil.toBean(line, Event.class);
                    event.setPullRequests((float) (event.getPullRequests() * (3.0/6) + event.getStars() * (1.0/6) + event.getForks() * (2.0/6)));
                    ctx.collect(event);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void cancel() {
        running = false;
    }

}
