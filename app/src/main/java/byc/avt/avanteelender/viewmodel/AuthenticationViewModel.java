package byc.avt.avanteelender.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;

public class AuthenticationViewModel extends ViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<String> msg = new MutableLiveData<>();
    private MutableLiveData<String> msg_in = new MutableLiveData<>();
    private MutableLiveData<String> msg_out = new MutableLiveData<>();

    public AuthenticationViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void register(User user, Context context){
        msg = authenticationRepository.registration(user, context);
    }

    public LiveData<String> getResult(){
        return msg;
    }

    public void login(String email, String password, Context context){
        msg_in = authenticationRepository.login(email, password, context);
    }

    public LiveData<String> getLoginResult(){
        return msg_in;
    }

    public void logout(String uid, String token, Context context){
        msg_out = authenticationRepository.logout(uid, token, context);
    }

    public LiveData<String> getLogoutResult(){
        return msg_out;
    }
}
