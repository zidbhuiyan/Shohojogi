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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryDataEventListener;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.shohojogi.databinding.ActivityClientMapsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class ClientMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,com.google.android.gms.location.LocationListener {

    private GoogleMap mMap;
    private ActivityClientMapsBinding binding;
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation,mLastLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private com.google.android.gms.location.LocationListener listener;
    private long UPDATE_INTERVAL = 3000;
    private long FASTEST_INTERVAL = 5000;
    private LocationManager locationManager;
    private LatLng latLng,markClient,avgLatLng,workerLatLng;
    private boolean isPermisssion;
    private Button offerbutton,call;
    private  int radius=1;
    private boolean workerfound = false;
    private String workerfoundId;
    private Marker workerMarker,clientMarker;
    private float distance,zoomLevel,dis;
    private TextView details,pay;
    private LinearLayout lhd;
    String detailsString,phn,price,skill;

    FirebaseFirestore fstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_maps);

        offerbutton = (Button) findViewById(R.id.offerbutton);
        details = (TextView) findViewById(R.id.details);
        pay = (TextView) findViewById(R.id.pay);
        lhd = (LinearLayout) findViewById(R.id.lhd);
        call = (Button) findViewById(R.id.call);

        skill = getIntent().getStringExtra("catWorker").toString();

        price = getIntent().getStringExtra("total_price");

        pay.setText("Job: "+skill+"\n"+"Payment: " +price+" BDT");



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

        offerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerbutton.setBackgroundResource(R.color.teal_light);
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("ClientRequest");
                GeoFire geoFire = new GeoFire(ref);

                geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));

                DatabaseReference pri = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(userId).child("Payment");
                pri.setValue(price);

                DatabaseReference job = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(userId).child("Job");
                job.setValue(skill);

                markClient = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
                clientMarker = mMap.addMarker(new MarkerOptions().position(markClient).title("Your Location"));


                offerbutton.setText("Finding Worker...");
                offerbutton.setEnabled(false);
                
                getclosestWorker();
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

    private void requestCallPermission() {
        ActivityCompat.requestPermissions(ClientMapsActivity.this,new String[]{Manifest.permission.CALL_PHONE},1);
    }


    private void getclosestWorker() {
        DatabaseReference workerLocation = FirebaseDatabase.getInstance().getReference().child("WorkerAvailable");
        GeoFire geoFire = new GeoFire(workerLocation);
        GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(markClient.latitude,markClient.longitude), radius);
        geoQuery.removeAllListeners();

       geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
           @Override
           public void onKeyEntered(String key, GeoLocation location) {
               DatabaseReference workerSkill = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(key);
               workerSkill.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if (snapshot.exists()){
                           String skills = snapshot.getValue().toString();

                           if(skills.contains(skill)){
                               if(!workerfound){
                                   workerfound = true;
                                   workerfoundId = key;

                                   DatabaseReference workerRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Worker").child(workerfoundId);
                                   String clientId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                   HashMap map = new HashMap();
                                   map.put("clientRequestId", clientId);
                                   workerRef.updateChildren(map);

                                   getWorkerLocation();
                                   offerbutton.setText("Looking for Worker Location...");
                               }
                           }
                       }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
           }

           @Override
           public void onKeyExited(String key) {

           }

           @Override
           public void onKeyMoved(String key, GeoLocation location) {

           }

           @Override
           public void onGeoQueryReady() {

               if(!workerfound){
                   radius++;
                   if(radius>5){
                     radius = 1;
                   }
                   getclosestWorker();
               }

           }

           @Override
           public void onGeoQueryError(DatabaseError error) {

           }
       });

    }

    private void getWorkerLocation() {
        DatabaseReference workerLocationRef = FirebaseDatabase.getInstance().getReference().child("WorkerWorking").child(workerfoundId).child("l");
        workerLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<Object> map = (List<Object>) snapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;


                    if(map.get(0) != null){
                        locationLat = Double.parseDouble(map.get(0).toString());
                    }
                    if(map.get(1) != null){
                        locationLng = Double.parseDouble(map.get(1).toString());
                    }

                    workerLatLng = new LatLng(locationLat,locationLng);

                    if(workerMarker != null){
                        workerMarker.remove();
                    }

                    Location loc1 = new Location("");
                    loc1.setLatitude(markClient.latitude);
                    loc1.setLongitude(markClient.longitude);

                    Location loc2 = new Location("");
                    loc2.setLatitude(workerLatLng.latitude);
                    loc2.setLongitude(workerLatLng.longitude);

                    distance = loc1.distanceTo(loc2);
                    dis = distance/2;

                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference dist = FirebaseDatabase.getInstance().getReference().child("ClientRequest").child(userId).child("Distance");
                    dist.setValue(distance);

                    double avglat = (markClient.latitude + workerLatLng.latitude)/2;
                    double avglng = (markClient.longitude + workerLatLng.longitude)/2;
                    avgLatLng = new LatLng(avglat,avglng);

                    double scale = (dis) / 500;
                    zoomLevel =(float) (16 - Math.log(scale) / Math.log(2));
                    fstore=FirebaseFirestore.getInstance();

                    DocumentReference docRef = fstore.collection("users").document(workerfoundId);

                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot.exists()){

                                detailsString = "Name: "+documentSnapshot.getString("Name")+"\n"+"Gender:"+documentSnapshot.getString("Gender")+"\n"+"Number:"+documentSnapshot.getString("Number");
                                phn = documentSnapshot.getString("Number");
                                details.setText(detailsString);
                            }
                        }
                    });



                    if(distance<50){
                        DatabaseReference refisWorking = FirebaseDatabase.getInstance().getReference("Users").child("Worker").child(workerfoundId).child("Working");
                        refisWorking.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(snapshot.exists()){
                                    String bool = snapshot.getValue().toString();
                                    if(bool.contains("True")){
                                        offerbutton.setText("Worker is Working");
                                    }
                                    if(bool.contains("False")){
                                        offerbutton.setText("Work is Done");
                                        pay.setVisibility(View.GONE);
                                        Bundle bundle = new Bundle();
                                        bundle.putString("key","How was your experience?");

                                        ClientMapCustom_Dialog clientMapCustom_dialog = new ClientMapCustom_Dialog();
                                        clientMapCustom_dialog.setArguments(bundle);
                                        clientMapCustom_dialog.show(getSupportFragmentManager(), "Payment");
                                    }
                                }
                                else {
                                    offerbutton.setText("Worker is here");
                                    lhd.setVisibility(View.VISIBLE);
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                    else {
                        offerbutton.setText("Worker Found");
                        lhd.setVisibility(View.VISIBLE);
                    }

                    workerMarker = mMap.addMarker(new MarkerOptions().position(workerLatLng).title("Worker").icon(BitmapDescriptorFactory.fromResource(R.drawable.person)));

                }
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

        else if(markClient != null){
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markClient,15F));
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
            //Toast.makeText(this, "Location is not Detected", Toast.LENGTH_SHORT).show();
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

       //String msg = "Updated Location: " + Double.toString(location.getLatitude())+":"+Double.toString((location.getLongitude()));

        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

        mLastLocation = location;

        latLng = new LatLng(location.getLatitude(),location.getLongitude());
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
        }
    }
}