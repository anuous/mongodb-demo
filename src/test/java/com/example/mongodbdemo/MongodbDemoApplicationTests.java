package com.example.mongodbdemo;

import com.example.mongodbdemo.mongodb.gridfs.MongoGridFsServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongodbDemoApplicationTests {
    @Autowired
    private MongoGridFsServer mongoGridFsServer;
    @Test
    public void contextLoads() {
    }


    @Test
    public void test() throws FileNotFoundException {
        File file = new File("E:\\picture\\业务理解记录-测试数据.txt");
        String id = mongoGridFsServer.saveFile(file);
        System.out.println(id);
    }

    @Test
    public void getFile()throws Exception{
        InputStream fis= mongoGridFsServer.getMongoFileIptStm("mg_3532a2cbdbfd47be8b4c6c72f4381c55");
        BufferedReader in = new BufferedReader(new InputStreamReader(fis));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        while ((line = in.readLine()) != null){
            buffer.append(line);
        }
        System.out.println( buffer.toString());

    }
}
