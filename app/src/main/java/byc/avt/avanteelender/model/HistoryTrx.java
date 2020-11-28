package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class HistoryTrx implements Parcelable {

    private String description;
    private String trx_date;
    private String nominal;
    private String status;

    public HistoryTrx(){}

    public HistoryTrx(String description, String trx_date, String nominal, String status) {
        this.description = description;
        this.trx_date = trx_date;
        this.nominal = nominal;
        this.status = status;
    }

    protected HistoryTrx(Parcel in) {
        description = in.readString();
        trx_date = in.readString();
        nominal = in.readString();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(trx_date);
        dest.writeString(nominal);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<HistoryTrx> CREATOR = new Creator<HistoryTrx>() {
        @Override
        public HistoryTrx createFromParcel(Parcel in) {
            return new HistoryTrx(in);
        }

        @Override
        public HistoryTrx[] newArray(int size) {
            return new HistoryTrx[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public String getTrx_date() {
        return trx_date;
    }

    public String getNominal() {
        return nominal;
    }

    public String getStatus() {
        return status;
    }
}
