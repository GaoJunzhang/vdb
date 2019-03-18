package com.seeyoo.zm.visit.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Date;
import java.util.Objects;

@Entity
public class Calendar {
    private Date datelist;
    private int id;

    @Basic
    @Column(name = "datelist")
    public Date getDatelist() {
        return datelist;
    }

    public void setDatelist(Date datelist) {
        this.datelist = datelist;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calendar calendar = (Calendar) o;
        return Objects.equals(datelist, calendar.datelist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(datelist);
    }

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
