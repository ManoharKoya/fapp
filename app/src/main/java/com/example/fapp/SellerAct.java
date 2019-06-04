package com.example.fapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Button;
import android.Manifest;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;


public class SellerAct extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String [] SellerType = {"department store","supermarket","grocer","greengrocer","butcher","baker",
                                "fishmonger","chemist"};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    //Getting the instance of Spinner and applying OnItemSelectedListener on it
        Spinner spin = (Spinner) findViewById(R.id.outlet_spinner);
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,SellerType);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        SellerSubmit();
    }

    // SellerAct object should store all the offer details
    String OutletName,StoreType,ItemType,OfferDesc,UserLocation;
    //static String UserLocation;
    String percent_Off,Amount_off,buyx,gety;
    int PercentOff,AmountOff;
    int BuyX,GetY;
    boolean flatOff;
    int Location_PERMISSION_CODE =1,cnt=0;

    SellerAct Sob = null;

    private FusedLocationProviderClient client;

    private void SellerSubmit(){
        Button S_submit = findViewById(R.id.Sub_button);
        S_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Sob = new SellerAct();
                 Sob.Location_PERMISSION_CODE=1; Sob.cnt=0;

                final EditText otName = (EditText) findViewById(R.id.OutletName);
                final Spinner otType = (Spinner) findViewById(R.id.outlet_spinner);
                final AutoCompleteTextView ItemTp = (AutoCompleteTextView) findViewById(R.id.item);
                final EditText Offer_Desc = (EditText) findViewById(R.id.desc);
                final EditText AmtOff = (EditText) findViewById(R.id.amtOff);
                final EditText percent = (EditText) findViewById(R.id.percent);
                final EditText buy =  (EditText) findViewById(R.id.buy);
                final EditText get = (EditText) findViewById(R.id.get);
                final Switch flat = (Switch) findViewById(R.id.Flat);



                    Sob.OutletName = otName.getText().toString();
                    Sob.StoreType = otType.getSelectedItem().toString();
                    Sob.ItemType = ItemTp.getText().toString();
                    Sob.OfferDesc = Offer_Desc.getText().toString();

                    try {
                        Sob.percent_Off = percent.getText().toString();
                        System.out.println("hi "+ Sob.percent_Off +" this is percentage");
                        if (Sob.percent_Off == "") {Sob.PercentOff = 0; }
                        else {Sob.PercentOff = Integer.parseInt(Sob.percent_Off);}
                    } catch(NumberFormatException e) {
                        System.out.println(e);
                    }
                    try {
                        Sob.Amount_off = AmtOff.getText().toString();
                        System.out.println("This is Amount "+ Sob.Amount_off+" hi");
                        if(Sob.Amount_off=="") {Sob.AmountOff=0;}
                        else {Sob.AmountOff = Integer.parseInt(Sob.Amount_off);}
                    }
                    catch(NumberFormatException e) {
                        System.out.println(e);
                    }

                Sob.buyx = buy.getText().toString();
                Sob.gety = get.getText().toString();

                try {
                    if(Sob.buyx==""){Sob.BuyX=0; Sob.GetY=0;}
                    else { Sob.BuyX = Integer.parseInt(Sob.buyx); }
                }
                catch(NumberFormatException e) {
                    System.out.println(e);
                }

                try {
                    if(Sob.gety==""){Sob.BuyX=0; Sob.GetY=0;}
                    else {Sob.GetY = Integer.parseInt(Sob.gety);}
                }
                catch(NumberFormatException e) {
                    System.out.println(e);
                }

                flatOff = flat.isChecked();

                System.out.println(Sob.PercentOff+" "+Sob.AmountOff+" "+Sob.BuyX+" "+Sob.GetY);
                //Alert dialogue.
                if(Sob.PercentOff==0 && Sob.AmountOff==0 && (Sob.BuyX==0 || Sob.GetY==0)){
                    // should popup an Alert message ' at-least fill one requirement to proceed '
                    AlertDialog.Builder builder = new AlertDialog.Builder(SellerAct.this);

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
                } else {
                    // LOCATION PERMISSION.
                    if(ContextCompat.checkSelfPermission(SellerAct.this,Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED){
                        // Access location and store in the object ;
                        client = LocationServices.getFusedLocationProviderClient(SellerAct.this);

                        client.getLastLocation().addOnSuccessListener(SellerAct.this,
                                new OnSuccessListener<Location>() {
                                    @Override
                                    public void onSuccess(Location location) {
                                        if(location!=null){
                                            // STORE LOCATION IN object
                                            Sob.UserLocation = location.toString();
                                            System.out.println("KOYA KOYA KOYA : " + Sob.UserLocation);
                                        }
                                    }
                                });
                        // Starting newActivity.
                        startActivity(new Intent(SellerAct.this, CheckActivity.class));
                    } else {
                        // request permission.
                        ActivityCompat.requestPermissions(SellerAct.this,
                                new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                                Sob.Location_PERMISSION_CODE);
                        // if accepted then store in object.
                        // if denied -> alert message location access is needed to proceed.
                        // implemented at
                    }
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==Sob.Location_PERMISSION_CODE){
            if(grantResults.length>0 &&
                    grantResults[0]==PackageManager.PERMISSION_GRANTED ){

                // Access location and store in the object ;

                try{
                    client = LocationServices.getFusedLocationProviderClient(SellerAct.this);
                    client.getLastLocation().addOnSuccessListener(SellerAct.this,
                            new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if(location!=null){
                                        // STORE LOCATION IN object
                                        Sob.UserLocation = location.toString();
                                        System.out.println("KOYA KOYA KOYA : " + Sob.UserLocation);
                                    }
                                }
                            });
                } catch(SecurityException e){System.out.println(e);}
                // Starting CheckActivity on when permission is granted.
                startActivity(new Intent(SellerAct.this, CheckActivity.class));
                Sob.cnt=1;
                Toast.makeText(SellerAct.this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else Toast.makeText(SellerAct.this,"Location Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),SellerType[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    //DO NOTHING.
    }

}


