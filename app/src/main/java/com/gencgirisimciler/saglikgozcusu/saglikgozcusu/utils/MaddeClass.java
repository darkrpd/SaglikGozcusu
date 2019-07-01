package com.gencgirisimciler.saglikgozcusu.saglikgozcusu.utils;

/**
 * Created by Bdeppy on 9.06.2016.
 */
public class MaddeClass {
    String maddeAdi;
    String maddeAciklamasi;

    public MaddeClass(String maddeAdi, String maddeAciklamasi) {
        this.maddeAdi = maddeAdi;
        this.maddeAciklamasi = maddeAciklamasi;
    }

    public String getMaddeAdi() {
        return maddeAdi;
    }

    public void setMaddeAdi(String maddeAdi) {
        this.maddeAdi = maddeAdi;
    }

    public String getMaddeAciklamasi() {
        return maddeAciklamasi;
    }

    public void setMaddeAciklamasi(String maddeAciklamasi) {
        this.maddeAciklamasi = maddeAciklamasi;
    }
}
