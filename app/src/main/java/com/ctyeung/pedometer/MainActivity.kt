package com.ctyeung.pedometer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {


    var isPermitted: Boolean = false
    private var requestcode = 1000
    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun requestPermission() {
        this.baseContext.let {

            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                    requestcode
                )
            } else {
                val baseView = findViewById(R.id.baseView) as View
                Snackbar.make(baseView, "Permission", Snackbar.LENGTH_LONG)
                    .setAction("Already Granted", null).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        isPermitted = if (requestCode == 1000) {
            true
        } else {
            false
        }

        if (!isPermitted) {
            val baseView = findViewById(R.id.baseView) as View
            Snackbar.make(baseView, "Not Permitted", Snackbar.LENGTH_LONG)
                .setAction("Please give permission", null).show()
        }
    }

    fun startSensor(sensorType: Int, listener: SensorEventListener) {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(sensorType)
        sensorManager?.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopSensor(listener: SensorEventListener) {
        sensorManager?.unregisterListener(listener)
    }
}