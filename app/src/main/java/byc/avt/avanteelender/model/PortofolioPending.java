package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PortofolioPending implements Parcelable {

    private String loan_type; //endpointB
    private String loan_rating; //endpointB
    private String loan_no;
    private String est_pengembalian_date;
    private String tenor;
    private String interest;
    private String nominal;
    private String est_bunga;
    private String is_paid;

    public PortofolioPending(){}

    public PortofolioPending(String loan_type, String loan_rating, String loan_no, String est_pengembalian_date, String tenor, String interest, String nominal, String est_bunga, String is_paid) {
        this.loan_type = loan_type;
        this.loan_rating = loan_rating;
        this.loan_no = loan_no;
        this.est_pengembalian_date = est_pengembalian_date;
        this.tenor = tenor;
        this.interest = interest;
        this.nominal = nominal;
        this.est_bunga = est_bunga;
        this.is_paid = is_paid;
    }

    protected PortofolioPending(Parcel in) {
        loan_type = in.readString();
        loan_rating = in.readString();
        loan_no = in.readString();
        est_pengembalian_date = in.readString();
        tenor = in.readString();
        interest = in.readString();
        nominal = in.readString();
        est_bunga = in.readString();
        is_paid = in.readString();
    }

    public static final Creator<PortofolioPending> CREATOR = new Creator<PortofolioPending>() {
        @Override
        public PortofolioPending createFromParcel(Parcel in) {
            return new PortofolioPending(in);
        }

        @Override
        public PortofolioPending[] newArray(int size) {
            return new PortofolioPending[size];
        }
    };

    public String getLoan_type() {
        return loan_type;
    }

    public String getLoan_rating() {
        return loan_rating;
    }

    public String getLoan_no() {
        return loan_no;
    }

    public String getEst_pengembalian_date() {
        return est_pengembalian_date;
    }

    public String getTenor() {
        return tenor;
    }

    public String getInterest() {
        return interest;
    }

    public String getNominal() {
        return nominal;
    }

    public String getEst_bunga() {
        return est_bunga;
    }

    public String getIs_paid() {
        return is_paid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(loan_type);
        parcel.writeString(loan_rating);
        parcel.writeString(loan_no);
        parcel.writeString(est_pengembalian_date);
        parcel.writeString(tenor);
        parcel.writeString(interest);
        parcel.writeString(nominal);
        parcel.writeString(est_bunga);
        parcel.writeString(is_paid);
    }
}
