package com.example.fitbud;

import android.app.Activity;
import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> userProfile = new HashMap<String, Object>();
        userProfile.put("name","James");
        userProfile.put("mileTime","00:08:00");
        mDatabase.child("users").child(androidId).setValue(userProfile);

        TextView tvAndroidId = (TextView) findViewById(R.id.tvAndroidId);
        tvAndroidId.setText(androidId);
    }
}