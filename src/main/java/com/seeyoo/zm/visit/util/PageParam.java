package com.seeyoo.zm.visit.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageParam {
    private int start;
    private int length;
    private int page;

    public PageParam(int page,int length){
        this.page= page;
        this.length=length;
        this.start=(page-1)*length;
    }
}
