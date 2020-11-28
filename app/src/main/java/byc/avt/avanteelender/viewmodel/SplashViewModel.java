package byc.avt.avanteelender.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;
import byc.avt.avanteelender.repositories.SplashRepository;

public class SplashViewModel extends AndroidViewModel {
    private SplashRepository splashRepository;
    private MutableLiveData<String> msg = new MutableLiveData<>();
    private MutableLiveData<String> msgx = new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application) {
        super(application);
        splashRepository = SplashRepository.getInstance();
    }

    public void hanyaCheck(String uid, String token){
        msgx = splashRepository.hanyaCheck(uid, token, getApplication());
    }

    public LiveData<String> getResultHanya(){
        return msgx;
    }

    public void sessionCheck(String uid, String token){
        msg = splashRepository.sessionCheck(uid, token, getApplication());
    }

    public LiveData<String> getResult(){
        return msg;
    }

}