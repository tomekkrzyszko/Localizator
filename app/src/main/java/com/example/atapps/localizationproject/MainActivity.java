package com.example.atapps.localizationproject;

import android.app.Activity;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {

    Button myButton;
    Button MapButton;
    TextView latView;
    TextView lonView;

    private LocationManager locationManager;
    public Location location;
    private Criteria criteria;
    private String bestProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myButton=(Button) (findViewById(R.id.mybutton));
        MapButton=(Button) (findViewById(R.id.button));

        latView=(TextView) (findViewById(R.id.latitude));
        lonView=(TextView) (findViewById(R.id.longitude));

        criteria= new Criteria();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        View.OnClickListener myListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locateMe();

            }
        };

        myButton.setOnClickListener(myListener);

        View.OnClickListener onMapListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();

            }
        };

        MapButton.setOnClickListener(onMapListener);

    }

    public void showMap(){
        Intent trackIntent = new Intent(this,MapActivity.class);
        startActivity(trackIntent);
    }

    public void updateTextViews() {
        if(location.getLatitude()<0)
        {
            latView.setText("Long. = "+ (-location.getLatitude()) + " S");
        }
        else
        {
            latView.setText("Long. = "+ (location.getLatitude()) + " N");
        }

        if(location.getLongitude()<0)
        {
            lonView.setText("Lat. = " + (-location.getLongitude()) + " W");
        }
        else
        {
            lonView.setText("Lat. = " + (location.getLongitude()) + " E");
        }

    }

    public void locateMe(){
        refreshLocation();
        updateTextViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_autor) {
            Toast.makeText(getApplicationContext(), "Autor: Tomasz Krzyszkowski", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshLocation(){
        bestProvider = locationManager.getBestProvider(criteria,true);
        location=locationManager.getLastKnownLocation(bestProvider);
    }
}
