
package com.home.springkafka.component;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.home.springkafka.mapper.MessageMapper;
import com.home.springkafka.service.DataService;
import com.home.springkafka.config.SpringBootKafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@Component
@Slf4j
public class SecondConsumer {
    @Autowired
    private DataService dataService;

    private final MessageMapper messageMapper;

    @Autowired
    public SecondConsumer(MessageMapper messageMapper) {
        this.messageMapper = messageMapper;
    }

    @KafkaListener(topics = SpringBootKafkaConfig.TOPIC_TEST_SECOND, groupId = SpringBootKafkaConfig.GROUP_ID)
    public void listen(List<String> messages, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        System.out.println("Second consumer received message" + messages);

        // 解析 JSON 数组
        JSONArray jsonArray = JSONUtil.parseArray(messages);

        // 打开或创建历史文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("history.txt", true))) {
            // 遍历数组并处理每条消息
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                // 提取需要的字段
                String uuid = jsonObject.getStr("uuid");
                String name = jsonObject.getStr("name");
                String category = jsonObject.getStr("category");
                int quality = jsonObject.getInt("quality");
                boolean isQualified = jsonObject.getBool("isQualified");
                String receivedAt = jsonObject.getStr("receivedAt");

                // 写入接收到的货物信息到历史文件
                writer.write("RFID: " + uuid + "," + " NAME: " + name + "," + " CATEGORY: " + category + "," + " IS_QUALIFIED: " + isQualified + "," + " RECEIVE_TIME: " + receivedAt + "\n");

                // 调用服务存储数据到数据库
                dataService.addProductToDatabase(uuid, name, category, quality, isQualified, receivedAt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


//package com.atguigu.springkafka.component;
//
//        import cn.hutool.json.JSONObject;
//        import cn.hutool.json.JSONUtil;
//        import com.atguigu.springkafka.service.DataService;
//        import lombok.extern.slf4j.Slf4j;
//        import com.atguigu.springkafka.config.SpringBootKafkaConfig;
//        import org.springframework.beans.factory.annotation.Autowired;
//        import org.springframework.kafka.annotation.KafkaListener;
//        import org.springframework.kafka.support.KafkaHeaders;
//        import org.springframework.messaging.handler.annotation.Header;
//        import org.springframework.stereotype.Component;
//
//        import java.util.List;
//
//@Component
//@Slf4j
//public class SecondConsumer {
//
//    private final DataService dataService;
//
//    @Autowired
//    public SecondConsumer(DataService dataService) {
//        this.dataService = dataService;
//    }
//
//    @KafkaListener(topics = SpringBootKafkaConfig.TOPIC_TEST_SECOND, groupId = SpringBootKafkaConfig.GROUP_ID)
//    public void listen(List<String> messages, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
//        log.info("Second consumer received messages");
//        for (String message : messages) {
//            final JSONObject payload = JSONUtil.parseObj(message);
//            String uuid = payload.getStr("uuid");
//            String name = payload.getStr("name");
//            String category = payload.getStr("category");
//            int quality = payload.getInt("quality");
//            boolean isQualified = payload.getBool("isQualified");
//            String receivedAt = payload.getStr("receivedAt");
//
//            // 将数据存储到数据库
//            dataService.addProductToDatabase(uuid, name, category, quality, isQualified, receivedAt);
//        }
//    }
//}
