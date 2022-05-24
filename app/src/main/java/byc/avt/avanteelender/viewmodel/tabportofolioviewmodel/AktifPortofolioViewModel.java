package byc.avt.avanteelender.viewmodel.tabportofolioviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.model.PortofolioAktifDetail;
import byc.avt.avanteelender.repositories.tabportofoliorepositories.AktifPortofolioRepository;

public class AktifPortofolioViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AktifPortofolioRepository AktifPortofolioRepository;
    private MutableLiveData<JSONObject> resultHeader = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioAktif>> resultList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioAktifDetail>> resultListDetail = new MutableLiveData<>();

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

    public void portofolioAktifDetailList(String uid, String token, String loan_no, String funding_id){
        resultListDetail = AktifPortofolioRepository.getPortAktifDetList(uid, token, getApplication(), loan_no, funding_id);
    }

    public LiveData<ArrayList<PortofolioAktifDetail>> getResultListDetail(){
        return resultListDetail;
    }

    public void portofolioAktifList(String uid, String token, String page){
        resultList = AktifPortofolioRepository.portofolioAktifList(page, uid, token, getApplication());
    }

    public LiveData<ArrayList<PortofolioAktif>> getResultList(){
        return resultList;
    }
}
