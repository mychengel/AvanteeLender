package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class User implements Parcelable {
    private String email, no_handphone, password, referral_code;

    public User() {
    }

    public User(String email, String no_handphone, String password, String referral_code) {
        this.email = email;
        this.no_handphone = no_handphone;
        this.password = password;
        this.referral_code = referral_code;
    }

    protected User(Parcel in) {
        email = in.readString();
        no_handphone = in.readString();
        password = in.readString();
        referral_code = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNo_handphone() {
        return no_handphone;
    }

    public void setNo_handphone(String no_handphone) {
        this.no_handphone = no_handphone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferral_code() {
        return referral_code;
    }

    public void setReferral_code(String referral_code) {
        this.referral_code = referral_code;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(email);
        dest.writeString(no_handphone);
        dest.writeString(password);
        dest.writeString(referral_code);
    }
}
