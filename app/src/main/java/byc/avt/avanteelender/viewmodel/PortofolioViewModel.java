package byc.avt.avanteelender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;
import byc.avt.avanteelender.repositories.PortofolioRepository;

public class PortofolioViewModel extends AndroidViewModel {

    private PortofolioRepository portofolioRepository;
    private MutableLiveData<JSONObject> portClose = new MutableLiveData<>();

    public PortofolioViewModel(@NonNull Application application) {
        super(application);
        portofolioRepository = PortofolioRepository.getInstance();
    }

    public void portofolioClose(String uid, String token){
        portClose = portofolioRepository.portofolioClose(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResult(){
        return portClose;
    }
}