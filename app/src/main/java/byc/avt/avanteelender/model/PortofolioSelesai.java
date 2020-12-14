package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PortofolioSelesai implements Parcelable {

    private String loan_type; //endpointB
    private String loan_rating; //endpointB
    private String loan_no;
    private String end_date;
    private String tenor;
    private String interest;
    private String nominal;
    private String payment_amount;

    public PortofolioSelesai(){}

    public PortofolioSelesai(String loan_type, String loan_rating, String loan_no, String end_date, String tenor, String interest, String nominal, String payment_amount) {
        this.loan_type = loan_type;
        this.loan_rating = loan_rating;
        this.loan_no = loan_no;
        this.end_date = end_date;
        this.tenor = tenor;
        this.interest = interest;
        this.nominal = nominal;
        this.payment_amount = payment_amount;
    }

    protected PortofolioSelesai(Parcel in) {
        loan_type = in.readString();
        loan_rating = in.readString();
        loan_no = in.readString();
        end_date = in.readString();
        tenor = in.readString();
        interest = in.readString();
        nominal = in.readString();
        payment_amount = in.readString();
    }

    public static final Creator<PortofolioSelesai> CREATOR = new Creator<PortofolioSelesai>() {
        @Override
        public PortofolioSelesai createFromParcel(Parcel in) {
            return new PortofolioSelesai(in);
        }

        @Override
        public PortofolioSelesai[] newArray(int size) {
            return new PortofolioSelesai[size];
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

    public String getEnd_date() {
        return end_date;
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

    public String getPayment_amount() {
        return payment_amount;
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
        parcel.writeString(end_date);
        parcel.writeString(tenor);
        parcel.writeString(interest);
        parcel.writeString(nominal);
        parcel.writeString(payment_amount);
    }
}
