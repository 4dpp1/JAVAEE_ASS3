package com.home.springkafka.component;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.home.springkafka.config.SpringBootKafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Component
@Slf4j
public class FirstConsumer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public FirstConsumer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = SpringBootKafkaConfig.TOPIC_TEST, groupId = SpringBootKafkaConfig.GROUP_ID)
    public void listen(List<String> messages, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) throws InterruptedException {
        for (String message : messages) {
            System.out.println(message);
            final JSONObject payload = JSONUtil.parseObj(message);

            String name = payload.getStr("name");
            String category = payload.getStr("category");
            int quality = payload.getInt("quality");

            log.info("First consumer received message: {}", message);

            // 生成 UUID
            UUID uuid = UUID.randomUUID();

            // 评估质量是否合格
            boolean isQualified = quality >= 5;

            // 添加接收时间
            // 创建当前时间
            Date receivedAt = new Date();

            // 创建 Calendar 对象，并设置为 receivedAt 的时间
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(receivedAt);

            // 在 Calendar 对象上增加十分钟
            calendar.add(Calendar.MINUTE, 10);

            // 获取增加后的时间
            receivedAt = calendar.getTime();

            // 构造新的 JSON 数据
            JSONObject newPayload = new JSONObject();
            newPayload.put("uuid", uuid.toString());
            newPayload.put("name", name);
            newPayload.put("category", category);
            newPayload.put("quality", quality);
            newPayload.put("isQualified", isQualified);
            newPayload.put("receivedAt", receivedAt.toString());
            message = newPayload.toString();

            String data = payload.getStr("data");
            log.info("First consumer received message: {}", data);
            // 发送消息到第二个生产者
            kafkaTemplate.send(SpringBootKafkaConfig.TOPIC_TEST_SECOND, message);
        }
    }
}

