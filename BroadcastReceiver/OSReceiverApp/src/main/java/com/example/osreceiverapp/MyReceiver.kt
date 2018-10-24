package com.example.osreceiverapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.telephony.SmsMessage
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast

class MyReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            // 부팅 완료 후
            //"android.intent.action.BOOT_COMPLETED" -> {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "부팅완료", Toast.LENGTH_SHORT).show()
            }

            // 전화 발신
            Intent.ACTION_NEW_OUTGOING_CALL -> {
                val phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                Toast.makeText(context, "Phone Number: ${phoneNumber}", Toast.LENGTH_SHORT).show()
            }


            // 전화 수신
            "android.intent.action.PHONE_STATE" -> {
                val bundle = intent.extras
                bundle.keySet().forEach {
                    Log.d("OSReceiver", "Bundle key : ${it} \n")
                }
                val state = bundle.getString(TelephonyManager.EXTRA_STATE)
                val phoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                Toast.makeText(context, "state: ${state}, Number: ${phoneNumber}", Toast.LENGTH_SHORT).show()
            }

            // 문자 수신
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

            // 화면을 켤 때
            Intent.ACTION_SCREEN_ON -> {
                Toast.makeText(context, "화면 켜짐", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
