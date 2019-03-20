package com.seeyoo.zm.visit.bean;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
public class DayVisitBean {
    private BigInteger visitDayCount;
    private String mac;
    private Timestamp time;

    public DayVisitBean(BigInteger visitDayCount,String mac,Timestamp time){
        this.visitDayCount = visitDayCount;
        this.mac= mac;
        this.time= time;
    }
    public DayVisitBean(){}
}
