package byc.avt.avanteelender.viewmodel.tabportofolioviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.model.PortofolioSelesai;
import byc.avt.avanteelender.repositories.PortofolioRepository;
import byc.avt.avanteelender.repositories.tabportofoliorepositories.SelesaiPortofolioRepository;

public class SelesaiPortofolioViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private SelesaiPortofolioRepository selesaiPortofolioRepository;
    private MutableLiveData<JSONObject> resultHeader = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioSelesai>> resultList = new MutableLiveData<>();

    public SelesaiPortofolioViewModel(@NonNull Application application) {
        super(application);
        selesaiPortofolioRepository = SelesaiPortofolioRepository.getInstance();
    }

    public void portofolioCloseHeader(String uid, String token){
        resultHeader = selesaiPortofolioRepository.portofolioCloseHeader(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultHeader(){
        return resultHeader;
    }

    public void portofolioCloseList(String uid, String token){
        resultList = selesaiPortofolioRepository.portofolioCloseList(uid, token, getApplication());
    }

    public LiveData<ArrayList<PortofolioSelesai>> getResultList(){
        return resultList;
    }
}
