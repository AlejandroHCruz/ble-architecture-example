package com.alejandrohcruz.blecamera.bluetooth.gatt

import android.content.Context
import android.content.ContextWrapper
import android.location.Location
import androidx.annotation.CallSuper
import com.alejandrohcruz.blecamera.BleCameraApi
import com.alejandrohcruz.blecamera.bluetooth.base.BleDeviceContract
import com.alejandrohcruz.blecamera.bluetooth.base.BleEnqueueingError
import com.alejandrohcruz.blecamera.bluetooth.base.BleOperation
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.GpsResponseCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerEnum
import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleCameraListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleNotificationListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleReadWriteListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.GattManagerContract
import com.alejandrohcruz.blecamera.bluetooth.utils.toByteArray
import toByteArray
import java.util.concurrent.TimeUnit

abstract class BaseGattManager(
    applicationContext: Context?,
    var bleCameraListener: BleCameraListenerContract?
) : ContextWrapper(applicationContext), GattManagerContract, BleCameraApi {

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

    // TODO: Return BLeError
    override fun sendGps(location: Location) {

        val data = ArrayList<Byte>()

        val date: ByteArray

        try {
            // 4 bytes for the Unit time (seconds since January 1st 1970)
            date = TimeUnit.MILLISECONDS.toSeconds(location.time).toInt().toByteArray()
        } catch (e: RuntimeException) {
            return
        }

        // TODO: Split into sub-functions and unit test these things

        // latitude & longitude: 4 bytes (Degree x 10^7)
        val longitude = location.longitude.times(10000000).toInt().toByteArray()
        val latitude = location.latitude.times(10000000).toInt().toByteArray()

        // 2 bytes (meters as short) for altitude
        val altitude = location.altitude.toInt().toShort().toByteArray()

        date.forEach { data.add(it) }
        longitude.forEach { data.add(it) }
        latitude.forEach { data.add(it) }
        altitude.forEach { data.add(it) }

        bleCameraDevice?.write(
            BleOperation(CameraService, GpsResponseCharacteristic, data.toByteArray())
        )
    }

    override fun writeToTriggerCameraCharacteristic(cameraTriggerEnum: CameraTriggerEnum): BleEnqueueingError {

        bleCameraDevice?.let {

            return it.write(
                BleOperation(
                    CameraService,
                    TriggerCameraCharacteristic,
                    cameraTriggerEnum.ordinal.toByteArray()
                )
            )
        } ?: return BleEnqueueingError.DeviceIsNull
    }

    override fun takePhoto() {
        writeToTriggerCameraCharacteristic(CameraTriggerEnum.TakePhoto)
    }

    override fun startVideoRecording() {
        writeToTriggerCameraCharacteristic(CameraTriggerEnum.StartVideoRecording)
    }

    override fun stopVideoRecording() {
        writeToTriggerCameraCharacteristic(CameraTriggerEnum.StopVideoRecording)
    }
}