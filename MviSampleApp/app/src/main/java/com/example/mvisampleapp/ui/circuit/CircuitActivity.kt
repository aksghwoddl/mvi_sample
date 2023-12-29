package com.example.mvisampleapp.ui.circuit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mvisampleapp.di.CircuitModule
import com.slack.circuit.foundation.Circuit
import javax.inject.Inject

class CircuitActivity : AppCompatActivity() {
    @Inject
    @CircuitModule.MainCircuit
    lateinit var circuit: Circuit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}