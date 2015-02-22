package com.example.atapps.localizationproject;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;


public class MapActivity extends Activity implements LocationListener {

    private LocationManager locationManager;
    public Location location;
    private Criteria criteria;
    private String bestProvider;

    private ItemizedOverlay<OverlayItem> locationOverlay;
    private ResourceProxy resourceProxy;

    private MapView mapView;
    private MapController mapController;

    ArrayList<OverlayItem> overlayItemList;
    //point with your location - global because of that all functions which use it but it has to be change
    GeoPoint point;


    //Refreshing current location - should be in new thread
    private void refreshLocation(){
        bestProvider = locationManager.getBestProvider(criteria,true);
        location=locationManager.getLastKnownLocation(bestProvider);
    }

    public void addItem(GeoPoint p){
        OverlayItem newItem = new OverlayItem("Here","Sample",p);
        overlayItemList.add(newItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        setContentView(R.layout.activity_map);

        mapView=(MapView) findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPQUESTOSM);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapController=(MapController) mapView.getController();
        mapController.setZoom(14);

        criteria= new Criteria();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        refreshLocation();
        GeoPoint centerPoint = new GeoPoint(50.1,19.9);
        point = new GeoPoint(location.getLatitude(),location.getLongitude());
        locationManager.requestLocationUpdates(bestProvider,1000,1,this);

        mapController.setCenter(centerPoint);

        overlayItemList = new ArrayList<OverlayItem>();
        addItem(point);
        this.locationOverlay = new ItemizedIconOverlay<OverlayItem>(overlayItemList, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemSingleTapUp(int i, OverlayItem overlayItem) {
                Toast.makeText(getApplicationContext(), "Your localization !", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onItemLongPress(int i, OverlayItem overlayItem) {
                Toast.makeText(getApplicationContext(), "Szerokośće= "+location.getLatitude()+" Długość= "+location.getLongitude(), Toast.LENGTH_SHORT).show();
                return true;
            }
        }, resourceProxy);
        this.mapView.getOverlays().add(this.locationOverlay);

        mapView.invalidate();
    }


    @Override
    public void onLocationChanged(Location location) {
        refreshLocation();
        addItem(point);
        mapController.setCenter(point);
        mapView.invalidate();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
