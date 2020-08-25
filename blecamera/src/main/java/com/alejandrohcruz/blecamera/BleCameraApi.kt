package com.alejandrohcruz.blecamera

import android.location.Location

interface BleCameraApi {

    // TODO: Base Activity/Fragment to handle this and permissions
    fun onGpsRequested()
    fun sendGps(location: Location)

    fun connect(macAddress: String)

    fun takePhoto()
    fun startVideoRecording()
    fun stopVideoRecording()
}