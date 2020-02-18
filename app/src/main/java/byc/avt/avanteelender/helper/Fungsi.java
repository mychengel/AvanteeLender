package byc.avt.avanteelender.helper;

import android.content.Context;
import android.widget.Toast;

public class Fungsi {

    Context ctx;

    public Fungsi(Context ctx){
        this.ctx = ctx;
    }

    public void showMessage(String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }

}
