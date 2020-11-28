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

public class LoginViewModel extends AndroidViewModel {
    //private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<String> msg = new MutableLiveData<>();
    private MutableLiveData<String> msg_out = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void login(String email, String password){
        msg = authenticationRepository.login(email, password, getApplication());
    }

    public LiveData<String> getResult(){
        return msg;
    }

    public void logout(String uid, String token){
        msg_out = authenticationRepository.logout(uid, token, getApplication());
    }

    public LiveData<String> getLogoutResult(){
        return msg_out;
    }

}