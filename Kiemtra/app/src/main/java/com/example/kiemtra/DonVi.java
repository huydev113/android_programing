package com.example.kiemtra;

import java.io.Serializable;

public class DonVi implements Serializable {
    private String ma;
    private String ten;
    private String email;
    private String web;
    private String sodienthoai;
    private String logo;
    private String diachi;

    public DonVi() {
    }

    public DonVi(String ma, String ten, String email, String web, String sodienthoai, String logo, String diachi) {
        this.ma = ma;
        this.ten = ten;
        this.email = email;
        this.web = web;
        this.sodienthoai = sodienthoai;
        this.logo = logo;
        this.diachi = diachi;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    @Override
    public String toString() {
        return ten;
    }
}
