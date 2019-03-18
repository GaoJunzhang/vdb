package com.seeyoo.zm.visit.bean;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigInteger;
import java.util.Date;

@Data
public class VisitStatisBean {
    private Date visitDate;
    private BigInteger visitCount;
    public VisitStatisBean(Date visitDate, BigInteger visitCount) {
        this.visitDate = visitDate;
        this.visitCount = visitCount;
    }

    public VisitStatisBean() {

    }
}
