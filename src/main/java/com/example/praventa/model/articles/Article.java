package com.example.praventa.model.articles;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "article")
public class Article {

    private String tanggal;
    private String judul;
    private String deskripsi;
    private String imagePath;

    public Article() {
        // Konstruktor kosong diperlukan untuk JAXB
    }

    public Article(String tanggal, String judul, String deskripsi, String imagePath) {
        this.tanggal = tanggal;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.imagePath = imagePath;
    }

    public String getTanggal() {
        return tanggal;
    }

    @XmlElement
    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJudul() {
        return judul;
    }

    @XmlElement
    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @XmlElement
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getImagePath() {
        return imagePath;
    }

    @XmlElement
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}