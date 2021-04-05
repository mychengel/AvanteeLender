package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {

    private String email;
    private String password;
    private String uid;
    private int type;
    private String client_type;
    private String avatar;
    private String name;
    private int avantee_verif;
    private String token;
    private long expired_time;

    public UserData(){}

    public UserData(String email, String password, String uid, int type, String client_type, String avatar, String name, int avantee_verif, String token, long expired_time) {
        this.email = email;
        this.password = password;
        this.uid = uid;
        this.type = type;
        this.client_type = client_type;
        this.avatar = avatar;
        this.name = name;
        this.avantee_verif = avantee_verif;
        this.token = token;
        this.expired_time = expired_time;
    }

    protected UserData(Parcel in) {
        email = in.readString();
        password = in.readString();
        uid = in.readString();
        type = in.readInt();
        client_type = in.readString();
        avatar = in.readString();
        name = in.readString();
        avantee_verif = in.readInt();
        token = in.readString();
        expired_time = in.readLong();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public int getType() {
        return type;
    }

    public String getClient_type() {
        return client_type;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public int getAvantee_verif() {
        return avantee_verif;
    }

    public String getToken() {
        return token;
    }

    public long getExpired_time() {
        return expired_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(uid);
        parcel.writeInt(type);
        parcel.writeString(client_type);
        parcel.writeString(avatar);
        parcel.writeString(name);
        parcel.writeInt(avantee_verif);
        parcel.writeString(token);
        parcel.writeLong(expired_time);
    }
}
