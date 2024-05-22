package com.home.springkafka.service.impl;

import com.home.springkafka.mapper.MessageMapper;
import com.home.springkafka.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DataServiceImpl implements DataService {
    @Autowired
    MessageMapper messageMapper;

    @Override
    public void add(String data) {
        messageMapper.add(data);
    }

    @Override
    public void addProductToDatabase(String uuid, String name, String category, int quality, boolean isQualified, String receivedAt) {
        System.out.println(isQualified);
        if (isQualified) {
            messageMapper.addQualifiedProductToDatabase(uuid, name, category,quality, true, receivedAt);
        } else {
            messageMapper.addUnqualifiedProductToDatabase(uuid, name, category,quality, false, receivedAt);
        }

    }

    @Override
    public void delete(String rfid, String isQualified) {
        if (Objects.equals(isQualified, "true")) {
            messageMapper.qualified_delete(rfid);
        } else {
            messageMapper.unqualified_delete(rfid);
        }
    }
}
