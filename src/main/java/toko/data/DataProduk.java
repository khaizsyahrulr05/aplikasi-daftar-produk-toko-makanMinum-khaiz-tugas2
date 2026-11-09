package toko.data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import toko.model.Produk;


public class DataProduk {

    private final ObservableList<Produk> daftar = FXCollections.observableArrayList();
    private int urutan = 1;

    public ObservableList<Produk> getDaftar() {
        return FXCollections.unmodifiableObservableList(daftar);
    }

    public Produk tambah(String nama, String kategori, long harga, String gambarUri) {
        Produk p = new Produk(String.format("produk%03d", urutan), nama, kategori, harga, gambarUri);
        daftar.add(p);
        urutan++;
        return p;
    }
}