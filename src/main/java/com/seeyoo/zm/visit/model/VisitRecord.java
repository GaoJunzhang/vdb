package com.seeyoo.zm.visit.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "visit_record", schema = "mps", catalog = "")
public class VisitRecord {
    private long id;
    private String mac;
    private Integer db;
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
    @Column(name = "mac", nullable = true, length = 32)
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    @Basic
    @Column(name = "db", nullable = true)
    public Integer getDb() {
        return db;
    }

    public void setDb(Integer db) {
        this.db = db;
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
        VisitRecord that = (VisitRecord) o;
        return id == that.id &&
                Objects.equals(mac, that.mac) &&
                Objects.equals(db, that.db) &&
                Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mac, db, time);
    }

    @Basic
    @Column(name = "assets_id", nullable = true)
    public Integer getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Integer assetsId) {
        this.assetsId = assetsId;
    }
}
