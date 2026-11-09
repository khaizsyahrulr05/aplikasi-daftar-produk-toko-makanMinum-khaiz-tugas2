package toko.controller;

import java.io.IOException;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import toko.Navigator;
import toko.data.DataProduk;
import toko.model.Produk;

public class DaftarController {

    @FXML
    private TableView<Produk> tabel;
    @FXML
    private TableColumn<Produk, String> gambarCol;
    @FXML
    private TableColumn<Produk, String> kodeCol;
    @FXML
    private TableColumn<Produk, String> namaCol;
    @FXML
    private TableColumn<Produk, String> kategoriCol;
    @FXML
    private TableColumn<Produk, Long> hargaCol;
    @FXML
    private TextField cariField;
    @FXML
    private Label pesanLabel;

    private Navigator navigator;
    private DataProduk data;
    private final ObservableList<Produk> dataTampilan = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        kodeCol.setCellValueFactory(new PropertyValueFactory<>("kode"));
        namaCol.setCellValueFactory(new PropertyValueFactory<>("nama"));
        kategoriCol.setCellValueFactory(new PropertyValueFactory<>("kategori"));
        hargaCol.setCellValueFactory(new PropertyValueFactory<>("harga"));

        // Kolom gambar: ambil alamat gambar, lalu tampilkan sebagai gambar kecil
        gambarCol.setCellValueFactory(new PropertyValueFactory<>("gambarUri"));
        gambarCol.setCellFactory(kolom -> new TableCell<Produk, String>() {
            private final ImageView view = new ImageView();

            @Override
            protected void updateItem(String uri, boolean kosong) {
                super.updateItem(uri, kosong);
                if (kosong || uri == null) {
                    setGraphic(null);   // produk tanpa gambar: sel dikosongkan
                    return;
                }
                view.setImage(new Image(uri, 48, 48, true, true));
                setGraphic(view);
            }
        });
        tabel.setFixedCellSize(56);   // tinggi tiap baris sama

        tabel.setItems(dataTampilan);
        tabel.setPlaceholder(new Label("Belum ada produk yang sesuai."));
    }

    public void setKonteks(Navigator navigator, DataProduk data) {
        this.navigator = navigator;
        this.data = data;
        cari();
    }

    @FXML
    private void cari() {
        dataTampilan.clear();
        String kata = cariField.getText().trim().toLowerCase(Locale.ROOT);

        for (Produk produk : data.getDaftar()) {
            String nama = produk.getNama().toLowerCase(Locale.ROOT);
            if (nama.contains(kata)) {
                dataTampilan.add(produk);
            }
        }
        pesanLabel.setText("Jumlah produk yang tampil: " + dataTampilan.size());
    }

    @FXML
    private void resetPencarian() {
        cariField.clear();
        cari();
    }

    @FXML
    private void tambah() {
        try {
            navigator.bukaForm();
        } catch (IOException e) {
            pesanLabel.setText("Form gagal dibuka.");
            e.printStackTrace();
        }
    }

    @FXML
    private void detail() {
        Produk produk = tabel.getSelectionModel().getSelectedItem();
        if (produk == null) {
            pesanLabel.setText("Pilih satu produk dahulu.");
            return;
        }
        try {
            navigator.bukaDetail(produk);
        } catch (IOException e) {
            pesanLabel.setText("Detail gagal dibuka.");
            e.printStackTrace();
        }
    }
}