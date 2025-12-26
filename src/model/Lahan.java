package model;

public class Lahan {
    private int id;
    private String namaLahan;
    private String lokasi;
    private double luas;
    private int tanamanId;

    public Lahan(int id, String namaLahan, String lokasi, double luas, int tanamanId) {
        this.id = id;
        this.namaLahan = namaLahan;
        this.lokasi = lokasi;
        this.luas = luas;
        this.tanamanId = tanamanId;
    }

    public int getId() { return id; }
    public String getNamaLahan() { return namaLahan; }
    public String getLokasi() { return lokasi; }
    public double getLuas() { return luas; }
    public int getTanamanId() { return tanamanId; }

    public void setId(int id) { this.id = id; }
    public void setNamaLahan(String namaLahan) { this.namaLahan = namaLahan; }
    public void setLokasi(String lokasi) { this.lokasi = lokasi; }
    public void setLuas(double luas) { this.luas = luas; }
    public void setTanamanId(int tanamanId) { this.tanamanId = tanamanId; }
}
