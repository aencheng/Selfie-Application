package com.example.project9

import android.hardware.SensorManager
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope


class SensorViewModel
    : ViewModel() {
    private val TAG = "SensorViewModel"
    private lateinit var accelerometerSensor: MeasurableSensor

    private var accelerometerData = floatArrayOf(
        SensorManager.GRAVITY_EARTH, SensorManager.GRAVITY_EARTH, 0.0F
    )

    private var _shaken: MutableLiveData<Boolean> = MutableLiveData(false)
    val shaken: LiveData<Boolean>
        get() = _shaken


    fun initializeSensors(sAccelerometer: MeasurableSensor) {
        //initialize accelerometer sensor
        accelerometerSensor = sAccelerometer
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { a ->
            val x: Float = a[0]
            val y: Float = a[1]
            val z: Float = a[2]
            accelerometerData[1] = accelerometerData[0]
            accelerometerData[0] = Math.sqrt((x * x).toDouble() + y * y + z * z).toFloat()
            val delta: Float = accelerometerData[0] - accelerometerData[1]
            accelerometerData[2] = accelerometerData[2] * 0.9f + delta

            _shaken.value = accelerometerData[2] > 12

            if (accelerometerData[2] > 12) {
                Log.i(TAG, "Phone Shaken, Navigating to next Fragment")
            }
        }
        Log.i("Initial Value", shaken.value.toString())
    }
}