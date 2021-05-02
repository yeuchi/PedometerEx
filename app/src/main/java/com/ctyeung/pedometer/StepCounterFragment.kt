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
import kotlinx.android.synthetic.main.fragment_step_counter.*


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
    private val sensorListener: SensorEventListener = this
    private var initialCount: Long = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).requestPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_step_counter, container, false)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step_counter, container, false)
        binding.listener = this;
        return binding.root;
    }

    override fun onResume() {
        super.onResume()
        initButtons()
    }

    private fun initButtons() {
        (activity as MainActivity).apply {

            // initial
            btn_clear.isEnabled = false
            btn_pause.isEnabled = false
            btn_stop.isEnabled = false
            btn_play.isEnabled = true

            findViewById<FloatingActionButton>(R.id.btn_play).setOnClickListener { view ->
                startSensor(Sensor.TYPE_STEP_COUNTER, sensorListener)
                currentState = State.ACTIVE
                binding.txtState.text = "Active"

                btn_pause.isEnabled = true
                btn_clear.isEnabled = false
                btn_play.isEnabled = false
                btn_stop.isEnabled = true
            }

            findViewById<FloatingActionButton>(R.id.btn_pause).setOnClickListener { view ->
                (activity as MainActivity).stopSensor(sensorListener)
                currentState = State.PAUSED
                binding.txtState.text = "Paused"

                btn_clear.isEnabled = false
                btn_pause.isEnabled = false
                btn_stop.isEnabled = true
                btn_play.isEnabled = true
            }

            findViewById<FloatingActionButton>(R.id.btn_stop).setOnClickListener { view ->
                (activity as MainActivity).stopSensor(sensorListener)
                currentState = State.STOPPED
                binding.txtState.text = "Stopped"

                btn_clear.isEnabled = true
                btn_pause.isEnabled = false
                btn_stop.isEnabled = false
                btn_play.isEnabled = true
            }

            findViewById<FloatingActionButton>(R.id.btn_clear).setOnClickListener { view ->
                (activity as MainActivity).stopSensor(sensorListener)
                currentState = State.CLEARED
                binding.txtState.text = "Cleared"
                steps = 0
                binding.txtSteps.text = "Steps: ${steps}"

                btn_clear.isEnabled = false
                btn_pause.isEnabled = false
                btn_stop.isEnabled = false
                btn_play.isEnabled = true
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        when (currentState) {
            State.ACTIVE -> {
                if (0L==initialCount) {
                    initialCount = event?.values?.get(0)?.toLong() ?: 0
                }

                steps = (event?.values?.get(0)?.toLong() ?: 0) - initialCount
                binding.txtSteps.text = "Steps: ${steps}"
            }

            State.CLEARED -> {
                initialCount = 0L
                steps = 0L
                binding.txtSteps.text = "Steps: ${steps}"
            }
            else -> {
                // do nothing !
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}