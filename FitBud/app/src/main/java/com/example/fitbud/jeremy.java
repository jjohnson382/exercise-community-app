package com.example.fitbud;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.inputmethod.InputMethodManager;import com.example.tedvlady.app1.R;

public class jeremy extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jeremy);

        final Button phillip = (Button) findViewById(R.id.button2);
        final EditText name = (EditText) findViewById(R.id.editname);
        final EditText emCI = (EditText) findViewById(R.id.editECI);
        final EditText avgMile = (EditText) findViewById(R.id.avgMile);
        final EditText avgDis = (EditText) findViewById(R.id.avgDis);
        final EditText ex1 = (EditText) findViewById(R.id.exercise1);
        final EditText ex2 = (EditText) findViewById(R.id.exercise2);
        final EditText ex3 = (EditText) findViewById(R.id.exercise3);




        final boolean[] editMode = {false};
        toggleEditMode(editMode,name,emCI,avgMile,avgDis,ex1,ex2,ex3);



        phillip.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editMode[0] = !editMode[0];
                toggleEditMode(editMode,name,emCI,avgMile,avgDis,ex1,ex2,ex3);
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

    public void toggleEditMode(boolean[] editMode, EditText name,EditText emCI, EditText avgMile, EditText avgDis, EditText ex1, EditText ex2, EditText ex3){

        if(editMode[0]) {
            name.setInputType(InputType.TYPE_CLASS_TEXT);
            emCI.setInputType(InputType.TYPE_CLASS_TEXT);
            avgMile.setInputType(InputType.TYPE_CLASS_TEXT);
            avgDis.setInputType(InputType.TYPE_CLASS_TEXT);
            ex1.setInputType(InputType.TYPE_CLASS_TEXT);
            ex2.setInputType(InputType.TYPE_CLASS_TEXT);
            ex3.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else {
            name.setInputType(InputType.TYPE_NULL);
            emCI.setInputType(InputType.TYPE_NULL);
            avgDis.setInputType(InputType.TYPE_NULL);
            avgMile.setInputType(InputType.TYPE_NULL);
            ex1.setInputType(InputType.TYPE_NULL);
            ex2.setInputType(InputType.TYPE_NULL);
            ex3.setInputType(InputType.TYPE_NULL);
        }
    }
}
