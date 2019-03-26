package com.seeyoo.zm.visit.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "visit_member", schema = "mps", catalog = "")
public class VisitMember {
    private long id;
    private Integer age;
    private Short gender;
    private Integer beauty;
    private Integer stay;
    private Timestamp stamp;
    private String mac;
    private Timestamp time;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "age", nullable = true)
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Basic
    @Column(name = "gender", nullable = true)
    public Short getGender() {
        return gender;
    }

    public void setGender(Short gender) {
        this.gender = gender;
    }

    @Basic
    @Column(name = "beauty", nullable = true)
    public Integer getBeauty() {
        return beauty;
    }

    public void setBeauty(Integer beauty) {
        this.beauty = beauty;
    }

    @Basic
    @Column(name = "stay", nullable = true)
    public Integer getStay() {
        return stay;
    }

    public void setStay(Integer stay) {
        this.stay = stay;
    }

    @Basic
    @Column(name = "stamp", nullable = true)
    public Timestamp getStamp() {
        return stamp;
    }

    public void setStamp(Timestamp stamp) {
        this.stamp = stamp;
    }

    @Basic
    @Column(name = "mac", nullable = true, length = 32)
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Basic
    @Column(name = "time", nullable = true)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitMember that = (VisitMember) o;
        return id == that.id &&
                Objects.equals(age, that.age) &&
                Objects.equals(gender, that.gender) &&
                Objects.equals(beauty, that.beauty) &&
                Objects.equals(stay, that.stay) &&
                Objects.equals(stamp, that.stamp) &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, gender, beauty, stay, stamp, mac, time);
    }
}
