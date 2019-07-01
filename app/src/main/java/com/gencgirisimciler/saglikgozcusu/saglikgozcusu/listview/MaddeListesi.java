package com.gencgirisimciler.saglikgozcusu.saglikgozcusu.listview;

/**
 * Created by Batuhan on 10.08.2015.
 */
public class MaddeListesi {
    private String isim;
    private boolean aktifMi;
    private int position;


    public MaddeListesi(String isim, boolean aktifMi) {
        super();
        this.isim = isim;
        this.aktifMi = aktifMi;

    }
    public MaddeListesi(String isim, boolean aktifMi, int position) {
        super();
        this.isim = isim;
        this.aktifMi = aktifMi;
        this.position = position;

    }

    @Override
    public String toString() {
        return isim;
    }


    public String getIsim() {
        return isim;
    }

    public void setIsim(String isim) {
        this.isim = isim;
    }

    public boolean isAktifMi() {
        return aktifMi;
    }

    public void setAktifMi(boolean aktifMi) {
        this.aktifMi = aktifMi;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}