package ders.yasin.googlemapapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btnGo, btnChange;
    EditText etLocation;
    ZoomControls zoomControls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        etLocation = (EditText) findViewById(R.id.et_Location);
        btnGo = (Button) findViewById(R.id.btn_Go);
        btnChange = (Button) findViewById(R.id.btn_Change);
        zoomControls = (ZoomControls) findViewById(R.id.zoom_control);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myLocation = etLocation.getText().toString();
                Geocoder geocoder = new Geocoder(MapsActivity.this);

                try {
                    List<Address> addressList = geocoder.getFromLocationName(myLocation, 1);
                    Address myAddress = addressList.get(0);
                    LatLng myLatLng = new LatLng(myAddress.getLatitude(), myAddress.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLatLng).title("Marker in " + myLocation));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(myLatLng));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    btnChange.setText("NORMAL");
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    btnChange.setText("SAT");
                }
            }
        });

        zoomControls.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.zoomIn());
            }
        });
        zoomControls.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.moveCamera(CameraUpdateFactory.zoomOut());
            }
        });

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
            mMap.setMyLocationEnabled(true);
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==100){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }else{
                Toast.makeText(getApplicationContext(),"Location request is denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
