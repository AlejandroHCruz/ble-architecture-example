package com.alejandrohcruz.blecamera.bluetooth.gatt.contracts

import com.alejandrohcruz.blecamera.BleCameraApi
import com.alejandrohcruz.blecamera.bluetooth.base.BleDeviceContract
import com.alejandrohcruz.blecamera.bluetooth.base.BleEnqueueingError
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerEnum

interface GattManagerContract : BleCameraApi {

    var bleCameraDevice: BleDeviceContract?
    val readWriteListener: BleReadWriteListenerContract
    val notificationListener: BleNotificationListenerContract
    var bleCameraListener: BleCameraListenerContract?

    fun onDeviceConnected(macAddress: String)
    fun onDeviceDisconnected(macAddress: String)
    fun onGpsRequested()

    fun writeToTriggerCameraCharacteristic(cameraTriggerEnum: CameraTriggerEnum): BleEnqueueingError
}