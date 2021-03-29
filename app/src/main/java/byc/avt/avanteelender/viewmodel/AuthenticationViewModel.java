package byc.avt.avanteelender.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;

public class AuthenticationViewModel extends AndroidViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<JSONObject> msg = new MutableLiveData<>();
    private MutableLiveData<String> msg_in = new MutableLiveData<>();
    private MutableLiveData<String> msg_out = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultSettingData = new MutableLiveData<>();

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void register(User user){
        msg = authenticationRepository.registration(user, getApplication());
    }

    public LiveData<JSONObject> getResult(){
        return msg;
    }

    public void login(String email, String password){
        msg_in = authenticationRepository.login(email, password, getApplication());
    }

    public LiveData<String> getLoginResult(){
        return msg_in;
    }

    public void logout(String uid, String token){
        msg_out = authenticationRepository.logout(uid, token, getApplication());
    }

    public LiveData<String> getLogoutResult(){
        return msg_out;
    }

    public void getSettingData(String uid, String token){
        resultSettingData = authenticationRepository.getSettingData(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultSettingData(){
        return resultSettingData;
    }
}
