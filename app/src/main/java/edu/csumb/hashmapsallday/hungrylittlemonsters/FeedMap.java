package edu.csumb.hashmapsallday.hungrylittlemonsters;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import android.location.*;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

//ADDED FOR TIMER
///////////////////////////////////////////////////////////////
import android.os.Handler;
///////////////////////////////////////////////////////////////


//ADDED FOR USER LOCATION
///////////////////////////////////////////////////////////////
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
////////////////////////////////////////////////////////////////

public class FeedMap extends FragmentActivity implements OnMapReadyCallback, ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    final String TAG = "Application";
    private final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private GoogleMap mMap;

    private static LatLng currCoords;
    private static LatLng destCoords;

    //NEW CODE FOR TIMER
    //////////////////////////////////////////////////////////
    TextView timerTextView;
    long startTime = 0;
    //////////////////////////////////////////////////////////

    //NEW CODE FOR LOCATION
    //////////////////////////////////////////////////////////
    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    //////////////////////////////////////////////////////////

    //NEW CODE FOR BUNDLE
    /////////////////////////////////////////////////////////
    String businessName = "Google";
    String businessLatitude = "37.421707";
    String businessLongitude = "-122.084231";

    private double DLatitude = 37.421707;
    private double DLongtitude = -122.084231;

    private int radiusThreshhold = 300;
    private double distance = -1;
    /////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //sets screen orientation to portrait
        this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //NEW CODE FOR BUNDLE
        /////////////////////////////////////////////////////
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            businessName = bundle.getString("NAME");
            businessLatitude = bundle.getString("LATITUDE");
            businessLongitude = bundle.getString("LONGITUDE");
        }
        ////////////////////////////////////////////////////


        //NEW CODE FOR TIMER
        ////////////////////////////////////////////////////////
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
        ////////////////////////////////////////////////////////


        //NEW CODE FOR LOCATION
        ////////////////////////////////////
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
        ////////////////////////////////////


        //GET DISTANCE
        /////////////////////////////////////

        //CalculationByDistance(latLng, latLng2);

        //set for BIT
        //destCoords = new LatLng(36.652389, -121.797291);
        DLatitude = Double.parseDouble(businessLatitude);
        DLongtitude = Double.parseDouble(businessLongitude);

        destCoords = new LatLng(DLatitude, DLongtitude);
        //when connected gets the distance
        //CalculationByDistance(currCoords, destCoords);
        /////////////////////////////////////
    }

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;


            //every 5 seconds
            if(seconds % 10 == 0) {
                Log.d(TAG, "attempting to connect again");
                Log.d(TAG, String.format("%d:%02d", minutes, seconds));
                if (mGoogleApiClient.isConnected()) {
                    mGoogleApiClient.disconnect();
                }

                mGoogleApiClient.connect();
            }

            timerHandler.postDelayed(this, 500);
        }
    };

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d(TAG, "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //or valueresult
        return Radius * c;
    }

    public int getKM(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d(TAG, "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);
        //or valueresult
        return kmInDec;
    }

    public int getM(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d(TAG, "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        //or valueresult
        return meterInDec;
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
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        //check for internet permission
        /////////////////////////////////////////////
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        Log.d(TAG, "INTERNET permission " + permissionCheck);

        int permissionCheck3 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d(TAG, "FINE location permission " + permissionCheck3);

        // Assume thisActivity is the current activity
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, "COARSE location permission " + permissionCheck2);
        ///////////////////////////////////////////


        //checks if the user has enabled the right permission
        ////////////////////////////////////////////////////
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkPermissions();
        } else {
            //onSearch();
            mapLocation();
        }
        ////////////////////////////////////////////////////
    }

    public void getUserLocation() {

    }

    public void checkPermissions() {
        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        Log.d(TAG, "FINE location permission " + permissionCheck);

        // Assume thisActivity is the current activity
        int permissionCheck2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        Log.d(TAG, "COARSE location permission " + permissionCheck2);

        //NEW
        //ASKS FOR THE PERMISSIONS
        //////////////////////////////////
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        ////////////////////////////////////////////
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    Log.d(TAG, "permission granted");
                    //goes to the search portion
                    //onSearch();
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mMap.setMyLocationEnabled(true);
                    mapLocation();

                }else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "permission denied");

                    //RETURN TO ORIGINAL ACTIVITY
                    Intent i = new Intent(getApplicationContext(), FeedMe.class);
                    startActivity(i);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    public void getCurrentLocation() {

    }

    //works
    //in the future pass in the value of the business name
    public void mapLocation() {
        Log.d(TAG, "Entering onSearch");
        //checks for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            return;
        }

        //LATITUDE AND LONGITUDE BASED
        ////////////////////////////////////////////////////////////

        //based on latitude and longitude
        //LatLng latLng = new LatLng(36.687945, -121.798975);

        Log.d(TAG, "before addmarker");
        //set title of marker

        //mMap.addMarker(new MarkerOptions().position(latLng).title("Jack in the Box"));

        //static final LatLng MELBOURNE = new LatLng(-37.813, 144.962);


        //BITMAP IMAGE COVERT
        ///////////////////////////////////////////////////////////////////////
        /*
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(80, 80, conf);
        Canvas canvas1 = new Canvas(bmp);

// paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setTextSize(35);
        color.setColor(Color.BLACK);

// modify canvas
        canvas1.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_burger), 0,0, color);
        canvas1.drawText("User Name!", 30, 40, color);

// add marker to Map
        mMap.addMarker(new MarkerOptions().position(latLng)
                .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                // Specifies the anchor to be at a particular point in the marker image.
                .anchor(0.5f, 1));
*/
        //////////////////////////////////////////

        //customize marker
        ///////////////////////

        Marker currentMarker = mMap.addMarker(new MarkerOptions()
                .position(destCoords)
                .title(businessName)
                .snippet("Feed Monster Here :)")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.burger)));
        //.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        ///////////////////////////

        Log.d(TAG, "before animate camera");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(destCoords));


        Log.d(TAG, "after addresses");
        //will zoom in on the point
        ////////////////////////////
        CameraUpdate center = CameraUpdateFactory.newLatLng(destCoords);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(10);

        //mMap.moveCamera(center);
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destCoords, 10));

