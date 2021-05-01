package com.ctyeung.pedometer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ctyeung.pedometer.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_main, container, false)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.listener = this;
        return binding!!.root;
    }

    override fun onResume() {
        super.onResume()
        initButtons()
    }

    private fun initButtons() {
        var bundle = bundleOf("url" to "hello")

        activity?.apply {
            findViewById<Button>(R.id.btn_motion).setOnClickListener {
                binding!!.root.findNavController().navigate(R.id.action_mainFragment_to_motionFragment, bundle)
            }

            findViewById<Button>(R.id.btn_step_count).setOnClickListener {
                binding!!.root.findNavController().navigate(R.id.action_mainFragment_to_stepCounterFragment, bundle)
            }

            findViewById<Button>(R.id.btn_step_detect).setOnClickListener {
                binding!!.root.findNavController().navigate(R.id.action_mainFragment_to_stepDetectFragment, bundle)
            }
        }
    }
}