package com.example.kiemtra;

public class NhanVien {
    private String ma;
    private String hoten;
    private String email;
    private String chucvu;
    private String sodienthoai;
    private String anh;
    private String madonvi;

    public NhanVien() {
    }

    public NhanVien(String ma, String hoten, String email, String chucvu, String sodienthoai, String anh, String madonvi) {
        this.ma = ma;
        this.hoten = hoten;
        this.email = email;
        this.chucvu = chucvu;
        this.sodienthoai = sodienthoai;
        this.anh = anh;
        this.madonvi = madonvi;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getHoten() {
        return hoten;
    }

    public void setHoten(String hoten) {
        this.hoten = hoten;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChucvu() {
        return chucvu;
    }

    public void setChucvu(String chucvu) {
        this.chucvu = chucvu;
    }

    public String getSodienthoai() {
        return sodienthoai;
    }

    public void setSodienthoai(String sodienthoai) {
        this.sodienthoai = sodienthoai;
    }

    public String getAnh() {
        return anh;
    }

    public void setAnh(String anh) {
        this.anh = anh;
    }

    public String getMadonvi() {
        return madonvi;
    }

    public void setMadonvi(String madonvi) {
        this.madonvi = madonvi;
    }
}
