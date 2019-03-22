package com.seeyoo.zm.visit.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class AssetsBean {
    private String name;
    private BigInteger avCount;
    public AssetsBean(String name, BigInteger avCount) {
        this.name = name;
        this.avCount = avCount;
    }

    public AssetsBean() {

    }
}
