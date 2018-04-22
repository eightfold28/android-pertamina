package com.example.septialoka.p_predictor;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.rio.xpredicter.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final String TAG = "MAPS_ACTIVITY";
    private Spinner spinner;
    private TextView textDate;
    private ProgressBar progressPredict;
    private MarkerOptions marker;
    public static ModelPredictor currentData;
    String tanggalHariIni = "";
    Integer stepDate = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        textDate = findViewById(R.id.text_date);
        progressPredict = findViewById(R.id.progress_predict);

        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();

        String tanggal = String.valueOf(now.getDate())+ "/" + String.valueOf(now.getMonth() + 1) + "/" + String.valueOf("2018");
        tanggalHariIni = tanggal;
        textDate.setText(tanggal);

        Log.d("lala", tanggalHariIni);

        showLoading(false);
        textDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String tanggal;

                        Calendar start = Calendar.getInstance();
                        Calendar end = Calendar.getInstance();
                        end.set(year, month, dayOfMonth);
                        Date startDate = start.getTime();
                        Date endDate = end.getTime();
                        long startTime = startDate.getTime();
                        long endTime = endDate.getTime();
                        long diffTime = endTime - startTime;
                        Long diffDays = diffTime / (1000 * 60 * 60 * 24);

                        stepDate = diffDays.intValue();

                        tanggal = String.valueOf(dayOfMonth)+ "/" + String.valueOf(month + 1) + "/" + String.valueOf(year);
                        tanggalHariIni = tanggal;
                        textDate.setText(tanggal);
                    }
                });
            }
        });


//        spinner = (Spinner) findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,
//                android.R.layout.simple_spinner_item,paths);
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);
//        spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

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

        final Marker d1 = mMap.addMarker(new MarkerOptions().position(depot_plumpang).title("DEPOT PLUMPANG").icon(BitmapDescriptorFactory.fromResource(R.drawable.depotup)));
        final Marker d2 = mMap.addMarker(new MarkerOptions().position(spbu_yossudarso).title("SPBU YOS SUDARSO").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbudown)));
        final Marker d3 = mMap.addMarker(new MarkerOptions().position(spbu_sunter).title("SPBU SUNTER").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbuup)));
        final Marker d4 = mMap.addMarker(new MarkerOptions().position(spbu_gayamotor).title("SPBU GAYA MOTOR").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbudown)));
        final Marker d5 = mMap.addMarker(new MarkerOptions().position(spbu_danausunter).title("SPBU DANAU SUNTER").icon(BitmapDescriptorFactory.fromResource(R.drawable.spbuup)));

        final HahaBox hb = new HahaBox(MapsActivity.this);
        hb.setOacklik(new HahaBox.OnARClickListener() {
            @Override
            public void onClickG() {
                showLoading(true);

                XPredicter.getInstance().getPrediction(7, "g", stepDate, new RetCon.CallbackRet() {
                    @Override
                    public void success(Object o) {
                        currentData = (ModelPredictor) o;
                        Intent i = new Intent(MapsActivity.this, GraphActivity.class);
                        i.putExtra("data_predict", currentData);
                        startActivity(i);
                        showLoading(false);
                    }

                    @Override
                    public void failure(String messageError) {
                        currentData = null;
                        Toast.makeText(MapsActivity.this, messageError, Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                });
            }

            @Override
            public void onClickD() {
                showLoading(true);

                XPredicter.getInstance().getPrediction(7, "d", stepDate, new RetCon.CallbackRet() {
                    @Override
                    public void success(Object o) {
                        currentData = (ModelPredictor) o;
                        Intent i = new Intent(MapsActivity.this, GraphActivity.class);
                        i.putExtra("data_predict", currentData);
                        startActivity(i);
                        showLoading(false);
                    }

                    @Override
                    public void failure(String messageError) {
                        currentData = null;
                        Toast.makeText(MapsActivity.this, messageError, Toast.LENGTH_SHORT).show();
                        showLoading(false);
                    }
                });
            }
        });
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                hb.setTextTitle(marker.getTitle());
                hb.setTextData(tanggalHariIni);

                hb.show();
                return false;
            }
        });

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(depot_plumpang)      // Sets the center of the map to Mountain View
                .zoom(15.5f)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(50)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


    }

    private void showLoading(boolean ya){
        progressPredict.setVisibility(ya ? View.VISIBLE : View.GONE);
        textDate.setVisibility(!ya ? View.VISIBLE : View.GONE);
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
