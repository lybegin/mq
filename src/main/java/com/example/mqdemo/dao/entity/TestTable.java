package com.example.mqdemo.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TestTable {

    private Long id;

    private String name;

    private int sex;

    private Date birth;

    private Long logId;
}
