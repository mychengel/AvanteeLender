package byc.avt.avanteelender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import byc.avt.avanteelender.repositories.MasterDataRepository;

public class MasterDataViewModel extends AndroidViewModel {
    private MasterDataRepository repository;
    private MutableLiveData<JSONObject> resultCivil = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultStatus = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultReligion = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultEducation = new MutableLiveData<>();

    public MasterDataViewModel(@NonNull Application application) {
        super(application);
        repository =MasterDataRepository.getInstance();
    }

    public void getCivil(String uid, String token){
        resultCivil = repository.getCivil(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultCivil(){
        return resultCivil;
    }

    public void getStatus(String uid, String token){
        resultStatus = repository.getStatus(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultStatus(){
        return resultStatus;
    }

    public void getReligion(String uid, String token){
        resultReligion = repository.getReligion(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultReligion(){
        return resultReligion;
    }

    public void getEducation(String uid, String token){
        resultEducation = repository.getEducation(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultEducation(){
        return resultEducation;
    }
}
