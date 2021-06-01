package com.ctyeung.pedometer

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ctyeung.pedometer.databinding.FragmentStepDetectBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_step_counter.*

class StepDetectFragment : Fragment(), SensorEventListener {

    private lateinit var binding : FragmentStepDetectBinding
    private val sensorListener: SensorEventListener = this
    private var currentState = State.STOPPED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).requestPermission()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_step_detect, container, false)
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_step_detect, container, false)
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
            btn_stop.isEnabled = false
            btn_play.isEnabled = true

            findViewById<FloatingActionButton>(R.id.btn_play).setOnClickListener { view ->
                startSensor(Sensor.TYPE_MOTION_DETECT, sensorListener)
                currentState = State.ACTIVE
                binding.txtState.text = "Active"

                btn_play.isEnabled = false
                btn_stop.isEnabled = true
            }

            findViewById<FloatingActionButton>(R.id.btn_stop).setOnClickListener { view ->
                (activity as MainActivity).stopSensor(sensorListener)
                currentState = State.STOPPED
                binding.txtState.text = "Stopped"

                btn_stop.isEnabled = false
                btn_play.isEnabled = true
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        /*
         * From docs - expect a value of 1 returned
         * https://developer.android.com/reference/android/hardware/Sensor#TYPE_STEP_DETECTOR
         */
        when (currentState) {
            State.ACTIVE -> {
                binding.txtState.text = "Detected!!"
            }

            State.STOPPED -> {
                // nothing is happening
                binding.txtState.text = "Stationed"
            }
            else -> {
                // do nothing !
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
}