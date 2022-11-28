package spark;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.fastjson.JSON;

public class HandleData {
    public static void main(String[] args) throws FileNotFoundException {
        for (int i = 2009; i <= 2022; i++) {
            List<Result> list = new ArrayList<>();
            File file = new File("E:\\tags\\" + i + ".json");
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String xx = line.substring(1, line.length() - 1);
                    String[] split = xx.split(",");
                    Result result = new Result(split[0], Integer.parseInt(split[1]));
                    list.add(result);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = JSON.toJSONString(list);
            System.out.println("========" + i + "==========");

            String filePath = "E:\\tagsRes\\" + i + ".json";
            File file2 = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file2));
            ps.println(s);// 往文件里写入字符串

            System.out.println(s);
        }
    }
}
