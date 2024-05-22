package com.home.springkafka.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MessageMapper {
    @Insert("INSERT INTO message (data) VALUES (#{data})")
    void insertMessage(@Param("data") String data);

    @Insert("INSERT INTO message (data) VALUES ('wdaawdawdw')")
    void add(String data);

    @Insert("INSERT INTO qualified_products (RFID, name, category, quality, is_qualified, received_at) VALUES ('${uuid}', '${name}', '${category}',${quality},${isQualified}, '${receivedAt}')")
    void addQualifiedProductToDatabase(@Param("uuid") String uuid, @Param("name") String name, @Param("category") String category, int quality, boolean isQualified, @Param("receivedAt") String receivedAt);

    @Insert("INSERT INTO unqualified_products (RFID, name, category, quality, is_qualified, received_at) VALUES ('${uuid}', '${name}', '${category}',${quality},${isQualified}, '${receivedAt}')")
    void addUnqualifiedProductToDatabase(@Param("uuid") String uuid, @Param("name") String name, @Param("category") String category, int quality, boolean isQualified, @Param("receivedAt") String receivedAt);

    @Delete("DELETE FROM qualified_products WHERE RFID = #{rfid}")
    void qualified_delete(String rfid);

    @Delete("DELETE FROM unqualified_products WHERE RFID = #{rfid}")
    void unqualified_delete(String rfid);
}

//    @Insert("INSERT INTO products (RFID, name, category,quality,is_qualified, received_at) VALUES (${uuid}, ${name}, ${category},${quality},${isQualified}, ${receivedAt})")
//    void addProductToDatabase(String uuid, String name, String category, int quality, boolean isQualified, String receivedAt);
