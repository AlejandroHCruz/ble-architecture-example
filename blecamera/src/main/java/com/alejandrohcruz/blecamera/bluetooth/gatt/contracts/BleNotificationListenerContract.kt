package com.alejandrohcruz.blecamera.bluetooth.gatt.contracts

import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation

interface BleNotificationListenerContract {
    var gattManager: GattManagerContract
    fun onSuccessfulNotificationReceived(bleOperation: BleOperation)
    fun onFailedNotificationReceived(bleOperation: BleOperation)
}