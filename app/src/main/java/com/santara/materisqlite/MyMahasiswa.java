package com.santara.materisqlite;

public class MyMahasiswa {
    private String nim,nama,path;
    private  int umur;

    public MyMahasiswa(String nim, String nama, String path, int umur) {
        this.nim = nim;
        this.nama = nama;
        this.path = path;
        this.umur = umur;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getUmur() {
        return umur;
    }

    public void setUmur(int umur) {
        this.umur = umur;
    }
}
