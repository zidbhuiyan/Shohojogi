package com.example.shohojogi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkerMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation,mLastLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 1000;
    private long FASTEST_INTERVAL = 5000;
    private LocationManager locationManager;
    private LatLng latLng,clientLatLng,avgLatLng;
    private boolean isPermisssion;
    private String clientId ="";
    private String startworking ="";
    private  float zoomLevel;
    FirebaseFirestore fstore;
    LinearLayout lhd;
    TextView details,pay;
    Button sbutton,fbutton,call;
    String detailsString,userId,phn,price,job,userName,clientName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_maps);

        lhd = (LinearLayout) findViewById(R.id.lhd);
        details=(TextView) findViewById(R.id.details);
        sbutton = (Button) findViewById(R.id.sbutton);
        fbutton = (Button) findViewById(R.id.fbutton);
        call = (Button) findViewById(R.id.call);
        pay = (TextView) findViewById(R.id.pay);


        if(requestSinglePermission()){
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            mLocationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

            checkLocation();
        }

        getAssignedClient();

        sbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbutton.setEnabled(false);
                sbutton.setVisibility(View.GONE);
                fbutton.setVisibility(View.VISIBLE);
                startworking = "True";
                DatabaseReference refisWorking = FirebaseDatabase.getInstance().getReference("Users").child("Worker").child(userId).child("Working");
                refisWorking.setValue(startworking);

                recordHistory();
            }
        });

        fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sbutton.setEnabled(false);
                sbutton.setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("key","Your Payment is\n"+price+ " BDT");

                WorkerMapCustom_Dialog workerMapCustom_dialog = new WorkerMapCustom_Dialog();
                workerMapCustom_dialog.setArguments(bundle);
                workerMapCustom_dialog.show(getSupportFragmentManager(), "Payment");
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCall = new Intent(Intent.ACTION_CALL);

                if(phn.trim().isEmpty()){
                    call.setEnabled(false);
                }
                else {
                    intentCall.setData(Uri.parse("tel:"+phn));
                }

                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getApplicationContext(),"Please Grant Permission",Toast.LENGTH_SHORT).show();
                    requestCallPermission();
                }
                else {
                    startActivity(intentCall);
                }
            }
        });

    }

    private void recordHistory() {
        DatabaseReference refWorker = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(userId).child("History");
        DatabaseReference refClient = FirebaseDatabase.getInstance().getReference().child("Users").child("Client").child(clientId).child("History");
        DatabaseReference refHistory = FirebaseDatabase.getInstance().getReference().child("History");
        String requestId = refHistory.push().getKey();
        refWorker.child(requestId).setValue(true);
        refClient.child(requestId).setValue(true);

        FirebaseFirestore fstore;
        fstore=FirebaseFirestore.getInstance();
        DocumentReference docClientRef = fstore.collection("users").document(clientId);
        docClientRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    clientName = documentSnapshot.getString("Name");
                    HashMap map = new HashMap();
                    map.put("ClientName",clientName);
                    refHistory.child(requestId).updateChildren(map);

                }
            }
        });

        DocumentReference docWorkerRef = fstore.collection("users").document(userId);
        docWorkerRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    userName = documentSnapshot.getString("Name");
                    HashMap map = new HashMap();
                    map.put("WorkerName",userName);
                    refHistory.child(requestId).updateChildren(map);
                }
            }
        });

        HashMap map = new HashMap();
        map.put("WorkerId",userId);
        map.put("ClientId",clientId);
        map.put("Payment",price);
        map.put("Job",job);
        map.put("Timestamp",getCurrentTimestamp());
        refHistory.child(requestId).updateChildren(map);

    }

    private Long getCurrentTimestamp() {
        Long timestamp = System.currentTimeMillis()/1000;
        return timestamp;
    }

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(WorkerMapsActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }

    private void getAssignedClient() {

        String workerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference assignedClientRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(workerId).child("clientRequestId");
        assignedClientRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                        clientId = snapshot.getValue().toString();
                        getAssignedClientPickupLocation();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getAssignedClientPickupLocation() {

        DatabaseReference assignedClientDistance = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(clientId).child("Distance");
        assignedClientDistance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference assignedClientPickupLocationRef = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(clientId).child("l");
                assignedClientPickupLocationRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            List<Object> map = (List<Object>) snapshot.getValue();
                            double locationLat=0;
                            double locationLng=0;

                            if(map.get(0) != null){
                                locationLat = Double.parseDouble(map.get(0).toString());
                            }
                            if(map.get(1) != null){
                                locationLng = Double.parseDouble(map.get(1).toString());
                            }

                            clientLatLng = new LatLng(locationLat,locationLng);

                            Location loc1 = new Location("");
                            loc1.setLatitude(latLng.latitude);
                            loc1.setLongitude(latLng.longitude);

                            Location loc2 = new Location("");
                            loc2.setLatitude(clientLatLng.latitude);
                            loc2.setLongitude(clientLatLng.longitude);

                            float distance = loc1.distanceTo(loc2);
                            float dis = distance/2;

                            double avglat = (latLng.latitude + clientLatLng.latitude)/2;
                            double avglng = (latLng.longitude + clientLatLng.longitude)/2;
                            avgLatLng = new LatLng(avglat,avglng);

                            double scale = (dis) / 500;
                            zoomLevel =(float) (16 - Math.log(scale) / Math.log(2));

                            fstore=FirebaseFirestore.getInstance();

                            DocumentReference docRef = fstore.collection("users").document(clientId);

                            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if(documentSnapshot.exists()){

                                        detailsString = "Name: "+documentSnapshot.getString("Name")+"\n"+"Number:"+documentSnapshot.getString("Number");
                                        phn = documentSnapshot.getString("Number");
                                        details.setText(detailsString);
                                    }
                                }
                            });

                            DatabaseReference pri = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(clientId).child("Payment");
                            pri.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        price = snapshot.getValue().toString();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            DatabaseReference jo = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(clientId).child("Job");
                            jo.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        job = snapshot.getValue().toString();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            pay.setText("Job: "+job+"\n"+"Payment: " +price+" BDT");

                            if(distance<20){

                                DatabaseReference workerSkill = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(userId);
                                workerSkill.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String skills = snapshot.getValue().toString();

                                            if(skills.contains("True")){
                                                sbutton.setEnabled(false);
                                                sbutton.setVisibility(View.GONE);
                                            }
                                            else if(skills.contains("False")){
                                                sbutton.setEnabled(false);
                                                sbutton.setVisibility(View.GONE);
                                            }
                                            else {
                                                sbutton.setText("Start Working");
                                                sbutton.setEnabled(true);
                                                lhd.setVisibility(View.VISIBLE);
                                                sbutton.setVisibility(View.VISIBLE);
                                                pay.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }
                            else {

                                lhd.setVisibility(View.VISIBLE);
                                sbutton.setVisibility(View.VISIBLE);
                                sbutton.setText("Move to Client Place");
                                pay.setVisibility(View.VISIBLE);
                            }
                            mMap.addMarker(new MarkerOptions().position(clientLatLng).title("Your Client"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private boolean checkLocation() {

        if(!isLocationEnabled()){
            showAlert();
        }

        return isLocationEnabled( ) ;
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Enable Location")
                .setMessage("Enable Your Location")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        dialog.show();
    }

    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private boolean requestSinglePermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        isPermisssion = true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        isPermisssion= false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

                    }
                }).check();
        return isPermisssion;
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        if(avgLatLng!= null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(avgLatLng,zoomLevel));
        }

        else if(latLng != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15F));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        != getPackageManager().PERMISSION_GRANTED){
            return;
        }

        startLocationUpdate();

        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mLocation == null){
            startLocationUpdate();
        }
        else {
           // Toast.makeText(this, "Location is not Detected", Toast.LENGTH_SHORT).show();
        }

    }

    private void startLocationUpdate() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);

        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                        != getPackageManager().PERMISSION_GRANTED){
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        // String msg = "Updated Location: " + Double.toString(location.getLatitude())+":"+Double.toString((location.getLongitude()));

        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        mLastLocation = location;

        latLng = new LatLng(location.getLatitude(),location.getLongitude());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference refAvailable = FirebaseDatabase.getInstance().getReference("WorkerAvailable");
        DatabaseReference refWorking = FirebaseDatabase.getInstance().getReference("WorkerWorking");
        GeoFire geoFireAvailable = new GeoFire(refAvailable);
        GeoFire geoFireWorking = new GeoFire(refWorking);
        switch (clientId){
            case "":
                geoFireWorking.removeLocation(userId);
                geoFireAvailable.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                break;

            default:
                geoFireAvailable.removeLocation(userId);
                geoFireWorking.setLocation(userId, new GeoLocation(location.getLatitude(), location.getLongitude()));
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("WorkerAvailable");
            GeoFire geoFire = new GeoFire(ref);
            geoFire.removeLocation(userId);
        }
    }
}