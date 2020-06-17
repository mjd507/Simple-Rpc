package com.github.mjd507.rpc.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by majiandong on 2020/6/17 11:49
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloEntity {
    private String name;
    private int age;
    private String msg;
}
