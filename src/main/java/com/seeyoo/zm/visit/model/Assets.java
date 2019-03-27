package com.seeyoo.zm.visit.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Assets implements Serializable {
    private static final long serialVersionUID = -5567121585657132027L;
    private int id;
    private String mac;
    private Timestamp time;
    private String name;
    private Short type;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
    @Column(name = "time", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Basic
    @Column(name = "name", nullable = true, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Assets assets = (Assets) o;
        return id == assets.id &&
                Objects.equals(mac, assets.mac) &&
                Objects.equals(time, assets.time) &&
                Objects.equals(name, assets.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mac, time, name);
    }

    @Basic
    @Column(name = "type", nullable = true)
    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }
}
