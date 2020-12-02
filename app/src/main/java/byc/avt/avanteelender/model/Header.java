package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Header implements Parcelable {

    private String user_type;
    private String user_code;
    private String joint_date;
    private String reff_code;
    private String no_handphone;
    private String email;

    public Header(String user_type, String user_code, String joint_date, String reff_code, String no_handphone, String email) {
        this.user_type = user_type;
        this.user_code = user_code;
        this.joint_date = joint_date;
        this.reff_code = reff_code;
        this.no_handphone = no_handphone;
        this.email = email;
    }

    protected Header(Parcel in) {
        user_type = in.readString();
        user_code = in.readString();
        joint_date = in.readString();
        reff_code = in.readString();
        no_handphone = in.readString();
        email = in.readString();
    }

    public static final Creator<Header> CREATOR = new Creator<Header>() {
        @Override
        public Header createFromParcel(Parcel in) {
            return new Header(in);
        }

        @Override
        public Header[] newArray(int size) {
            return new Header[size];
        }
    };

    public String getUser_type() {
        return user_type;
    }

    public String getUser_code() {
        return user_code;
    }

    public String getJoint_date() {
        return joint_date;
    }

    public String getReff_code() {
        return reff_code;
    }

    public String getNo_handphone() {
        return no_handphone;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_type);
        dest.writeString(user_code);
        dest.writeString(joint_date);
        dest.writeString(reff_code);
        dest.writeString(no_handphone);
        dest.writeString(email);
    }
}
