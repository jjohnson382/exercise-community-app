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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        mDatabase = FirebaseDatabase.getInstance().getReference();

        Map<String, Object> userProfile = new HashMap<String, Object>();
        userProfile.put("name","James");
        userProfile.put("mileTime","00:08:00");
        mDatabase.child("users").child(android_id).setValue(userProfile);

        TextView tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvPhoneNo.setText(android_id);
    }
}