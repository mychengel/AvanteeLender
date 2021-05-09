package byc.avt.avanteelender.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.model.Pendanaan;
import byc.avt.avanteelender.repositories.PendanaanRepository;
import byc.avt.avanteelender.repositories.SplashRepository;

public class PendanaanViewModel extends AndroidViewModel {
    private PendanaanRepository repository;
    private MutableLiveData<ArrayList<Pendanaan>> resultListPendanaan = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultDetailPendanaan = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultStageFunding = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultNominalFunding = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultAgreementFunding = new MutableLiveData<>();
    private MutableLiveData<String> resultViewKontrakFunding = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultSetujuFunding = new MutableLiveData<>();
    private MutableLiveData<String> resultSignerFunding = new MutableLiveData<>();
    private MutableLiveData<String> resultSignStatus = new MutableLiveData<>();

    public PendanaanViewModel(@NonNull Application application) {
        super(application);
        repository = PendanaanRepository.getInstance();
    }

    public void getStageFunding(String uid, String token, String loan_no){
        resultStageFunding = repository.stageFunding(loan_no, uid, token, getApplication());
    }

    public LiveData<JSONObject> getStageFundingResult(){
        return resultStageFunding;
    }

    public void getNominalFunding(String uid, String token, String loan_no, String type){
        resultNominalFunding = repository.nominalFunding(loan_no, type, uid, token, getApplication());
    }

    public LiveData<JSONObject> getNominalFundingResult(){
        return resultNominalFunding;
    }

    public void getAgreementFunding(String uid, String token, String loan_no, String nom){
        resultAgreementFunding = repository.agreementFunding(loan_no, nom, uid, token, getApplication());
    }

    public LiveData<JSONObject> getAgreementFundingResult(){
        return resultAgreementFunding;
    }

    public void getViewKontrakFunding(String uid, String token, String agreementCode){
        resultViewKontrakFunding= repository.viewKontrakFunding(agreementCode, uid, token, getApplication());
    }

    public LiveData<String> getViewKontrakFundingResult(){
        return resultViewKontrakFunding;
    }

    public void getSetujuFunding(String uid, String token, String agreementCode){
        resultSetujuFunding = repository.setujuFunding(agreementCode, uid, token, getApplication());
    }

    public LiveData<JSONObject> getSetujuFundingResult(){
        return resultSetujuFunding;
    }

    public void getSignerFunding(String uid, String token, String docToken){
        resultSignerFunding = repository.signerFunding(docToken, uid, token, getApplication());
    }

    public LiveData<String> getSignerFundingResult(){
        return resultSignerFunding;
    }

    public void getListPendanaan(String uid, String token){
        resultListPendanaan = repository.getListPendanaan(uid, token, getApplication());
    }

    public LiveData<ArrayList<Pendanaan>> getListPendanaanResult(){
        return resultListPendanaan;
    }

    public void getDetailPendanaan(String uid, String token, String loan_no){
        resultDetailPendanaan = repository.getDetailPendanaan(loan_no, uid, token, getApplication());
    }

    public LiveData<JSONObject> getDetailPendanaanResult(){
        return resultDetailPendanaan;
    }

    public void getSignStatus(String uid, String token, String docToken){
        resultSignStatus = repository.checkSignStatus(docToken, uid, token, getApplication());
    }

    public LiveData<String> getSignStatusResult(){
        return resultSignStatus;
    }

}