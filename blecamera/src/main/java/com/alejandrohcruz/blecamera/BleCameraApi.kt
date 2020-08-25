package com.alejandrohcruz.blecamera

import android.location.Location
import com.alejandrohcruz.blecamera.bluetooth.base.BleEnqueueingError

/**
 * Entry point for the app module to communicate with this library
 */
interface BleCameraApi {

    fun connect(macAddress: String)

    fun sendGps(location: Location): BleEnqueueingError
    fun takePhoto(): BleEnqueueingError
    fun startVideoRecording(): BleEnqueueingError
    fun stopVideoRecording(): BleEnqueueingError

}