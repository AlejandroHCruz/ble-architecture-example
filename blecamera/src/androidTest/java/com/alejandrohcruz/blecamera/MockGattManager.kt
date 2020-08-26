package com.alejandrohcruz.blecamera

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Handler
import android.os.Looper
import com.alejandrohcruz.blecamera.bluetooth.base.*
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerEnum
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerResponseEnum
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCharacteristicPermission
import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState
import com.alejandrohcruz.blecamera.bluetooth.gatt.BaseGattManager
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleCameraListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleNotificationListenerContract
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleReadWriteListenerContract
import java.util.*
import kotlin.collections.ArrayList

class MockGattManager(
    applicationContext: Context?,
    override var bleCameraListener: BleCameraListenerContract?
) : BaseGattManager(applicationContext) {

    // TODO: Bluetooth adapter state. Add a delay in the tests for that?

    override var bleCameraDevice: BleDeviceContract? = null

    override val readWriteListener: BleReadWriteListenerContract = MockBleReadWriteListener(this)
    override val notificationListener: BleNotificationListenerContract =
        MockBleNotificationListener(this)

    override fun connect(macAddress: String) {

        // Scanning & connecting are not in the scope of this mock class

        bleCameraDevice = MockBleDevice(readWriteListener as MockBleReadWriteListener).apply {
            connectionState = BleDeviceState.Connecting
            bleCameraListener?.updateDeviceStateInUi(macAddress, connectionState) // TODO: Use LiveData
        }

        Handler(Looper.getMainLooper()).postDelayed({
            onDeviceConnected(macAddress)
        }, 300L)
    }

    override fun onDeviceConnected(macAddress: String) {

        bleCameraDevice?.apply {

            this.macAddress = macAddress

            BleCameraProfile.notifyingCharacteristics.forEach { characteristicToEnableNotify ->
                enableNotify(
                    BleOperation(
                        CameraService, characteristicToEnableNotify
                    )
                )
            }
        }

        super.onDeviceConnected(macAddress)
    }

    override fun onGpsRequested() {
        // Note: A good library to use is: https://github.com/mcharmas/Android-ReactiveLocation

        //region mock response of this request
        Looper.getMainLooper()?.let {
            Handler(it).postDelayed({
                Location(LocationManager.NETWORK_PROVIDER).apply {
                    time = System.currentTimeMillis()
                    latitude = 25.42321
                    longitude = -101.0053
                    altitude = 1592.0
                    sendGps(this) // Saltillo, Mexico!
                }
            }, 100)
        }
        //endregion
    }

    internal class MockBleDevice(private val readWriteListener: MockBleReadWriteListener) :
        BaseBleDevice() {

        private var notifyingCharacteristicUuids = ArrayList<UUID>()
        private var isVideoRecordingStarted = false

        override fun write(bleOperation: BleOperation): BleEnqueueingError {
            return verifyOperationCanBeEnqueued(bleOperation)
        }

        override fun writeWithResponse(bleOperation: BleOperation): BleEnqueueingError {

            verifyOperationCanBeEnqueued(bleOperation).let { error ->

                if (error == BleEnqueueingError.None) {
                    mockDelayedResponseFromWrite(bleOperation)
                }

                return error
            }
        }

        override fun enableNotify(bleOperation: BleOperation): BleEnqueueingError {
            return if (bleOperation.bleCharacteristic.permissionType == BleCharacteristicPermission.Notify.value) {
                bleOperation.bleCharacteristic.uuid.let {
                    if (!notifyingCharacteristicUuids.contains(it))
                        notifyingCharacteristicUuids.add(it)
                }
                BleEnqueueingError.None
            } else BleEnqueueingError.InvalidBleOperationConfiguration
        }

        override fun isNotifyEnabledForCharacteristic(bleCharacteristic: BleCharacteristic): Boolean {
            return notifyingCharacteristicUuids.contains(bleCharacteristic.uuid)
        }

        private fun buildMockFailedVideoStartResponse(): ByteArray {
            return byteArrayOf(CameraTriggerResponseEnum.FailedVideoStartAlreadyStarted.ordinal.toByte())
        }

        private fun mockDelayedResponseFromWrite(bleOperation: BleOperation) {

            Looper.getMainLooper()?.let {

                Handler(it).postDelayed({

                    var bleOperationToWrite = bleOperation

                    if (bleOperation.data?.first()
                        == CameraTriggerEnum.StartVideoRecording.ordinal.toByte()
                    ) {

                        if (!isVideoRecordingStarted) {

                            isVideoRecordingStarted = true

                        } else {

                            // error!
                            bleOperationToWrite = BleOperation(
                                bleOperation,
                                buildMockFailedVideoStartResponse()
                            )

                        }
                    }

                    readWriteListener.onSuccessfulWriteResponseReceived(bleOperationToWrite)

                }, 30)
            }
        }
    }

    companion object {

        private var instance: BleCameraApi? = null

        /**
         * Entry point for the test module.
         */
        fun getInstance(
            applicationContext: Context?,
            bleCameraListener: BleCameraListenerContract?
        ): BleCameraApi {
            return instance ?: MockGattManager(
                applicationContext,
                bleCameraListener
            )
        }

        fun destroyInstance() {
            instance = null
        }
    }
}