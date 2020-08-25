package com.alejandrohcruz.blecamera.bluetooth.base

import androidx.annotation.CallSuper
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile
import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState

abstract class BaseBleDevice: BleDeviceContract {

    override var macAddress: String = ""

    override var mtu = BleCameraProfile.Mtu

    override var connectionState = BleDeviceState.DisconnectedAndUndiscovered

    override fun isDeviceConnected() = this.connectionState == BleDeviceState.Connected

    override fun verifyOperationCanBeEnqueued(bleOperation: BleOperation): BleEnqueueingError {
        if (!isDeviceConnected()) {
            return BleEnqueueingError.DeviceDisconnected
        }

        bleOperation.apply {
            if (!bleCharacteristic.packetLengthRange.contains(data?.size ?: 0)) {
                return BleEnqueueingError.InvalidBleOperationConfiguration
            }
        }

        return BleEnqueueingError.None
    }
}