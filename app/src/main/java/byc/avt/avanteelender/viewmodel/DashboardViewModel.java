package byc.avt.avanteelender.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;
import byc.avt.avanteelender.repositories.DashboardRepository;

public class DashboardViewModel extends AndroidViewModel {

    private DashboardRepository dashboardRepository;
    private MutableLiveData<ArrayList<HistoryTrx>> resultHistoryTrx = new MutableLiveData<>();

    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashboardRepository = DashboardRepository.getInstance();
    }

    public void getHistoryTrx(String uid, String token){
        resultHistoryTrx = dashboardRepository.getHistoryTrx(uid, token, getApplication());
    }

    public LiveData<ArrayList<HistoryTrx>> getResultHistoryTrx(){
        return resultHistoryTrx;
    }
}