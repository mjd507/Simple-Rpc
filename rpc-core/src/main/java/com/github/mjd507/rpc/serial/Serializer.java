package com.github.mjd507.rpc.serial;

/**
 * Create by majiandong on 2020/6/15 17:40
 */
public interface Serializer {

    byte[] serialize(Object obj);

    <T> T deserialize(byte[] bytes, Class<T> clazz);
}
