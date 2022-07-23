package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import com.google.android.gms.location.*

class SplashScreen : AppCompatActivity() {
    lateinit var myLocation: FusedLocationProviderClient
    private var myRequestCode:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        myLocation=LocationServices.getFusedLocationProviderClient(this)

        getLastLocation()
    }


//------------------------------------------------

    private fun getLastLocation() {
        if (checkPermission()){
            if (locationEnabled()){
                myLocation.lastLocation.addOnCompleteListener{
                    task->
                    var location:Location? = task.result
                    if (location==null){
                        newLocation()
                    }else{
                        Handler(Looper.myLooper()!!).postDelayed({
                            var intent=Intent(this,MainActivity::class.java)
                            intent.putExtra("lat",location.latitude.toString())
                            intent.putExtra("long",location.longitude.toString())
                            startActivity(intent)
                            finish()
                        },2000)
                    }
                }
            }else{
                Toast.makeText(this,"please Turn On location",Toast.LENGTH_LONG).show()
                Handler(Looper.myLooper()!!).postDelayed({finish()},1500)
            }

        }else{
            requestPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun newLocation() {
        var locationValue= LocationRequest.create()
        locationValue.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationValue.interval=5000
        locationValue.fastestInterval=2000
        locationValue.numUpdates=1
        myLocation=LocationServices.getFusedLocationProviderClient(this)
        myLocation.requestLocationUpdates(locationValue,locationCallback, Looper.myLooper())
    }
    private var locationCallback=object:LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            var lasstLoc: Location? =p0.lastLocation
        }
        }

    private fun locationEnabled(): Boolean {
        var loc=getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return(
                loc.isProviderEnabled(LocationManager.GPS_PROVIDER)||
                loc.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                )
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),myRequestCode)
    }

    private fun checkPermission():Boolean{
        return (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==myRequestCode){
            if (grantResults.isNotEmpty() && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getLastLocation()
            }else {
                Toast.makeText(this,"Please Allow Location Acess",Toast.LENGTH_LONG).show()
                Handler(Looper.myLooper()!!).postDelayed({finish()},1500)
            }
        }
    }
}
