package com.ctyeung.pedometer

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ctyeung.pedometer.databinding.FragmentStepCounterBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton


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

class StepCounterFragment : Fragment(), SensorEventListener {
    private enum class State {
        PAUSED,
        ACTIVE,
        STOPPED,
        CLEARED
    }

    private var steps: Long = 0
    private var currentState = State.STOPPED
    private lateinit var binding: FragmentStepCounterBinding

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null
    private var requestcode = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_step_counter, container, false)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step_counter, container, false)
        binding.listener = this;
        return binding!!.root;
    }

    override fun onResume() {
        super.onResume()
        initButtons()
    }

    private fun requestPermission() {
        context?.let {

            if (ContextCompat.checkSelfPermission(it, Manifest.permission.ACTIVITY_RECOGNITION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                activity?.let {

                    ActivityCompat.requestPermissions(
                        it,
                        arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                        requestcode
                    )
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == 1000) {

        } else {
            // not permitted
        }
    }

    private fun initButtons() {
        activity?.apply {

            findViewById<FloatingActionButton>(R.id.btn_play).setOnClickListener { view ->
                startSensor()
                currentState = StepCounterFragment.State.ACTIVE
                binding.txtState.text = "Active"
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
            }

            findViewById<FloatingActionButton>(R.id.btn_pause).setOnClickListener { view ->
                stopSensor()
                currentState = StepCounterFragment.State.PAUSED
                binding.txtState.text = "Paused"
            }

            findViewById<FloatingActionButton>(R.id.btn_stop).setOnClickListener { view ->
                stopSensor()
                currentState = StepCounterFragment.State.STOPPED
                binding.txtState.text = "Stopped"
            }

            findViewById<FloatingActionButton>(R.id.btn_clear).setOnClickListener { view ->
                stopSensor()
                currentState = StepCounterFragment.State.CLEARED
                binding.txtState.text = "Cleared"
                steps = 0
                binding.txtSteps.text = "Steps: ${steps}"
            }
        }
    }

    private fun startSensor() {
        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        sensorManager?.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    private fun stopSensor() {
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (currentState) {
            StepCounterFragment.State.ACTIVE -> {
                steps = event?.values?.get(0)?.toLong() ?: 0
                binding.txtSteps.text = "Steps: ${steps}"
            }
            StepCounterFragment.State.CLEARED -> {
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