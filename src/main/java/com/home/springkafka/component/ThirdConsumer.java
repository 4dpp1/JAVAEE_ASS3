package com.home.springkafka.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
import java.util.Date;
import java.util.List;


@Component
@Slf4j
public class ThirdConsumer {

    @Autowired
    private DataService dataService;

    @KafkaListener(topics = SpringBootKafkaConfig.TOPIC_TEST_THIRD, groupId = SpringBootKafkaConfig.GROUP_ID)
    public void topic_test(List<String> messages, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        for (String message : messages) {
            final JSONObject entries = JSONUtil.parseObj(message);
            String RFID = entries.getStr("RFID");
            String isQualified = entries.getStr("isQualified");

            // 写入到历史文件
            writeDataToHistory(RFID, isQualified);

            // 删除记录
            dataService.delete(RFID, isQualified);
            System.out.println("出库成功");
        }
    }

    private void writeDataToHistory(String RFID, String isQualified) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("history.txt", true))) {
            Date exportAt = new Date();
            writer.write("RFID: " + RFID + "," + " IS_QUALIFIED: " + isQualified + isQualified + "EXPORT_TIME" + exportAt + "\n");
        } catch (IOException e) {
            log.error("Error writing to history file: " + e.getMessage());
        }
    }
}
