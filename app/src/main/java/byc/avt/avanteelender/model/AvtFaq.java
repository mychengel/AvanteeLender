package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Spanned;

public class AvtFaq {

    private Spanned title;
    private String content;

    public AvtFaq(){}

    public AvtFaq(Spanned title, String content) {
        this.title = title;
        this.content = content;
    }

//    protected AvtFaq(Parcel in) {
//        title = in.readString();
//        content = in.readString();
//    }

//    public static final Creator<AvtFaq> CREATOR = new Creator<AvtFaq>() {
//        @Override
//        public AvtFaq createFromParcel(Parcel in) {
//            return new AvtFaq(in);
//        }
//
//        @Override
//        public AvtFaq[] newArray(int size) {
//            return new AvtFaq[size];
//        }
//    };

    public Spanned getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }

//    @Override
//    public void writeToParcel(Parcel parcel, int i) {
//        parcel.writeString(title);
//        parcel.writeString(content);
//    }
}
