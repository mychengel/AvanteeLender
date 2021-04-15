package byc.avt.avanteelender.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Blog implements Parcelable {

    String img;
    String title;
    String slug;
    String text;
    String category;
    String created_date;
    String created_by;

    public Blog(){}

    public Blog(String img, String title, String slug, String text, String category, String created_date, String created_by) {
        this.img = img;
        this.title = title;
        this.slug = slug;
        this.text = text;
        this.category = category;
        this.created_date = created_date;
        this.created_by = created_by;
    }

    protected Blog(Parcel in) {
        img = in.readString();
        title = in.readString();
        slug = in.readString();
        text = in.readString();
        category = in.readString();
        created_date = in.readString();
        created_by = in.readString();
    }

    public static final Creator<Blog> CREATOR = new Creator<Blog>() {
        @Override
        public Blog createFromParcel(Parcel in) {
            return new Blog(in);
        }

        @Override
        public Blog[] newArray(int size) {
            return new Blog[size];
        }
    };

    public String getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public String getText() {
        return text;
    }

    public String getCategory() {
        return category;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getCreated_by() {
        return created_by;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(img);
        parcel.writeString(title);
        parcel.writeString(slug);
        parcel.writeString(text);
        parcel.writeString(category);
        parcel.writeString(created_date);
        parcel.writeString(created_by);
    }
}
