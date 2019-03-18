package org.fage.vo;

import lombok.Data;

import java.math.BigInteger;

@Data
public class VisitStatisBean {
    private String visitDate;
    private BigInteger visitCount;
    //    private int vaildCount;
//    private long vaildRate;
//    private int passCount;
    public VisitStatisBean(String visitDate, BigInteger visitCount) {
        this.visitDate = visitDate;
        this.visitCount = visitCount;
    }

    public VisitStatisBean() {
        super();
    }
}
