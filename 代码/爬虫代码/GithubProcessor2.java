package org.example;


import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GithubProcessor2 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100).addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
            .addHeader("Connection", "keep-alive")
            .addHeader("X-Requested-With", "XMLHttpRequest")
            .addHeader("Content-Type",
                    "application/x-www-form-urlencoded;charset=utf-8")
            .addHeader(
                    "User-Agent",
                    "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");

    private HashMap<String, String> map = new HashMap<>();
    private List<HashMap> list = new ArrayList<>();
    // 用来存储cookie信息
    private Set<Cookie> cookies;
    private List<Topic> topics = new ArrayList<Topic>();

    @Override
    public Site getSite() {

        // 将获取到的cookie信息添加到webmagic中
        for (Cookie cookie : cookies) {
            site.addCookie(cookie.getName().toString(), cookie.getValue()
                    .toString());
        }
        return site;
    }


    @Override
    public void process(Page page) {
        int[] years = {2009,2010,2011,2012,2013,2014,2015,2016,2017,2018,2019,2020,2021,2022};
        for(int year : years){
            String url = "https://github.com/search?q=language%3AJava++created%3A"+ year +"&type=Repositories";
            for (int i = 0; i < 100; i++) {
                //i代表当前页
                page.addTargetRequest(url+="&p=" + i);
                //获取详情页列表
                Selectable xpath = page.getHtml().xpath("//html/body/div[5]/main/div/div[3]/div/ul/li/a");
                List<Selectable> nodes = xpath.nodes();
                Topic topic = new Topic();
                for (int j = 0; j < nodes.size(); j++) {
//                    page.addTargetRequests(xpath.links().all());
                    Selectable selectable = nodes.get(i);
                    //从详情页面获取数据----topic1
                    Selectable t1 =selectable.xpath("//*[@id=\"repo-content-pjax-container\"]/div/div/div[3]/div[2]/div/div[1]/div/div[1]/div");//outerHtml()
                    //从详情页面获取数据----topic2
                    Selectable t2 = selectable.xpath("//*[@id=\"repo-content-pjax-container\"]/div/div/div[3]/div[2]/div/div[1]/div/div[2]/div");
                    topic.setTopic1(t1.get());
                    topic.setTopic2(t2.get());
                }
                topics.add(topic);
            }
        }
        System.out.println(list);

        //page.addTargetRequest(new Request("https://github.com/search?q=location%3AEngland&type=Users&ref=advsearch&l=&l="));
        //page.addTargetRequest(new Request("https://github.com/search?q=location%3AUSA&type=Users&ref=advsearch&l=&l="));
        //page.addTargetRequest(new Request("https://github.com/search?q=location%3AUSA&type=Users&ref=advsearch&l=&l="));
        //page.addTargetRequest(new Request("https://github.com/search?q=location%3AUSA&type=Users&ref=advsearch&l=&l="));
        //page.addTargetRequest(new Request("https://github.com/search?q=location%3AUSA&type=Users&ref=advsearch&l=&l="));
        //page.addTargetRequest(new Request("https://github.com/search?q=location%3AUSA&type=Users&ref=advsearch&l=&l="));
    }




    public static void main(String[] args) {
        Request request = new Request("https://github.com/search?q=location%3AChina&type=Users&ref=advsearch&l=&l=");
        request.setMethod(HttpConstant.Method.GET);
        GithubProcessor2 processor = new GithubProcessor2();
        Spider.create(processor)
                .addRequest(request)
                .thread(5).run();//开启5个线程
    }
}
