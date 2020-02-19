package byc.avt.avanteelender.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import byc.avt.avanteelender.model.User;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<Boolean> isSuccess = new MutableLiveData<>();

    public void login(User user, String ref_id){
        isSuccess.setValue(true);
    }

    public LiveData<Boolean> getStatus(){
        return isSuccess;
    }
}