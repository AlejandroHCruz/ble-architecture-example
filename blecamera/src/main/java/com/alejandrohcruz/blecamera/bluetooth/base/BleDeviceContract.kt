package com.alejandrohcruz.blecamera.bluetooth.base

import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState

interface BleDeviceContract {

    var macAddress: String
    var mtu: Int
    var connectionState: BleDeviceState

    fun verifyOperationCanBeEnqueued(bleOperation: BleOperation): BleEnqueueingError

    fun write(bleOperation: BleOperation): BleEnqueueingError
    fun writeWithResponse(bleOperation: BleOperation): BleEnqueueingError
    fun enableNotify(bleOperation: BleOperation): BleEnqueueingError

    fun isDeviceConnected(): Boolean
    fun isNotifyEnabledForCharacteristic(bleCharacteristic: BleCharacteristic): Boolean
}