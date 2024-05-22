package com.home.springkafka.service;

public interface DataService {
    void add(String data);

    void addProductToDatabase(String uuid, String name, String category, int quality, boolean isQualified, String receivedAt);

    void delete(String rfid, String isQualified);
}


//package com.atguigu.springkafka.service;
//
//public interface DataService {
//    void add(String data);
//
//    void addProductToDatabase(String uuid, String name, String category, int quality, boolean isQualified, String receivedAt);
//}
