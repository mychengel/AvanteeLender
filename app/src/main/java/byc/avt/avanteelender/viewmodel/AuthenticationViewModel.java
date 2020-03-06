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

    public AuthenticationViewModel() {
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void register(User user, Context context){
        msg = authenticationRepository.registration(user, context);
    }

    public LiveData<String> getResult(){
        return msg;
    }
}
