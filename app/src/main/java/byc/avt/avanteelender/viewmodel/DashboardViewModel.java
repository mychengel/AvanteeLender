package byc.avt.avanteelender.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import byc.avt.avanteelender.model.HistoryTrx;
import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;
import byc.avt.avanteelender.repositories.DashboardRepository;

public class DashboardViewModel extends ViewModel {

    private DashboardRepository dashboardRepository;
    private MutableLiveData<ArrayList<HistoryTrx>> resultHistoryTrx = new MutableLiveData<>();

    public DashboardViewModel() {
        dashboardRepository = DashboardRepository.getInstance();
    }

    public void getHistoryTrx(String uid, String token, Context context){
        resultHistoryTrx = dashboardRepository.getHistoryTrx(uid, token, context);
    }

    public LiveData<ArrayList<HistoryTrx>> getResultHistoryTrx(){
        return resultHistoryTrx;
    }
}