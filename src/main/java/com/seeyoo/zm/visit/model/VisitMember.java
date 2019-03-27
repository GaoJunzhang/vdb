package com.seeyoo.zm.visit.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "visit_member", schema = "vdb", catalog = "")
public class VisitMember {
    private long id;
    private Integer age;
    private Short gender;
    private Integer beauty;
    private Integer stay;
    private Timestamp stamp;
    private Timestamp time;
    private Integer assetsId;

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
    @Column(name = "time", nullable = true)
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "assets_id", nullable = true)
    public Integer getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Integer assetsId) {
        this.assetsId = assetsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VisitMember member = (VisitMember) o;
        return id == member.id &&
                Objects.equals(age, member.age) &&
                Objects.equals(gender, member.gender) &&
                Objects.equals(beauty, member.beauty) &&
                Objects.equals(stay, member.stay) &&
                Objects.equals(stamp, member.stamp) &&
                Objects.equals(time, member.time) &&
                Objects.equals(assetsId, member.assetsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, age, gender, beauty, stay, stamp, time, assetsId);
    }
}
