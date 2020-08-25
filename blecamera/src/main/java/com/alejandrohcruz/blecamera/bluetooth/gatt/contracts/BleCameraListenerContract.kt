package com.alejandrohcruz.blecamera.bluetooth.gatt.contracts

import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState

/**
 * This interface is to be implemented by the user of this library.
 */
interface BleCameraListenerContract {
    fun showCustomForegroundLocationPermissionDialog()
    fun showCustomEnableLocationServicesDialog()
    fun showCustomEnableBluetoothDialog()
    fun updateDeviceStateInUi(macAddress: String, connectionState: BleDeviceState)
}