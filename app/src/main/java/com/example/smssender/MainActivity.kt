package com.example.smssender

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.lang.Exception

const val CODE = 1001

class MainActivity : AppCompatActivity() {


    private lateinit var phoneNumber : EditText
    private lateinit var smsText : EditText
    private lateinit var sendBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

    }
    private fun initViews(){
        phoneNumber = findViewById(R.id.et_phoneNumber)
        smsText = findViewById(R.id.et_textSMS)
        sendBtn = findViewById(R.id.btn_send)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions()
        }

        sendBtn.setOnClickListener(View.OnClickListener {
            sendSms(phoneNumber.text.toString(), smsText.text.toString())
        })

    }

    private fun sendSms(phone : String, text: String){
        try {
            val smsManager:SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phone, null,text,null,null)
            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
        }catch (e : Exception){
            Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun hasCameraPermission() =
        ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationPermission() =
        ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun hasSmsReceivePermission() =
        ActivityCompat.checkSelfPermission(this,Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED

    private fun hasSmsSendPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(){

        var permissionsToRequest = mutableListOf<String>()

        if (!hasCameraPermission()){
            permissionsToRequest.add(Manifest.permission.CAMERA)
        }
        if (!hasLocationPermission()){
            permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (!hasSmsReceivePermission()){
            permissionsToRequest.add(Manifest.permission.RECEIVE_SMS)
        }
        if(!hasSmsSendPermission()){
            permissionsToRequest.add(Manifest.permission.SEND_SMS)
        }

        if (permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this,permissionsToRequest.toTypedArray(), CODE)
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODE && grantResults.isNotEmpty()){
            for (i in grantResults.indices){
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this, "${permissions[i]} granted", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}