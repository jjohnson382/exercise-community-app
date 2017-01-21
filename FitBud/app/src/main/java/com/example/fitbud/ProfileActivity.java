package com.example.fitbud;

import android.content.Context;
import android.content.pm.PackageManager;
import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity  implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private LocationManager locationManager;
    private Location lastLocation;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private DatabaseReference mDatabase;
    private String androidId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final Button phillip = (Button) findViewById(R.id.bEdit);
        final EditText name = (EditText) findViewById(R.id.editName);
        final EditText eciName = (EditText) findViewById(R.id.eciName);
        final EditText eciPhone = (EditText) findViewById(R.id.eciPhone);
        final EditText avgMile = (EditText) findViewById(R.id.avgMile);
        final EditText avgDis = (EditText) findViewById(R.id.avgDis);
        final EditText ex1 = (EditText) findViewById(R.id.exercise1);
        final EditText ex2 = (EditText) findViewById(R.id.exercise2);
        final EditText ex3 = (EditText) findViewById(R.id.exercise3);
        final ToggleButton active = (ToggleButton) findViewById(R.id.activetoggle);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        final boolean[] editMode = {false};
        toggleEditMode(editMode,name,eciName,eciPhone,avgMile,avgDis,ex1,ex2,ex3);

        active.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    connect();

                    if(mLastLocation != null){
                        mDatabase.child("users").child(androidId).child("lon").setValue(mLastLocation.getLongitude());
                        mDatabase.child("users").child(androidId).child("lat").setValue(mLastLocation.getLatitude());
                        mDatabase.child("users").child(androidId).child("isActive").setValue(true);
                    }
                } else {
                    mDatabase.child("users").child(androidId).child("isActive").setValue(false);

                }
            }
        });






        phillip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editMode[0] = !editMode[0];
                toggleEditMode(editMode,name,eciName,eciPhone,avgMile,avgDis,ex1,ex2,ex3);
                if(editMode[0])
                    phillip.setText("finish");
                else {
                    phillip.setText("edit");
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        });
    }

    public void toggleEditMode(boolean[] editMode, EditText name, EditText eciName, EditText eciPhone,
                               EditText avgMile, EditText avgDis, EditText ex1, EditText ex2, EditText ex3){

        if(editMode[0]) {
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            eciName.setInputType(InputType.TYPE_CLASS_TEXT);
            eciPhone.setInputType(InputType.TYPE_CLASS_TEXT);
            avgMile.setInputType(InputType.TYPE_CLASS_TEXT);
            avgDis.setInputType(InputType.TYPE_CLASS_TEXT);
            ex1.setInputType(InputType.TYPE_CLASS_TEXT);
            ex2.setInputType(InputType.TYPE_CLASS_TEXT);
            ex3.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else {
            name.setInputType(InputType.TYPE_NULL);
            eciName.setInputType(InputType.TYPE_NULL);
            eciPhone.setInputType(InputType.TYPE_NULL);
            avgDis.setInputType(InputType.TYPE_NULL);
            avgMile.setInputType(InputType.TYPE_NULL);
            ex1.setInputType(InputType.TYPE_NULL);
            ex2.setInputType(InputType.TYPE_NULL);
            ex3.setInputType(InputType.TYPE_NULL);

            Map<String, Object> userProfile = new HashMap<String, Object>();
            userProfile.put("name", name.getText().toString());
            userProfile.put("eciName", eciName.getText().toString());
            userProfile.put("eciPhone", eciPhone.getText().toString());
            userProfile.put("avgDis", avgDis.getText().toString());
            userProfile.put("avgMile", avgMile.getText().toString());
            userProfile.put("ex1", ex1.getText().toString());
            userProfile.put("ex2", ex2.getText().toString());
            userProfile.put("ex3", ex3.getText().toString());
            mDatabase.child("users").child(androidId).setValue(userProfile);
        }
    }


    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        connect();

    }

    public void connect() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 99);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        connect();
    }
}
