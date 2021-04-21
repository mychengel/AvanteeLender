package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataPart implements Parcelable {
    private String fileName;
    private byte[] content;
    private String type;

    public DataPart() {
    }

    public DataPart(String name, byte[] data) {
        fileName = name;
        content = data;
    }

    public DataPart(String name, byte[] data, String mimeType) {
        fileName = name;
        content = data;
        type = mimeType;
    }

    protected DataPart(Parcel in) {
        fileName = in.readString();
        content = in.createByteArray();
        type = in.readString();
    }

    public static final Creator<DataPart> CREATOR = new Creator<DataPart>() {
        @Override
        public DataPart createFromParcel(Parcel in) {
            return new DataPart(in);
        }

        @Override
        public DataPart[] newArray(int size) {
            return new DataPart[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(fileName);
        parcel.writeByteArray(content);
        parcel.writeString(type);
    }
}