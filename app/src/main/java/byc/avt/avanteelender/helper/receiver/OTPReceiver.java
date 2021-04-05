package byc.avt.avanteelender.helper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.mukesh.OtpView;

import byc.avt.avanteelender.view.misc.OTPActivity;

public class OTPReceiver extends BroadcastReceiver {

    private static OtpView otpView;

    public void setEditText(OtpView editText) {
        OTPReceiver.otpView = editText;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);

        for (SmsMessage sms : messages) {
            String message = sms.getMessageBody();
            String otp = message.split(": ")[1];
            otpView.setText(otp);
            OTPActivity.btnVerify.setEnabled(true);
        }
    }
}
