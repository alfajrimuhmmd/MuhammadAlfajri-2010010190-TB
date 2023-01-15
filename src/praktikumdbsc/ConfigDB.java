package praktikumdbsc;

import com.mysql.jdbc.Connection;
import java.sql.*;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

public class ConfigDB {

    private String Password = "";
    private String Username = "simpan_pinjam";
    private String Database = "simpan_pinjam";

    public Connection koneksi;

    public ConfigDB() {

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            koneksi = (Connection) DriverManager.getConnection("jdbc:mysql://"+new iplogin().ipconect+":3306/" + this.Database, this.Username, this.Password);
            System.out.println("Koneksi Berhasil");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian : \n[" + e.toString() + "]");

        }
    }

    public Object[][] isiTable(String SQL, int jumlah) {
        Object[][] data = null;
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            rs.last();
            int baris = rs.getRow();
            rs.beforeFirst();
            int j = 0;
            data = new Object[baris][jumlah];
            while (rs.next()) {

                for (int i = 0; i < jumlah; i++) {
                    data[j][i] = rs.getString(i + 1);
                }
                j++;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian method isiTable : \n [" + e.toString() + "]");

        }
        return data;
    }

    public void tampilTable(String Judul[], String SQL, JTable Table) {
        try {
            String title[] = Judul;
            int jum = title.length;
            Table.setModel(new DefaultTableModel(isiTable(SQL, jum), title));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian Method tampilTable: \n [" + e.toString() + "]");
        }
    }

    public void aturLebarKolom(JTable table, int baris[]) {
        try {
            int getBaris[] = baris;
            int JumlahBaris = getBaris.length;
            table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
            for (int i = 0; i < JumlahBaris - 1; i++) {
                table.getColumnModel().getColumn(i).setPreferredWidth(getBaris[i]);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahn pada bagian method aturLebarKolom : \n[" + e.toString() + "]");
        }
    }

    public void simpanDataStatement(String SQL) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            st.execute(SQL);
            st.close();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahn pada bagian method simpanDataStatement : \n[" + e.toString() + "]");

        }

    }

    public String getValueKolom(int jumlah) {
        String hasil = "";
        int deteksi = jumlah - 1;
        for (int i = 0; i < jumlah; i++) {

            if (i == deteksi) {
                hasil = hasil + "?";
            } else {
                hasil = hasil + "?" + ",";
            }

        }
        return "(" + hasil + ")";
    }

    public void simpanPrepareStatement(String namaTabel, String[] nilaiKolom) {
        try {
            int jumlah = nilaiKolom.length;
            System.out.println(jumlah);
            String SQL = "INSERT INTO " + namaTabel + " VALUES " + getValueKolom(jumlah);

            PreparedStatement simpanData = ConfigDB.this.koneksi.prepareStatement(SQL);
            for (int i = 0; i < jumlah; i++) {
                simpanData.setString(i + 1, nilaiKolom[i]);

            }
            simpanData.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahn pada bagian method simpanPrepareStatement : \n[" + e.toString() + "]");
        }
    }

    public String getUpdateValue(String[] field) {
        String hasil = "";
        int deteksi = field.length - 1;
        for (int i = 0; i < field.length; i++) {

            if (i == deteksi) {
                hasil = hasil + field[i] + " = ?";
            } else {
                hasil = hasil + field[i] + " = ?" + ",";
            }

        }
        return hasil;
    }

    public void ubahDataPrepareStatement(String namaTabel, String primaryKey, String valuePrimaryKey, String[] field, String[] value) {

        try {
            int jumlah = field.length + 1;
            int deteksi = jumlah - 1;
            System.out.println(jumlah);

            String SQL = " UPDATE " + namaTabel + " SET " + getUpdateValue(field) + " WHERE " + primaryKey + " = ?";
            System.out.println(jumlah);
            PreparedStatement ubahData = ConfigDB.this.koneksi.prepareStatement(SQL);
            for (int i = 0; i < jumlah; i++) {

                if (i == deteksi) {
                    ubahData.setString(i + 1, valuePrimaryKey);
                } else {
                    ubahData.setString(i + 1, value[i]);
                }

            }
            ubahData.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahn pada bagian method simpanPrepareStatement : \n[" + e.toString() + "]");

        }

    }

    public void ubahData(String SQL) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            st.execute(SQL);
            st.close();
            JOptionPane.showMessageDialog(null, "Data berhasil diubah");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian method ubahData : \n[" + e.toString() + "]");

        }
    }

    public void hapusData(String SQL) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            st.execute(SQL);
            st.close();
            JOptionPane.showMessageDialog(null, "Data berhasil dihapus");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahn pada bagian method hapusData : \n[" + e.toString() + "]");

        }
    }

    public boolean duplikasiData(String table, String Key, String nilai) {
        boolean ada = false;
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            ResultSet rs = st.executeQuery(" SELECT*FROM " + table + " WHERE " + Key + "=" + nilai);
            if (rs.next()) {
                ada = true;
            } else {
                ada = false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian method duplikasiData : \n [" + e.toString() + "]");
        }
        return ada;
    }

    public void cariData(String Judul[], String Cari, JTable Table) {
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            String title[] = Judul;
            int jum = title.length;
            Table.setModel(new DefaultTableModel(isiTable(Cari, jum), title));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian method cariData : \n[" + e.toString() + "]");
        }
    }

    public void tampilLaporan(String laporanFile, String SQL) {
        try {
            File file = new File(laporanFile);
            JasperDesign jasDes = JRXmlLoader.load(file);

            JRDesignQuery sqlQuery = new JRDesignQuery();
            sqlQuery.setText(SQL);
            jasDes.setQuery(sqlQuery);

            JasperReport JR = JasperCompileManager.compileReport(jasDes);
            JasperPrint JP = JasperFillManager.fillReport(JR, null, ConfigDB.this.koneksi);
            JasperViewer.viewReport(JP, false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian method TampilLaporan : \n [" + e.toString() + "]");
        }
    }

    public int jumlahRecord(String SQL) {
        int hasil = 0;
        int i = 0;
        try {
            Statement st = ConfigDB.this.koneksi.createStatement();
            ResultSet rs = st.executeQuery(SQL);
            while (rs.next()) {
                i++;
            }
            hasil = i;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Maaf Terjadi Kesalahan pada bagian method JumlahRecord : \n [" + e.toString() + "]");
        }
        return hasil;
    }

    

}