package byc.avt.avanteelender.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;
import byc.avt.avanteelender.repositories.SplashRepository;

public class SplashViewModel extends ViewModel {
    private SplashRepository splashRepository;
    private MutableLiveData<String> msg = new MutableLiveData<>();
    private MutableLiveData<String> msgx = new MutableLiveData<>();


    public SplashViewModel() {
       splashRepository = SplashRepository.getInstance();
    }

    public void hanyaCheck(String uid, String token, Context context){
        msgx = splashRepository.hanyaCheck(uid, token, context);
    }

    public LiveData<String> getResultHanya(){
        return msgx;
    }

    public void sessionCheck(String uid, String token, Context context){
        msg = splashRepository.sessionCheck(uid, token, context);
    }

    public LiveData<String> getResult(){
        return msg;
    }

}