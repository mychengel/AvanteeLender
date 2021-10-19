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


    public Spanned getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

}
