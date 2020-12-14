package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pendanaan implements Parcelable {

    private String loan_type;
    private String rating_pinjaman;
    private String loan_no;
    private String jumlah_hari_pinjam;
    private String invest_bunga;
    private String nominal_pinjaman;
    private String funding;
    private String jaminan_status;
    private String tipe_jaminan;
    private String city_name;
    private String publikasi_end;
    private String borrower_code;
    private String picture_bg;

    public Pendanaan(){}

    public Pendanaan(String loan_type, String rating_pinjaman, String loan_no, String jumlah_hari_pinjam, String invest_bunga, String nominal_pinjaman, String funding, String jaminan_status, String tipe_jaminan, String city_name, String publikasi_end, String borrower_code, String picture_bg) {
        this.loan_type = loan_type;
        this.rating_pinjaman = rating_pinjaman;
        this.loan_no = loan_no;
        this.jumlah_hari_pinjam = jumlah_hari_pinjam;
        this.invest_bunga = invest_bunga;
        this.nominal_pinjaman = nominal_pinjaman;
        this.funding = funding;
        this.jaminan_status = jaminan_status;
        this.tipe_jaminan = tipe_jaminan;
        this.city_name = city_name;
        this.publikasi_end = publikasi_end;
        this.borrower_code = borrower_code;
        this.picture_bg = picture_bg;
    }

    protected Pendanaan(Parcel in) {
        loan_type = in.readString();
        rating_pinjaman = in.readString();
        loan_no = in.readString();
        jumlah_hari_pinjam = in.readString();
        invest_bunga = in.readString();
        nominal_pinjaman = in.readString();
        funding = in.readString();
        jaminan_status = in.readString();
        tipe_jaminan = in.readString();
        city_name = in.readString();
        publikasi_end = in.readString();
        borrower_code = in.readString();
        picture_bg = in.readString();
    }

    public static final Creator<Pendanaan> CREATOR = new Creator<Pendanaan>() {
        @Override
        public Pendanaan createFromParcel(Parcel in) {
            return new Pendanaan(in);
        }

        @Override
        public Pendanaan[] newArray(int size) {
            return new Pendanaan[size];
        }
    };

    public String getLoan_type() {
        return loan_type;
    }

    public String getRating_pinjaman() {
        return rating_pinjaman;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public String getJumlah_hari_pinjam() {
        return jumlah_hari_pinjam;
    }

    public String getInvest_bunga() {
        return invest_bunga;
    }

    public String getNominal_pinjaman() {
        return nominal_pinjaman;
    }

    public String getFunding() {
        return funding;
    }

    public String getJaminan_status() {
        return jaminan_status;
    }

    public String getTipe_jaminan() {
        return tipe_jaminan;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getPublikasi_end() {
        return publikasi_end;
    }

    public String getBorrower_code() {
        return borrower_code;
    }

    public String getPicture_bg() {
        return picture_bg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(loan_type);
        parcel.writeString(rating_pinjaman);
        parcel.writeString(loan_no);
        parcel.writeString(jumlah_hari_pinjam);
        parcel.writeString(invest_bunga);
        parcel.writeString(nominal_pinjaman);
        parcel.writeString(funding);
        parcel.writeString(jaminan_status);
        parcel.writeString(tipe_jaminan);
        parcel.writeString(city_name);
        parcel.writeString(publikasi_end);
        parcel.writeString(borrower_code);
        parcel.writeString(picture_bg);
    }
}
