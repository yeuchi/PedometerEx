package com.ctyeung.pedometer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.AttributeSet
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ctyeung.pedometer.databinding.ActivityMainBinding
import androidx.databinding.DataBindingUtil
import java.security.Permission
import java.security.Permissions

/*
 * Step sensor doc
 * https://developer.android.com/guide/topics/sensors/sensors_motion
 *
 * Example code
 * https://ssaurel.medium.com/create-a-step-counter-fitness-app-for-android-with-kotlin-bbfb6ffe3ea7
 *
 * Permissions
 * https://stackoverflow.com/questions/20497087/manifest-xml-when-using-sensors
 *
 * Permission request
 * https://developers.google.com/fit/android/authorization
 */

class MainActivity : AppCompatActivity(), SensorEventListener {
    private enum class State {
        PAUSED,
        ACTIVE,
        STOPPED,
        CLEARED
    }

    private var steps: Long = 0
    private var currentState = State.STOPPED
    private lateinit var binding: ActivityMainBinding

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var requestcode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_main)
        //  setSupportActionBar(findViewById(R.id.toolbar))
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.listener = this
        requestPermission()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                requestcode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1000) {

        }
        else {
            // not permitted
        }
    }

    private fun initButtons() {
        findViewById<FloatingActionButton>(R.id.btn_play).setOnClickListener { view ->
            startSensor()
            currentState = State.ACTIVE
            binding.txtState.text = "Active"
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
        }

        findViewById<FloatingActionButton>(R.id.btn_pause).setOnClickListener { view ->
            stopSensor()
            currentState = State.PAUSED
            binding.txtState.text = "Paused"
        }

        findViewById<FloatingActionButton>(R.id.btn_stop).setOnClickListener { view ->
            stopSensor()
            currentState = State.STOPPED
            binding.txtState.text = "Stopped"
        }

        findViewById<FloatingActionButton>(R.id.btn_clear).setOnClickListener { view ->
            stopSensor()
            currentState = State.CLEARED
            binding.txtState.text = "Cleared"
            steps = 0
            binding.txtSteps.text = "Steps: ${steps}"
        }
    }

    override fun onResume() {
        super.onResume()
        initButtons()
    }


    private fun startSensor() {
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun stopSensor() {
        sensorManager?.unregisterListener(this)
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

    override fun onSensorChanged(event: SensorEvent?) {
        when (currentState) {
            State.ACTIVE -> {
                steps = event?.values?.get(0)?.toLong() ?: 0
                binding.txtSteps.text = "Steps: ${steps}"
            }
            State.CLEARED -> {
                steps = 0
                binding.txtSteps.text = "Steps: ${steps}"
            }
            else -> {
                // do nothing !
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}