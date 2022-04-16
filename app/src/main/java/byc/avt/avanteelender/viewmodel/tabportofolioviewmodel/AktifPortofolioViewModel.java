package byc.avt.avanteelender.viewmodel.tabportofolioviewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import java.util.ArrayList;

import byc.avt.avanteelender.helper.DocumentType;
import byc.avt.avanteelender.model.PortofolioAktif;
import byc.avt.avanteelender.model.PortofolioAktifDetail;
import byc.avt.avanteelender.repositories.tabportofoliorepositories.AktifPortofolioRepository;

public class AktifPortofolioViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    private AktifPortofolioRepository AktifPortofolioRepository;
    private MutableLiveData<JSONObject> resultHeader = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioAktif>> resultList = new MutableLiveData<>();
    private MutableLiveData<ArrayList<PortofolioAktifDetail>> resultListDetail = new MutableLiveData<>();
    private MutableLiveData<String> resultDownloadSuratKuasa = new MutableLiveData<>();
    private MutableLiveData<String> resultDownloadSuratPerjanjian = new MutableLiveData<>();
    private MutableLiveData<String> resultDownloadSuratKuasaLoan = new MutableLiveData<>();
    private MutableLiveData<String> resultDownloadSuratPerjanjianFunding = new MutableLiveData<>();

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

    public void downloadSuratKuasa(String uid, String token){
        resultDownloadSuratKuasa = AktifPortofolioRepository.downloadDocument(uid, token, getApplication(), DocumentType.PROCURATION, "n/a");
    }

    public LiveData<String> getResultDownloadSuratKuasa(){
        return resultDownloadSuratKuasa;
    }

    public void downloadSuratPerjanjian(String uid, String token){
        resultDownloadSuratPerjanjian = AktifPortofolioRepository.downloadDocument(uid, token, getApplication(), DocumentType.AGREEMENT, "n/a");
    }

    public LiveData<String> getResultDownloadSuratPerjanjian(){
        return resultDownloadSuratPerjanjian;
    }

    public void downloadSuratKuasaLoan(String uid, String token, String loan_no){
        resultDownloadSuratKuasaLoan = AktifPortofolioRepository.downloadDocument(uid, token, getApplication(), DocumentType.PROCURATION_LOAN, loan_no);
    }

    public LiveData<String> getResultDownloadSuratKuasaLoan(){
        return resultDownloadSuratKuasaLoan;
    }

    public void downloadAgreementFunding(String uid, String token, String funding_id){
        resultDownloadSuratPerjanjianFunding = AktifPortofolioRepository.downloadDocument(uid, token, getApplication(), DocumentType.AGREEMENT_LOAN, funding_id);
    }

    public LiveData<String> getResultDownloadAgreementFunding(){
        return resultDownloadSuratPerjanjianFunding;
    }
}
