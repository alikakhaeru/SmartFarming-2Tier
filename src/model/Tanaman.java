package model;

public class Tanaman {
    private int id;
    private String nama;
    private String jenis;

    public Tanaman() {}

    public Tanaman(int id, String nama, String jenis) {
        this.id = id;
        this.nama = nama;
        this.jenis = jenis;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getJenis() { return jenis; }
    public void setJenis(String jenis) { this.jenis = jenis; }

    public Object getLahanId() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLahanId'");
    }
}
