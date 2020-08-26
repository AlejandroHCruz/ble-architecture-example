package com.alejandrohcruz.blecamera

import android.util.Log
import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerResponseEnum
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleReadWriteListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.GattManagerContract
import com.alejandrohcruz.blecamera.bluetooth.utils.toIntLittleEndian

internal class MockBleReadWriteListener(override val gattManager: GattManagerContract) :
    BleReadWriteListenerContract {

    val TAG = this.javaClass.simpleName

    override fun onSuccessfulWriteResponseReceived(bleOperation: BleOperation) {

        when (val characteristicUuid = bleOperation.bleCharacteristic.uuid) {

            TriggerCameraCharacteristic.uuid -> {

                val responseAsInt = bleOperation.data?.first()?.toInt()

                val responseEnum =
                    CameraTriggerResponseEnum.fromInt(responseAsInt)

                when (responseEnum) {

                    CameraTriggerResponseEnum.Invalid -> {
                        Log.e(TAG, "Invalid camera trigger response: $responseAsInt")
                        gattManager.bleCameraListener?.onCameraActionError(responseEnum)
                    }

                    CameraTriggerResponseEnum.SuccessfulPhoto,
                    CameraTriggerResponseEnum.SuccessfulVideoStart,
                    CameraTriggerResponseEnum.SuccessfulVideoEnd -> {
                        gattManager.bleCameraListener?.onCameraActionSuccess(responseEnum)
                    }

                    CameraTriggerResponseEnum.FailedVideoStartAlreadyStarted -> {
                        gattManager.bleCameraListener?.onCameraActionError(responseEnum)
                    }
                }
            }
            else -> {
                Log.e(TAG,"Unhandled characteristic: $characteristicUuid")
            }
        }
    }
}