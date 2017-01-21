package com.example.fitbud;

import android.content.Context;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
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

        final boolean[] editMode = {false};
        toggleEditMode(editMode,name,eciName,eciPhone,avgMile,avgDis,ex1,ex2,ex3);

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
}
