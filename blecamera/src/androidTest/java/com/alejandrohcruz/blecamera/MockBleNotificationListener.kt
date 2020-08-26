package com.alejandrohcruz.blecamera

import android.util.Log
import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.GpsRequestCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.GpsRequestCharacteristic.GpsRequest
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleNotificationListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.GattManagerContract
import com.alejandrohcruz.blecamera.bluetooth.utils.toIntLittleEndian

class MockBleNotificationListener(override var gattManager: GattManagerContract) :
    BleNotificationListenerContract {

    val TAG = this.javaClass.simpleName

    override fun onSuccessfulNotificationReceived(bleOperation: BleOperation) {

        when (val characteristicUuid = bleOperation.bleCharacteristic.uuid) {
            GpsRequestCharacteristic.uuid -> {
                val responseAsInt = bleOperation.data?.toIntLittleEndian()
                if (responseAsInt == GpsRequest.Requested.ordinal) {
                    gattManager.onGpsRequested()
                } else {
                    Log.e(TAG, "Invalid gps request notification: $responseAsInt")
                }
            }
            else -> {
                Log.e(TAG,"Unhandled characteristic: $characteristicUuid")
            }
        }
    }

    override fun onFailedNotificationReceived(bleOperation: BleOperation) {
        // Intentionally left empty
    }
}