package com.seeyoo.zm.visit.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Assets {
    private int id;
    private String mac;
    private Timestamp time;

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        Assets assets = (Assets) o;
        return id == assets.id &&
                Objects.equals(mac, assets.mac) &&
                Objects.equals(time, assets.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mac, time);
    }
}
