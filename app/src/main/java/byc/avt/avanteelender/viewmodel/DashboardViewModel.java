package byc.avt.avanteelender.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.model.Header;
import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;
import byc.avt.avanteelender.repositories.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {

    private DashboardRepository dashboardRepository;
    private MutableLiveData<ArrayList<Header>> resultHeader = new MutableLiveData<>();
    private MutableLiveData<ArrayList<HistoryTrx>> resultHistoryTrx = new MutableLiveData<>();
    private MutableLiveData<ArrayList<HistoryTrx>> resultHistoryTrxList = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultDashboard = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultWallet = new MutableLiveData<>();
    private MutableLiveData<String> totActivePort = new MutableLiveData<>();
    private MutableLiveData<String> totPendingPort = new MutableLiveData<>();
    private MutableLiveData<String> resultTopupInstruction = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepository = DashboardRepository.getInstance();
    }

    public void getWallet(String uid, String token){
        resultWallet = dashboardRepository.getWallet(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultWallet(){
        return resultWallet;
    }

    public void getHeader(String uid, String token){
        resultHeader = dashboardRepository.getHeader(uid, token, getApplication());
    }

    public LiveData<ArrayList<Header>> getResultHeader(){
        return resultHeader;
    }

    public void getHistoryTrx(String uid, String token){
        resultHistoryTrx = dashboardRepository.getHistoryTrx(uid, token, getApplication());
    }

    public LiveData<ArrayList<HistoryTrx>> getResultHistoryTrx(){
        return resultHistoryTrx;
    }

    public void getHistoryTrxList(String uid, String token){
        resultHistoryTrxList = dashboardRepository.getHistoryTrxList(uid, token, getApplication());
    }

    public LiveData<ArrayList<HistoryTrx>> getResultHistoryTrxList(){
        return resultHistoryTrxList;
    }

    public void getDashboard(String uid, String token){
        resultDashboard = dashboardRepository.getDashboard(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultDashboard(){
        return resultDashboard;
    }

    public void getTotActivePort(String uid, String token){
        totActivePort = dashboardRepository.getTotActivePort(uid, token, getApplication());
    }

    public LiveData<String> getResultTotActivePort(){
        return totActivePort;
    }

    public void getTotPendingPort(String uid, String token){
        totPendingPort = dashboardRepository.getTotPendingPort(uid, token, getApplication());
    }

    public LiveData<String> getResultTotPendingPort(){
        return totPendingPort;
    }

    public void getTopupInstruction(String uid, String token){
        resultTopupInstruction = dashboardRepository.getTopupInstruction(uid, token, getApplication());
    }

    public LiveData<String> getResultTopupInstruction(){
        return resultTopupInstruction;
    }


}