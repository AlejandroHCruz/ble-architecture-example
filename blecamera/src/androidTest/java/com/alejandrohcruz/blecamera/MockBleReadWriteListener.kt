package com.alejandrohcruz.blecamera

import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerResponseEnum
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleReadWriteListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.GattManagerContract
import com.alejandrohcruz.blecamera.bluetooth.utils.toIntLittleEndian

internal class MockBleReadWriteListener(override val gattManager: GattManagerContract) :
    BleReadWriteListenerContract {

    override fun onSuccessfulWriteResponseReceived(bleOperation: BleOperation) {

        when (bleOperation.bleCharacteristic.uuid) {

            TriggerCameraCharacteristic.uuid -> {

                val responseEnum =
                    CameraTriggerResponseEnum.fromInt(bleOperation.data?.toIntLittleEndian())

                when (responseEnum) {

                    CameraTriggerResponseEnum.Invalid -> {
                        // TODO: Log
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
                // TODO: Log unhandled characteristic
            }
        }
    }
}