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
    private MutableLiveData<JSONObject> resultCountry = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultProvince = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultCity = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultDistrict = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultVillage = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultBank = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultCivil = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultStatus = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultReligion = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultEducation = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultJob = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultJobField = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultJobPosition = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultWorkExperience = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultIncome = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultFundsSource = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultAvgTransaction = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultCompanyType = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultBusinessType = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultGrade = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultPeriodeOfTime = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultInterest = new MutableLiveData<>();

    public MasterDataViewModel(@NonNull Application application) {
        super(application);
        repository =MasterDataRepository.getInstance();
    }

    public void getCountry(String uid, String token){
        resultCountry = repository.getCountry(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultCountry(){
        return resultCountry;
    }

    public void getProvince(String uid, String token){
        resultProvince = repository.getProvince(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultProvince(){
        return resultProvince;
    }

    public void getCity(String uid, String token, String province_id){
        resultCity = repository.getCity(uid, token, province_id, getApplication());
    }

    public LiveData<JSONObject> getResultCity(){
        return resultCity;
    }

    public void getDistrict(String uid, String token, String city_id){
        resultDistrict = repository.getDistrict(uid, token, city_id, getApplication());
    }

    public LiveData<JSONObject> getResultDistrict(){
        return resultDistrict;
    }

    public void getUrban(String uid, String token, String district_id){
        resultVillage = repository.getVillage(uid, token, district_id, getApplication());
    }

    public LiveData<JSONObject> getResultUrban(){
        return resultVillage;
    }

    public void getBank(String uid, String token){
        resultBank = repository.getBank(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultBank(){
        return resultBank;
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

    public void getJob(String uid, String token){
        resultJob = repository.getJob(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultJob(){
        return resultJob;
    }

    public void getJobField(String uid, String token){
        resultJobField = repository.getJobField(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultJobField(){
        return resultJobField;
    }

    public void getJobPosition(String uid, String token){
        resultJobPosition = repository.getJobPosition(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultJobPosition(){
        return resultJobPosition;
    }

    public void getWorkExperience(String uid, String token){
        resultWorkExperience = repository.getWorkExperience(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultWorkExperience(){
        return resultWorkExperience;
    }

    public void getIncome(String uid, String token){
        resultIncome = repository.getIncome(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultIncome(){
        return resultIncome;
    }

    public void getFundsSource(String uid, String token){
        resultFundsSource = repository.getFundsSource(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultFundsSource(){
        return resultFundsSource;
    }

    public void getAvgTransaction(String uid, String token){
        resultAvgTransaction = repository.getAvgTransaction(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultAvgTransaction(){
        return resultAvgTransaction;
    }

    public void getCompanyType(String uid, String token){
        resultCompanyType = repository.getCompanyType(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultCompanyType(){
        return resultCompanyType;
    }

    public void getBusinessType(String uid, String token){
        resultBusinessType = repository.getBusinessType(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultBusinessType(){
        return resultBusinessType;
    }

    public void getGrade(String uid, String token){
        resultGrade = repository.getGrade(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultGrade(){
        return resultGrade;
    }

    public void getPeriodeOfTime(String uid, String token){
        resultPeriodeOfTime = repository.getPeriodeOfTime(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultPeriodeOfTime(){
        return resultPeriodeOfTime;
    }

    public void getInterest(String uid, String token){
        resultInterest = repository.getInterest(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultInterest(){
        return resultInterest;
    }
}