// Zoom in, animating the camera.
        //mMap.animateCamera(CameraUpdateFactory.zoomIn());

// Zoom out to zoom level 10, animating with a duration of 2 seconds.
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);

        //mMap.animateCamera(zoom);

    }


    public void onSearch() {
        Log.d(TAG, "Entering onSearch");
        //checks for permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            // Show rationale and request permission.
            return;
        }

        //LATITUDE AND LONGITUDE BASED
        ////////////////////////////////////////////////////////////

        //based on latitude and longitude
        LatLng latLng = new LatLng(36.687361, -121.799678);

        Log.d(TAG, "before addmarker");
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
        Log.d(TAG, "before animate camera");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


        Log.d(TAG, "after addresses");
        //will zoom in on the point
        ////////////////////////////
        CameraUpdate center = CameraUpdateFactory.newLatLng(latLng);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mMap.moveCamera(center);
        mMap.animateCamera(zoom);

        //DOES NOT WORK PROPERLY
        /////////////////////////////////////////////////////
        /*
         Log.d(TAG, "Before String location");
        String location = "Taco Bell 1830 Fremont Blvd, Seaside, CA 93955";
        List<Address> addressList = null;
        if(location != null || !location.equals(""));
        {
            Log.d(TAG, "before geocoder");
            Geocoder geocoder = new Geocoder(this);
            try {
                Log.d(TAG, "inside try, geocoder");

                //do a try catch here

                addressList = geocoder.getFromLocationName(location, 1);
                Log.d(TAG, addressList.get(0).toString());

            } catch (IOException e) {
                e.printStackTrace();
            }

            //For string based addresses
            ///////////////////////////////////////////////////////////
            Log.d(TAG, "before addresses");
            Address address = addressList.get(0);
            Log.d(TAG, "before latlng");

            //based on address
            //LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            //Log.d(TAG, "getLatitude = "+ address.getLatitude());
            //Log.d(TAG, "getLongitude = "+address.getLongitude());

            //based on latitude and longitude
            LatLng latLng = new LatLng(36.687361, -121.799678);

            ////////////////////////////////////////////////////////////
            Log.d(TAG, "before addmarker");
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            Log.d(TAG, "before animate camera");
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));


            Log.d(TAG, "after addresses");
            //will zoom in on the point
            ////////////////////////////
            CameraUpdate center= CameraUpdateFactory.newLatLng(latLng);
            CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

            mMap.moveCamera(center);
            mMap.animateCamera(zoom);

            ///////
        }*/
    }


    //ADDED FOR LOCATION
    ////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
        Log.d(TAG, "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
        //pauses the timer
        //timerHandler.removeCallbacks(timerRunnable);

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        android.location.Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(TAG, "location is null");

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

            Log.d(TAG, "onConnceted "+currentLatitude + " WORKS " + currentLongitude + "");

            //onConnected updates location
            currCoords = new LatLng(currentLatitude, currentLongitude);
            //Toast.makeText(this, "onConnected "+currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

            Log.d(TAG, "Current latitude: "+currCoords.latitude+" longitude: "+currCoords.longitude);
            Log.d(TAG, "Destination latitude: "+destCoords.latitude+" longitude: "+destCoords.longitude);

            int kmDist = getKM(currCoords, destCoords);
            int mDist = getM(currCoords, destCoords);
            double aDist = CalculationByDistance(currCoords, destCoords);

            //DISTANCES
            //////////////////////////////////////////////////////////
            if(mDist  >  1 && aDist < 1){
                Log.d(TAG, "Distance: "+mDist+" Meters");
                Toast.makeText(this, "Distance: "+mDist+" Meters", Toast.LENGTH_SHORT).show();
            }
            else{

                if(mDist == 0 && aDist < 1 ){
                    double tempDist = aDist*1000;

                    //formats the double
                    NumberFormat formatter = new DecimalFormat("#0.00");

                    Log.d(TAG, "Distance: "+formatter.format(tempDist)+" Meters");
                    Toast.makeText(this, "Distance: "+formatter.format(tempDist)+" Meters", Toast.LENGTH_SHORT).show();
                    distance = tempDist;
                }
                else {
                    Log.d(TAG, "Distance: "+getKM(currCoords, destCoords)+" KM");
                    Toast.makeText(this, "Distance: "+getKM(currCoords, destCoords)+" KM", Toast.LENGTH_SHORT).show();
                }
            }

            //only starts checking for the distance when the person is within 100 meters
            if(distance < radiusThreshhold && aDist < 1){
                Log.d(TAG, "within reach");

                //LAUNCH INTENT TO FeedIt Here
                //////////////////////////////////
                Intent i = new Intent(getApplicationContext(), FeedIt.class);
                startActivity(i);

                //////////////////////////////////
            }

            /////////////////////////////////////////////////////////
            /*
            Double aDist = CalculationByDistance(currCoords, destCoords);
            Toast.makeText(this, "Distance: "+ aDist/1000 +" Meters", Toast.LENGTH_SHORT).show();
            */
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
                Log.d(TAG, "Location services canceled original pendingintern");
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.d(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();

        Toast.makeText(this, "LocaToastion Changed "+currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onLocationChanged "+currentLatitude + " WORKS " + currentLongitude + "");
    }
    //////////////////////////////////////////////////
}