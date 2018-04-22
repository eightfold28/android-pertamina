package com.example.septialoka.p_predictor;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MAPS_ACTIVITY";
    private Button date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        date = findViewById(R.id.btn1);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tanggal;

                        tanggal = String.valueOf(dayOfMonth)+ "/" + String.valueOf(month) + "/" + String.valueOf(year);
                        date.setText(tanggal);
                    }
                });
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

        try {
            boolean success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(MapsActivity.this, R.raw.map_style));

            if (!success) {
                Log.e(TAG, "Style parsing failed");
            }
        } catch(Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error");
        }

        // Add a marker in Sydney and move the camera
        LatLng depot_plumpang = new LatLng(-6.1218121,106.89331420000008);
        LatLng spbu_sunter = new LatLng(-6.1471338,106.8860557);
        LatLng spbu_gayamotor = new LatLng(-6.1361986,106.8910296);
        LatLng spbu_yossudarso = new LatLng(-6.1534098,106.886117);
        LatLng spbu_danausunter = new LatLng(-6.1464125,106.872846);

        mMap.addMarker(new MarkerOptions().position(depot_plumpang).title("DEPOT PLUMPANG").icon(BitmapDescriptorFactory.fromResource(R.drawable.depotup)));
        mMap.addMarker(new MarkerOptions().position(spbu_yossudarso).title("SPBU YOS SUDARSO").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbudown)));
        mMap.addMarker(new MarkerOptions().position(spbu_sunter).title("SPBU SUNTER").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbuup)));
        mMap.addMarker(new MarkerOptions().position(spbu_gayamotor).title("SPBU GAYA MOTOR").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbudown)));
        mMap.addMarker(new MarkerOptions().position(spbu_danausunter).title("SPBU DANAU SUNTER").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbuup)));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(depot_plumpang)      // Sets the center of the map to Mountain View
                .zoom(15.5f)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(50)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }


    public void showDialog (DatePickerDialog.OnDateSetListener dateSetListener) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePicker = new DatePickerDialog(MapsActivity.this, dateSetListener, year, month, day );
        datePicker.show();
    }


}
