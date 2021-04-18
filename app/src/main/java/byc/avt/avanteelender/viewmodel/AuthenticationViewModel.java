package byc.avt.avanteelender.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.json.JSONObject;

import byc.avt.avanteelender.model.User;
import byc.avt.avanteelender.repositories.AuthenticationRepository;

public class AuthenticationViewModel extends AndroidViewModel {
    private AuthenticationRepository authenticationRepository;
    private MutableLiveData<JSONObject> resultAccountData = new MutableLiveData<>();
    private MutableLiveData<JSONObject> msg = new MutableLiveData<>();
    private MutableLiveData<String> msg_in = new MutableLiveData<>();
    private MutableLiveData<String> msg_out = new MutableLiveData<>();
    private MutableLiveData<String> msg_otp_ver = new MutableLiveData<>();
    private MutableLiveData<String> msg_ver_otp = new MutableLiveData<>();
    private MutableLiveData<String> msg_ver_otp_doc = new MutableLiveData<>();
    private MutableLiveData<String> msg_check_surat_kuasa = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultSettingData = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultSettingDataNoAuth = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultResetPassword = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultSetNewPassword = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultResendEmail = new MutableLiveData<>();
    private MutableLiveData<JSONObject> resultCreatePersonalDoc = new MutableLiveData<>();

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        authenticationRepository = AuthenticationRepository.getInstance();
    }

    public void checkSuratKuasa(String uid, String token){
        msg_check_surat_kuasa = authenticationRepository.cekSuratKuasa2(uid, token, getApplication());
    }

    public LiveData<String> getCheckSuratKuasaResult(){
        return msg_check_surat_kuasa;
    }

    public void getAccountData(String uid, String token){
        resultAccountData = authenticationRepository.getAccountData(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultAccountData(){
        return resultAccountData;
    }

    public void register(User user){
        msg = authenticationRepository.registration(user, getApplication());
    }

    public LiveData<JSONObject> getResult(){
        return msg;
    }

    public void login(String email, String password){
        msg_in = authenticationRepository.login(email, password, getApplication());
    }

    public LiveData<String> getLoginResult(){
        return msg_in;
    }

    public void logout(String uid, String token){
        msg_out = authenticationRepository.logout(uid, token, getApplication());
    }

    public LiveData<String> getLogoutResult(){
        return msg_out;
    }

    public void sendOTPVerification(String uid, String token){
        msg_otp_ver = authenticationRepository.sendOTPVerification(uid, token, getApplication());
    }

    public LiveData<String> getOTPVerificationResult(){
        return msg_otp_ver;
    }

    public void verifyOTP(String uid, String token, String otpCode){
        msg_ver_otp = authenticationRepository.verifyOTP(uid, token, otpCode, getApplication());
    }

    public LiveData<String> getVerifyOTPResult(){
        return msg_ver_otp;
    }

    public void verifyOTPDoc(String uid, String token, String otpCode){
        msg_ver_otp_doc = authenticationRepository.verifyOTPDoc(uid, token, otpCode, getApplication());
    }

    public LiveData<String> getVerifyOTPDocResult(){
        return msg_ver_otp_doc;
    }

    public void getSettingData(String uid, String token){
        resultSettingData = authenticationRepository.getSettingData(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultSettingData(){
        return resultSettingData;
    }

    public void resetPassword(String email){
        resultResetPassword = authenticationRepository.resetPassword(email, getApplication());
    }

    public LiveData<JSONObject> getResultResetPassword(){
        return resultResetPassword;
    }

    public void setNewPassword(String newPass, String key){
        resultSetNewPassword = authenticationRepository.setNewPassword(newPass, key, getApplication());
    }

    public LiveData<JSONObject> getResultSetNewPassword(){
        return resultSetNewPassword;
    }

    public void setResendEmail(String email){
        resultResendEmail = authenticationRepository.resendEmailVerification(email, getApplication());
    }

    public LiveData<JSONObject> getResultResendEmail(){
        return resultResendEmail;
    }

    public void getSettingDataNoAuth(){
        resultSettingDataNoAuth = authenticationRepository.getSettingDataNoAuth(getApplication());
    }

    public LiveData<JSONObject> getResultSettingDataNoAuth(){
        return resultSettingDataNoAuth;
    }

    public void createPersonalDoc(String uid, String token){
        resultCreatePersonalDoc = authenticationRepository.createPersonalDocument(uid, token, getApplication());
    }

    public LiveData<JSONObject> getResultCreatePersonalDoc(){
        return resultCreatePersonalDoc;
    }
}
