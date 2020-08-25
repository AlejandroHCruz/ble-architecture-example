package com.alejandrohcruz.blecamera.bluetooth.gatt.contracts

import com.alejandrohcruz.blecamera.bluetooth.base.BleDeviceContract
import com.alejandrohcruz.blecamera.bluetooth.base.BleEnqueueingError
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerEnum

interface GattManagerContract {
    var bleCameraDevice: BleDeviceContract?
    val readWriteListener: BleReadWriteListenerContract
    val notificationListener: BleNotificationListenerContract

    fun onDeviceConnected(macAddress: String)
    fun onDeviceDisconnected(macAddress: String)
    fun writeToTriggerCameraCharacteristic(cameraTriggerEnum: CameraTriggerEnum): BleEnqueueingError
}