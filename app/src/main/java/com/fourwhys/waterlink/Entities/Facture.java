package com.fourwhys.waterlink.Entities;

public class Facture {
    private int id;
    private String periode;
    private String status;

    public Facture(int id, String periode, String status) {
        this.id = id;
        this.periode = periode;
        this.status = status;
    }

    public Facture() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Facture{" +
                "id=" + id +
                ", periode='" + periode + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
