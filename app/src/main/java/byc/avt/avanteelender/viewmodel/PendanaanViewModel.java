package byc.avt.avanteelender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import byc.avt.avanteelender.model.Pendanaan;
import byc.avt.avanteelender.repositories.PendanaanRepository;
import byc.avt.avanteelender.repositories.SplashRepository;

public class PendanaanViewModel extends AndroidViewModel {
    private PendanaanRepository repository;
    private MutableLiveData<ArrayList<Pendanaan>> result = new MutableLiveData<>();

    public PendanaanViewModel(@NonNull Application application) {
        super(application);
        repository = PendanaanRepository.getInstance();
    }

    public void getListPendanaan(String uid, String token){
        result = repository.getListPendanaan(uid, token, getApplication());
    }

    public LiveData<ArrayList<Pendanaan>> getListPendanaanResult(){
        return result;
    }

}