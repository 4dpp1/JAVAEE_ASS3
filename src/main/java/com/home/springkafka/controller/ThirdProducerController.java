

package com.home.springkafka.controller;

import cn.hutool.json.JSONUtil;
import com.home.springkafka.config.SpringBootKafkaConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
@Slf4j
public class ThirdProducerController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @ResponseBody
    @PostMapping(value = "/third-produce", produces = "application/json")
    public String produce(@RequestBody Object obj) {
        try {
            String obj2String = JSONUtil.toJsonStr(obj);
            kafkaTemplate.send(SpringBootKafkaConfig.TOPIC_TEST_THIRD, obj2String);
            return "Third producer success";
        } catch (Exception e) {
            log.error("Error producing message: {}", e.getMessage());
            return "Third producer error";
        }
    }
}

