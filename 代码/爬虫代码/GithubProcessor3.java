package org.example;


import org.apache.commons.collections.map.HashedMap;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.model.HttpRequestBody;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;
import us.codecraft.webmagic.utils.HttpConstant;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

public class GithubProcessor3 implements PageProcessor {

    private Site site = Site.me().setRetryTimes(3).setSleepTime(100);
    private HashMap<String, String> map = new HashMap<>();
    private List<HashMap> list = new ArrayList<>();

    @Override

    public Site getSite() {
        return site;
    }

    @Override
    public void process(Page page) {
        page.addTargetRequest("https://github.com/search?q=location%3AUSA&type=Users&ref=advsearch&l=&l=");
        page.addTargetRequest("https://github.com/search?q=location%3ASwitzerland&type=Users&ref=advsearch&l=&l=");
        Selectable xpath = page.getHtml().xpath("/html/body/div[5]/main/div/div[2]/div[1]/ul/li/a");
        List<Selectable> nodes = xpath.nodes();
        for (int i = 0; i < nodes.size(); i++) {
            Selectable selectable = nodes.get(i);
            map.put(selectable.xpath("//a//text()").toString(), selectable.xpath("//span//text()").toString());
            System.out.println(selectable.xpath("//a//text()").toString() + "====>" + selectable.xpath("//span//text()").toString());
        }
        list.add( map);
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
        //request.setRequestBody(HttpRequestBody.json("{'id':1}","utf-8"));
        request.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.150 Safari/537.36");

        Spider.create(new GithubRepoPageProcessor())
                .addRequest(request)
                //.setDownloader(new HttpClientDownloader())
                .thread(5).run();

    }
}