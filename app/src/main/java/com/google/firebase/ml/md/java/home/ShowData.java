package com.google.firebase.ml.md.java.home;

public class ShowData {
    public String barang_id, kurir, lokasi;

    public ShowData(){

    }

    public ShowData(String barang_id, String kurir, String lokasi) {
        this.barang_id = barang_id;
        this.kurir = kurir;
        this.lokasi = lokasi;
    }

    public String getBarang_id() {
        return barang_id;
    }

    public void setBarang_id(String barang_id) {
        this.barang_id = barang_id;
    }

    public String getNama() {
        return kurir;
    }

    public void setNama(String kurir) {
        this.kurir = kurir;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
