package com.example.imfuyo;

public class LiveStockData {

    //Deklarasi Variable
    private String fotoTernak;
    private String namaTernak;
    private String namaPeternak;
    private String jenisTernak;
    private String kondisi;
    private String key;
    private String url;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFotoTernak() {
        return fotoTernak;
    }

    public void setFotoTernak(String fotoTernak) {
        this.fotoTernak = fotoTernak;
    }

    public String getNamaTernak() {
        return namaTernak;
    }

    public void setNamaTernak(String namaTernak) {
        this.namaTernak = namaTernak;
    }

    public String getNamaPeternak() {
        return namaPeternak;
    }

    public void setNamaPeternak(String namaPeternak) {
        this.namaPeternak = namaPeternak;
    }

    public String getJenisTernak() { return jenisTernak; }

    public void setJenisTernak(String jenisTernak){
        this.jenisTernak = jenisTernak;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    //Membuat Konstuktor kosong untuk membaca data snapshot
    public LiveStockData(){
    }

    //Konstruktor dengan beberapa parameter, untuk mendapatkan Input Data dari User
    public LiveStockData(String namaTernak, String namaPeternak, String jenisTernak,String url, String kondisi) {
        this.namaTernak = namaTernak;
        this.namaPeternak = namaPeternak;
        this.jenisTernak = jenisTernak;
        this.kondisi = kondisi;
        this.url=url;
    }
}
