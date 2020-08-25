package com.alejandrohcruz.blecamera.bluetooth.gatt.contracts

import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation

interface BleReadWriteListenerContract {
    val gattManager: GattManagerContract
    fun onSuccessfulWriteResponseReceived(bleOperation: BleOperation)
}