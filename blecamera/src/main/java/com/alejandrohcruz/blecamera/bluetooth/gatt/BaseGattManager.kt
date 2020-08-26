package com.alejandrohcruz.blecamera.bluetooth.gatt

import android.content.Context
import android.content.ContextWrapper
import android.location.Location
import androidx.annotation.CallSuper
import com.alejandrohcruz.blecamera.bluetooth.base.BleDeviceContract
import com.alejandrohcruz.blecamera.bluetooth.base.BleEnqueueingError
import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.GpsResponseCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerEnum
import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.GattManagerContract

abstract class BaseGattManager(
    applicationContext: Context?,
) : ContextWrapper(applicationContext), GattManagerContract {

    // TODO: Bluetooth adapter state. Add a delay in the tests?

    // TODO: Support multiple devices
    override var bleCameraDevice: BleDeviceContract? = null

    @CallSuper
     override fun onDeviceConnected(macAddress: String) {
        BleDeviceState.Connected.let {
            bleCameraDevice?.connectionState = it
            bleCameraListener?.updateDeviceStateInUi(macAddress, it) // TODO: Use LiveData
        }
    }

    @CallSuper
    override fun onDeviceDisconnected(macAddress: String) {
        BleDeviceState.DisconnectedAndDiscovered.let {
            bleCameraDevice?.connectionState = it
            bleCameraListener?.updateDeviceStateInUi(macAddress, it) // TODO: Use LiveData
        }
    }

    override fun sendGps(location: Location): BleEnqueueingError {

        val data = GattPackagesBuilder.buildGpsData(location)

        bleCameraDevice?.let {
            return it.write(
                BleOperation(CameraService, GpsResponseCharacteristic, data)
            )
        } ?: return BleEnqueueingError.DeviceIsNull
    }

    override fun writeToTriggerCameraCharacteristic(cameraTriggerEnum: CameraTriggerEnum): BleEnqueueingError {

        bleCameraDevice?.let {

            return it.writeWithResponse(
                BleOperation(
                    CameraService,
                    TriggerCameraCharacteristic,
                    GattPackagesBuilder.buildCameraTriggerData(cameraTriggerEnum)
                )
            )
        } ?: return BleEnqueueingError.DeviceIsNull
    }

    override fun takePhoto(): BleEnqueueingError {
        return writeToTriggerCameraCharacteristic(CameraTriggerEnum.TakePhoto)
    }

    override fun startVideoRecording(): BleEnqueueingError {
        return writeToTriggerCameraCharacteristic(CameraTriggerEnum.StartVideoRecording)
    }

    override fun stopVideoRecording(): BleEnqueueingError {
        return writeToTriggerCameraCharacteristic(CameraTriggerEnum.StopVideoRecording)
    }
}