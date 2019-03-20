package com.seeyoo.zm.visit.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Setter
@Getter
public class VisitTimeBean {
    private BigInteger visitTime;
    private String mac;
    private BigInteger visitCount;
    public VisitTimeBean(BigInteger visitTime,String mac,BigInteger visitCount){
        this.visitTime = visitTime;
        this.mac = mac;
        this.visitCount = visitCount;
    }
    public VisitTimeBean(){}
}
