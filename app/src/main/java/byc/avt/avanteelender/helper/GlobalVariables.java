package byc.avt.avanteelender.helper;

import android.util.Base64;

import java.util.HashMap;
import java.util.Map;

public final class GlobalVariables {

    public static final String BASE_URL = "https://avantee.co.id:8444/baycode_api/";
    public static final String IMG_URL = "https://avantee.co.id:8444/assets/images/";

    public static final Map<String, String> API_ACCESS(){
        final String basicAuth = "Basic " + Base64.encodeToString("Av@nTe3:p@ssw0rdyangsimpleajalahyah".getBytes(), Base64.NO_WRAP);
        Map<String, String> header = new HashMap<>();
        header.put("Content-Type: ", "application/json");
        header.put("Accept: ", "application/json");
        header.put("Authorization", basicAuth);
        header.put("X-API-KEY", "G2HN@D4N483RS@MA");
        return header;
    }

}
