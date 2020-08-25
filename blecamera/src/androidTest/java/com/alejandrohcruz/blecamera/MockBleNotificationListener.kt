package com.alejandrohcruz.blecamera

import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.GpsRequestCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.GpsRequestCharacteristic.GpsRequest
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleNotificationListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.GattManagerContract
import com.alejandrohcruz.blecamera.bluetooth.utils.toIntLittleEndian

class MockBleNotificationListener(override var gattManager: GattManagerContract) :
    BleNotificationListenerContract {

    override fun onSuccessfulNotificationReceived(bleOperation: BleOperation) {

        when (bleOperation.bleCharacteristic.uuid) {
            GpsRequestCharacteristic.uuid -> {
                if (bleOperation.data?.toIntLittleEndian() == GpsRequest.Requested.ordinal) {
                    gattManager.onGpsRequested()
                } else {
                    // TODO: Log unexpected value
                }
            }
            else -> {
                // TODO: Log unhandled characteristic notifying
            }
        }
    }

    override fun onFailedNotificationReceived(bleOperation: BleOperation) {
        // Intentionally left empty
    }
}