package com.seeyoo.zm.visit.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

@Data
public class VisitCameraBean {
    private Date visitDate;
    private BigInteger visitCount;
    private Short gender;
    private BigDecimal stayCount;
    public VisitCameraBean(Date visitDate, BigInteger visitCount,Short gender,BigDecimal stayCount) {
        this.visitDate = visitDate;
        this.visitCount = visitCount;
        this.gender = gender;
        this.stayCount = stayCount;
    }
//
    public VisitCameraBean() {

    }
}
