package byc.avt.avanteelender.viewmodel.tabportofolioviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.model.PortofolioPending;
import byc.avt.avanteelender.repositories.tabportofoliorepositories.PendingPortofolioRepository;

public class PendingPortofolioViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private PendingPortofolioRepository PendingPortofolioRepository;
    private MutableLiveData<JSONObject> resultHeader = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioPending>> resultList = new MutableLiveData<>();

    public PendingPortofolioViewModel(@NonNull Application application) {
        super(application);
        PendingPortofolioRepository = PendingPortofolioRepository.getInstance();
    }

    public void portofolioPendingHeader(String uid, String token){
        resultHeader = PendingPortofolioRepository.portofolioPendingHeader(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultHeader(){
        return resultHeader;
    }

    public void portofolioPendingList(String uid, String token){
        resultList = PendingPortofolioRepository.portofolioPendingList(uid, token, getApplication());
    }

    public LiveData<ArrayList<PortofolioPending>> getResultList(){
        return resultList;
    }
}
