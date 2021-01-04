package byc.avt.avanteelender.viewmodel.tabportofolioviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.repositories.tabportofoliorepositories.AktifPortofolioRepository;

public class AktifPortofolioViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AktifPortofolioRepository AktifPortofolioRepository;
    private MutableLiveData<JSONObject> resultHeader = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioAktif>> resultList = new MutableLiveData<>();

    public AktifPortofolioViewModel(@NonNull Application application) {
        super(application);
        AktifPortofolioRepository = AktifPortofolioRepository.getInstance();
    }

    public void portofolioAktifHeader(String uid, String token){
        resultHeader = AktifPortofolioRepository.portofolioAktifHeader(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultHeader(){
        return resultHeader;
    }

    public void portofolioAktifList(String uid, String token){
        resultList = AktifPortofolioRepository.portofolioAktifList(uid, token, getApplication());
    }

    public LiveData<ArrayList<PortofolioAktif>> getResultList(){
        return resultList;
    }
}
