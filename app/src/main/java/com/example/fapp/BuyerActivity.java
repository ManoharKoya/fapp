package com.example.fapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuyerActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Sub_button();
    }

    String objTp,UserLocation;
    String Perc_Off, Amt_off,buy_x,get_y;
    int percentageOff,AmountOff,Buyx,Gety;
    int Location_PERMISSION_CODE;
    BuyerActivity buyerOb = null;

    private FusedLocationProviderClient client; // To find location of user and store it.


    private void Sub_button(){
        Button Sub = (Button) findViewById(R.id.sub_but);
        Sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   // reff = FirebaseDatabase.getInstance().getReference().child("Buyer");
                    buyerOb = new BuyerActivity();
                    buyerOb.Location_PERMISSION_CODE=1;
                    final EditText obT = (EditText) findViewById(R.id.obj);
                    buyerOb.objTp = obT.getText().toString();

                    final EditText perOf = (EditText) findViewById(R.id.perc);
                    buyerOb.Perc_Off = perOf.getText().toString();

                    try{
                        if(buyerOb.Perc_Off=="") buyerOb.percentageOff=0;
                        else buyerOb.percentageOff = Integer.parseInt(buyerOb.Perc_Off);
                    }  catch(NumberFormatException e){
                        System.out.println(e);
                    }

                final EditText Amt = (EditText) findViewById(R.id.amt);
                buyerOb.Amt_off = Amt.getText().toString();
                    try{
                        if(buyerOb.Amt_off==""){buyerOb.AmountOff=0;}
                        else buyerOb.AmountOff = Integer.parseInt(buyerOb.Amt_off);
                    } catch(NumberFormatException e){
                        System.out.println(e);
                    }


                    final EditText buyX = (EditText) findViewById(R.id.buy);
                    final EditText getY = (EditText) findViewById(R.id.get);
                    buyerOb.buy_x = buyX.getText().toString();
                    buyerOb.get_y = getY.getText().toString();
                try {
                    if(buyerOb.buy_x==""){buyerOb.Buyx=0; buyerOb.Gety=0;}
                    else { buyerOb.Buyx = Integer.parseInt(buyerOb.buy_x); }
                    } catch(NumberFormatException e){
                    System.out.println(e);
                    }
                try {
                    if(buyerOb.get_y=="") {buyerOb.Buyx=0; buyerOb.Gety=0;}
                    else buyerOb.Gety = Integer.parseInt(buyerOb.get_y);
                } catch (NumberFormatException e) {
                    System.out.println(e);
                }

                if(buyerOb.Buyx==0 || buyerOb.Gety==0) {
                    buyerOb.Buyx=0; buyerOb.Gety=0;
                }

                System.out.println("Perc_Off: "+ buyerOb.percentageOff +" AmtOff: "+buyerOb.AmountOff);
                System.out.println(buyerOb.Gety);
                System.out.println(buyerOb.Buyx);

                /*  NumberFormatException is handled
                    at-least one number field should be filled.
                * */

                if(buyerOb.percentageOff==0 && buyerOb.AmountOff==0 && (buyerOb.Gety==0 || buyerOb.Buyx==0)){
                    // should popup an Alert message ' at-least fill one requirement to proceed '
                    AlertDialog.Builder builder = new AlertDialog.Builder(BuyerActivity.this);

                    builder.setCancelable(true);
                    builder.setTitle("Please");
                    builder.setMessage("Provide at-least one buyer requirement");

                    builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                }
                else {
                    // LOCATION PERMISSION.
                    if(ContextCompat.checkSelfPermission(BuyerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){
                        // Access location and store in the object ;
                        client = LocationServices.getFusedLocationProviderClient(BuyerActivity.this);
                        client.getLastLocation().addOnSuccessListener(BuyerActivity.this,
                                    new OnSuccessListener<Location>() {
                                        @Override
                                        public void onSuccess(Location location) {
                                            if(location!=null){
                                                // STORE LOCATION IN object
                                                buyerOb.UserLocation = location.toString();
                                                System.out.println("KOYA KOYA KOYA : " + location.toString());
                                            }
                                        }
                                    });


                        //Start Activity.
                        startActivity(new Intent(BuyerActivity.this, CheckActivity.class));
                    } else {
                        // request permission.
                        ActivityCompat.requestPermissions(BuyerActivity.this,
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                buyerOb.Location_PERMISSION_CODE);
                        // if accepted then store in object. -> all these are done at onRequestPermissionResult method
                        // if denied -> alert message location access is needed to proceed.
                        // implemented at
                    }
                }
                // Should upload data to database as objects.
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==buyerOb.Location_PERMISSION_CODE){
            if(grantResults.length>0 &&
                    grantResults[0]==PackageManager.PERMISSION_GRANTED ){
                // Access location and store in the object ;
                try{
                    client = LocationServices.getFusedLocationProviderClient(BuyerActivity.this);

                    client.getLastLocation().addOnSuccessListener(BuyerActivity.this,
                            new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if(location!=null){
                                        // STORE LOCATION IN object
                                        buyerOb.UserLocation = location.toString();
                                        System.out.println("'ManoharKoya' THis is the location of user : " + buyerOb.UserLocation);
                                    }
                                }
                            });
                } catch(SecurityException e){System.out.println(e);}


                // Starting CheckActivity on when permission is granted.
                startActivity(new Intent(BuyerActivity.this, CheckActivity.class));
                //buyerOb.cnt=1;
                Toast.makeText(BuyerActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(BuyerActivity.this,"Location Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }


}
