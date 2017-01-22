package com.example.fitbud;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NearbyActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2, double el1, double el2) //converts lat/long to Km
    {

        final int R = 6371; // Radius of the earth

        Double latDistance = Math.toRadians(lat2 - lat1);
        Double lonDistance = Math.toRadians(lon2 - lon1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearby);

        final String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);


        final TextView barron = (TextView) findViewById(R.id.infobox);
        final Button gtNearby = (Button) findViewById(R.id.gtProfile);
        gtNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference users =mDatabase.child("users");

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot me = null;
                String s="";
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    if(user.getKey().equals(androidId)){
                        me=user;
                    }
                }
                for(DataSnapshot user : dataSnapshot.getChildren()){
                    if(user.getKey().equals(androidId)){
                        me=user;
                    }
                    else{
                        double d = 100;
                        if(me != null)
                            if (!(user.child("lat").getValue() == null || user.child("lon").getValue() == null || me.child("lat").getValue() == null || me.child("lon").getValue() == null)){
                                d = distance(Double.parseDouble(user.child("lat").getValue().toString()),Double.parseDouble(me.child("lat").getValue().toString()),Double.parseDouble(user.child("lon").getValue().toString()),Double.parseDouble(me.child("lon").getValue().toString()),0,0);
                                d=d/1000.0;
                                d=Math.round(d*10)/10.0;
                            } else {
                                System.out.println(user.child("lat").getValue() + ";" + Double.parseDouble(user.child("lat").getValue().toString()));
                                System.out.println(me.child("lat").getValue() + ";" + Double.parseDouble(me.child("lat").getValue().toString()));

                            }

                        double compatibility=0.0;

                        if(!(user.child("avgMile").getValue() == null || user.child("avgDis").getValue()==null || me.child("avgMile").getValue() == null || me.child("avgDis").getValue() == null)){
                            compatibility=compatibility+(Math.min(Double.parseDouble(user.child("avgDis").getValue().toString()),Double.parseDouble(me.child("avgDis").getValue().toString()))/Math.max(Double.parseDouble(user.child("avgDis").getValue().toString()),Double.parseDouble(me.child("avgDis").getValue().toString())))*50;
                            compatibility=compatibility+Math.max(25-d,0);


                            String time = (String) user.child("avgMile").getValue();
                            String[] units = time.split(":"); //will break the string up into an array
                            int minutes = Integer.parseInt(units[0]); //first element
                            int seconds = Integer.parseInt(units[1]); //second element
                            double userduration = 60.0 * minutes + seconds; //add up our values

                            String time1 = (String) me.child("avgMile").getValue();
                            String[] units1 = time.split(":"); //will break the string up into an array
                            int minutes1 = Integer.parseInt(units[0]); //first element
                            int seconds1 = Integer.parseInt(units[1]); //second element
                            double meduration = 60.0 * minutes + seconds; //add up our values
                            compatibility=compatibility+Math.min(meduration,userduration)/Math.max(meduration,userduration)*25;
                            compatibility=Math.round(compatibility*10)/10;
                        }









                        s+=user.child("name").getValue() + "\n"
                                + "Average Dist. \t" + user.child("avgDis").getValue() + "mi.\n"
                                + "Average Mile Time \t" + user.child("avgMile").getValue() + "min. \n"
                                + "Phone Number \t" +  user.child("eciName").getValue() +"\n"
                                +  "Distance \t" + d + "km\n"
                                +  "Compatibility \t" + compatibility + "/100\n\n";
                    }


                }

                barron.setText(s);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }
}
