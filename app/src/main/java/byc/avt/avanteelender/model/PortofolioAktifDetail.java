package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PortofolioAktifDetail implements Parcelable {

    private String periode;
    private String date_payment;
    private String date_actualtrans;
    private String principal_payment;
    private String interest_amount;
    private String payment_amount;
    private String actual_payment;
    private String tax;
    private String status;
    private String delay_details;

    public PortofolioAktifDetail(){}

    public PortofolioAktifDetail(String periode, String date_payment, String date_actualtrans, String principal_payment, String interest_amount, String payment_amount, String actual_payment, String tax, String status, String delay_details) {
        this.periode = periode;
        this.date_payment = date_payment;
        this.date_actualtrans = date_actualtrans;
        this.principal_payment = principal_payment;
        this.interest_amount = interest_amount;
        this.payment_amount = payment_amount;
        this.actual_payment = actual_payment;
        this.tax = tax;
        this.status = status;
        this.delay_details = delay_details;
    }

    protected PortofolioAktifDetail(Parcel in) {
        periode = in.readString();
        date_payment = in.readString();
        date_actualtrans = in.readString();
        principal_payment = in.readString();
        interest_amount = in.readString();
        payment_amount = in.readString();
        actual_payment = in.readString();
        tax = in.readString();
        status = in.readString();
        delay_details = in.readString();
    }

    public static final Creator<PortofolioAktifDetail> CREATOR = new Creator<PortofolioAktifDetail>() {
        @Override
        public PortofolioAktifDetail createFromParcel(Parcel in) {
            return new PortofolioAktifDetail(in);
        }

        @Override
        public PortofolioAktifDetail[] newArray(int size) {
            return new PortofolioAktifDetail[size];
        }
    };

    public String getPeriode() {
        return periode;
    }

    public String getDate_payment() {
        return date_payment;
    }

    public String getDate_actualtrans() {
        return date_actualtrans;
    }

    public String getPrincipal_payment() {
        return principal_payment;
    }

    public String getInterest_amount() {
        return interest_amount;
    }

    public String getPayment_amount() {
        return payment_amount;
    }

    public String getActual_payment() {
        return actual_payment;
    }

    public String getTax() {
        return tax;
    }

    public String getStatus() {
        return status;
    }

    public String getDelay_details() {
        return delay_details;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(periode);
        parcel.writeString(date_payment);
        parcel.writeString(date_actualtrans);
        parcel.writeString(principal_payment);
        parcel.writeString(interest_amount);
        parcel.writeString(payment_amount);
        parcel.writeString(actual_payment);
        parcel.writeString(tax);
        parcel.writeString(status);
        parcel.writeString(delay_details);
    }
}
