package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DataPart2 implements Parcelable {
    private String fileName;
    private byte[][] content;
    private String type;

    public DataPart2() {
    }

    public DataPart2(String name, byte[][] data) {
        fileName = name;
        content = data;
    }

    public DataPart2(String name, byte[][] data, String mimeType) {
        fileName = name;
        content = data;
        type = mimeType;
    }

    protected DataPart2(Parcel in) {
        fileName = in.readString();
        type = in.readString();
    }

    public static final Creator<DataPart2> CREATOR = new Creator<DataPart2>() {
        @Override
        public DataPart2 createFromParcel(Parcel in) {
            return new DataPart2(in);
        }

        @Override
        public DataPart2[] newArray(int size) {
            return new DataPart2[size];
        }
    };

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[][] getContent() {
        return content;
    }

    public void setContent(byte[][] content) {
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fileName);
        dest.writeString(type);
    }
}