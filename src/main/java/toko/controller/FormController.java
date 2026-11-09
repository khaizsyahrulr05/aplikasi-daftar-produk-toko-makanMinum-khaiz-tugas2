package toko.controller;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import toko.Navigator;
import toko.data.DataProduk;

public class FormController {

    @FXML
    private TextField namaField;
    @FXML
    private ComboBox<String> kategoriBox;
    @FXML
    private TextField hargaField;
    @FXML
    private ImageView gambarView;
    @FXML
    private Label pesanLabel;

    private Navigator navigator;
    private DataProduk data;
    private String gambarUri;   // alamat gambar yang dipilih (null = belum ada)

    @FXML
    private void initialize() {
        kategoriBox.getItems().addAll("Makanan", "Minuman");
        kategoriBox.setValue("Makanan");
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
    }

    @FXML
    private void pilihGambar() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Pilih gambar produk");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Gambar PNG/JPEG", "*.png", "*.jpg", "*.jpeg"));
        File file = chooser.showOpenDialog(namaField.getScene().getWindow());
        if (file == null) {
            return; // dibatalkan: pilihan lama dipertahankan
        }
        if (file.length() > 5L * 1024 * 1024) {
            tampilPesan("Maksimum gambar 5 MB.", true);
            return;
        }
        Image image = new Image(file.toURI().toString(), 160, 160, true, true);
        if (image.isError()) {
            tampilPesan("Gambar tidak dapat dibaca.", true);
            return;
        }
        gambarUri = file.toURI().toString();
        gambarView.setImage(image);
    }

    @FXML
    private void hapusGambar() {
        gambarUri = null;
        gambarView.setImage(null);
    }

    @FXML
    private void simpan() {
        try {
            long harga = Long.parseLong(hargaField.getText().trim());
            data.tambah(namaField.getText(), kategoriBox.getValue(), harga, gambarUri);
        } catch (NumberFormatException e) {
            tampilPesan("Harga harus berupa bilangan bulat.", true);
            return;
        } catch (IllegalArgumentException e) {
            tampilPesan(e.getMessage(), true);
            return;
        }
        namaField.clear();
        hargaField.clear();
        kategoriBox.setValue("Makanan");
        hapusGambar();
        tampilPesan("Produk berhasil disimpan.", false);
    }

    @FXML
    private void bukaDaftar() {
        try {
            navigator.bukaDaftar();
        } catch (IOException e) {
            tampilPesan("Gagal membuka daftar.", true);
            e.printStackTrace();
        }
    }

    private void tampilPesan(String teks, boolean error) {
        pesanLabel.getStyleClass().setAll("label", error ? "error" : "success");
        pesanLabel.setText(teks);
    }
}