package com.example.osreceiverapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            //"android.intent.action.BOOT_COMPLETED" -> {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "부팅완료", Toast.LENGTH_SHORT).show()
            }


            //"android.provider.Telephony.SMS_RECEIVED" -> {
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> {
                var bundle = intent.extras
                if (bundle != null) {
                    var obj = bundle.get("pdus") as Array<Any>
                    var msgs =
                        obj
                            .map {
                                var sms = SmsMessage.createFromPdu(it as ByteArray)
                                "From: ${sms.originatingAddress}, Message: ${sms.messageBody}"
                            }
                            .toList()

                    var msg = msgs.joinToString()
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
