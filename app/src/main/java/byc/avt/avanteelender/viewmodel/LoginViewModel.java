package byc.avt.avanteelender.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<String> msg = new MutableLiveData<>();

    public LoginViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void login(String email, String password, Context context){
        msg = authenticationRepository.login(email, password, context);
    }

    public LiveData<String> getResult(){
        return msg;
    }
}