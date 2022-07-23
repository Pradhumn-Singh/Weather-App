package com.example.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lat =intent.getStringExtra("lat")
        val long =intent.getStringExtra("long")

        getData(lat,long)
    }

    private fun getData(lat: String?, long: String?) {

        val api = "03df382a404b0723dec95b61bc0167e9"



        val queue = Volley.newRequestQueue(this)
        val url= "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${api}"

// Request a string response from the provided URL.
        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { response ->
                setValue(response)
            },
            Response.ErrorListener { Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show() })

// Add the request to the RequestQueue.
        queue.add(jsonRequest)

    }

    private fun setValue(response: JSONObject) {
        Location.text=response.getString("name")
        coordinate.text="Lat: ${response.getJSONObject("coord").getDouble("lat")}  Long: ${response.getJSONObject("coord").getDouble("lon")}"
        Type.text= response.getJSONArray("weather").getJSONObject(0).getString("main")
        val temp=response.getJSONObject("main").getInt("temp")-273
        val minTemp = response.getJSONObject("main").getInt("temp_min")-273
        val maxTemp = response.getJSONObject("main").getInt("temp_max")-273
        Temp.text = "Temp: $temp \nmin: $minTemp  max: $maxTemp"
        pressure.text = response.getJSONObject("main").getInt("pressure").toString()
        humidity.text = response.getJSONObject("main").getInt("humidity").toString()
        val wndSpeed = response.getJSONObject("wind").getInt("speed").toString()
        val wndAngle = response.getJSONObject("wind").getInt("deg").toString()
        windSpeed.text = "Wind Speed: $wndSpeed  Angle: $wndAngle deg"

    }
}