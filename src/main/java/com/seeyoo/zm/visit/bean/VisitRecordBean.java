package com.seeyoo.zm.visit.bean;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VisitRecordBean {
    private String visitDate;
    private int visitCount;
    private int vaildCount;
    private String vaildRate;
    private int passCount;

}
