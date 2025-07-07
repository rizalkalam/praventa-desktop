package com.example.praventa.model;

public class Artikel {
    private String tanggal;
    private String judul;
    private String deskripsi;
    private String imagePath;

    public Artikel(String tanggal, String judul, String deskripsi, String imagePath) {
        this.tanggal = tanggal;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
    }

    public String getTanggal() {
        return tanggal;
    }

    public String getJudul() {
        return judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getImagePath() {
        return imagePath;
    }
}
