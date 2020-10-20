package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {

    private String uid;
    private int type;
    private String client_type;
    private String avatar;
    private String name;
    private int avantee_verif;
    private String token;

    public UserData(){}

    public UserData(String uid, int type, String client_type, String avatar, String name, int avantee_verif, String token) {
        this.uid = uid;
        this.type = type;
        this.client_type = client_type;
        this.avatar = avatar;
        this.name = name;
        this.avantee_verif = avantee_verif;
        this.token = token;
    }

    protected UserData(Parcel in) {
        uid = in.readString();
        type = in.readInt();
        client_type = in.readString();
        avatar = in.readString();
        name = in.readString();
        avantee_verif = in.readInt();
        token = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeInt(type);
        dest.writeString(client_type);
        dest.writeString(avatar);
        dest.writeString(name);
        dest.writeInt(avantee_verif);
        dest.writeString(token);
    }
}
