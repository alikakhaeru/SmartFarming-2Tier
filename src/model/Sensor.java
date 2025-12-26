package model;

import java.sql.Timestamp;

public class Sensor {
    private int id;
    private int lahanId;
    private double suhu;
    private double kelembapanTanah;
    private double intensitasCahaya;
    private Timestamp waktu;

    public Sensor(int id, int lahanId, double suhu,
                  double kelembapanTanah, double intensitasCahaya,
                  Timestamp waktu) {
        this.id = id;
        this.lahanId = lahanId;
        this.suhu = suhu;
        this.kelembapanTanah = kelembapanTanah;
        this.intensitasCahaya = intensitasCahaya;
        this.waktu = waktu;
    }

    public int getId() { return id; }
    public int getLahanId() { return lahanId; }
    public double getSuhu() { return suhu; }
    public double getKelembapanTanah() { return kelembapanTanah; }
    public double getIntensitasCahaya() { return intensitasCahaya; }
    public Timestamp getWaktu() { return waktu; }

    public void setId(int id) { this.id = id; }
}
