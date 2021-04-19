package byc.avt.avanteelender.helper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.mukesh.OtpView;

import byc.avt.avanteelender.view.misc.OTPActivity;
import byc.avt.avanteelender.view.misc.OTPDocActivity;

public class OTPReceiver extends BroadcastReceiver {

    private static OtpView otpView;
    private static String from;

    public void setEditText(OtpView editText, String from) {
        OTPReceiver.otpView = editText;
        OTPReceiver.from = from;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (SmsMessage sms : messages) {
            String message = sms.getMessageBody();
            String otp = message.split(": ")[1];
            otpView.setText(otp);
            if(from.equalsIgnoreCase("verification")){
                OTPActivity.btnVerify.setEnabled(true);
            }else if(from.equalsIgnoreCase("document")){
                OTPDocActivity.btnVerify.setEnabled(true);
            }

        }
    }
}
