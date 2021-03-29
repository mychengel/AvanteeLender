package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PortofolioAktif implements Parcelable {
    private String loan_type; //endpointB
    private String loan_rating; //endpointB
    private String loan_no;
    private String funding_id;
    private String interest;
    private String tenor;
    private String sisa_tenor;
    private String is_on_time;
    private String angs_paid;
    private String angs_next;

    public PortofolioAktif(){};

    public PortofolioAktif(String loan_type, String loan_rating, String loan_no, String funding_id, String interest, String tenor, String sisa_tenor, String is_on_time, String angs_paid, String angs_next) {
        this.loan_type = loan_type;
        this.loan_rating = loan_rating;
        this.loan_no = loan_no;
        this.funding_id = funding_id;
        this.interest = interest;
        this.tenor = tenor;
        this.sisa_tenor = sisa_tenor;
        this.is_on_time = is_on_time;
        this.angs_paid = angs_paid;
        this.angs_next = angs_next;
    }

    protected PortofolioAktif(Parcel in) {
        loan_type = in.readString();
        loan_rating = in.readString();
        loan_no = in.readString();
        funding_id = in.readString();
        interest = in.readString();
        tenor = in.readString();
        sisa_tenor = in.readString();
        is_on_time = in.readString();
        angs_paid = in.readString();
        angs_next = in.readString();
    }

    public static final Creator<PortofolioAktif> CREATOR = new Creator<PortofolioAktif>() {
        @Override
        public PortofolioAktif createFromParcel(Parcel in) {
            return new PortofolioAktif(in);
        }

        @Override
        public PortofolioAktif[] newArray(int size) {
            return new PortofolioAktif[size];
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

    public String getFunding_id() {
        return funding_id;
    }

    public String getInterest() {
        return interest;
    }

    public String getTenor() {
        return tenor;
    }

    public String getSisa_tenor() {
        return sisa_tenor;
    }

    public String getIs_on_time() {
        return is_on_time;
    }

    public String getAngs_paid() {
        return angs_paid;
    }

    public String getAngs_next() {
        return angs_next;
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
        parcel.writeString(funding_id);
        parcel.writeString(interest);
        parcel.writeString(tenor);
        parcel.writeString(sisa_tenor);
        parcel.writeString(is_on_time);
        parcel.writeString(angs_paid);
        parcel.writeString(angs_next);
    }
}
