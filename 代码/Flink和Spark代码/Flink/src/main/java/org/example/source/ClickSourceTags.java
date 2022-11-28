package org.example.source;

import cn.hutool.json.JSONUtil;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.example.pojo.input.Event;
import org.example.pojo.input.Tags;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;


public class ClickSourceTags implements SourceFunction<Tags> {
    // 声明一个布尔变量，作为控制数据生成的标识位
    private Boolean running = true;
    @Override
    public void run(SourceContext<Tags> ctx) throws Exception {
        for (int i = 2011; i <= 2011; i++) {
            File file = new File("E:\\TagsData\\GitHubJavaProject_" + i + "tags循环网址.json");
            try (BufferedReader br = new BufferedReader(new FileReader(file)))
            {
                String line;
                while ((line = br.readLine()) != null) {
                    Tags tags = JSONUtil.toBean(line, Tags.class);
                    //System.out.println(tags.toString());
                    ctx.collect(tags);
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
