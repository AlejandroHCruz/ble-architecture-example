package com.alejandrohcruz.blecamera

import android.location.Location

/**
 * Entry point for the app module to communicate with this library
 */
interface BleCameraApi {

    fun connect(macAddress: String)

    fun sendGps(location: Location)
    fun takePhoto()
    fun startVideoRecording()
    fun stopVideoRecording()

}