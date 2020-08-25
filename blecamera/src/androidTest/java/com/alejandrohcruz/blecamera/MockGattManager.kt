package com.alejandrohcruz.blecamera

import android.content.Context
import android.location.Location
import android.os.Handler
import android.os.Looper
import com.alejandrohcruz.blecamera.bluetooth.base.*
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCharacteristicPermission
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

        Handler(Looper.getMainLooper()).postDelayed({
            onDeviceConnected(macAddress)
        }, 300L)
    }

    override fun onDeviceConnected(macAddress: String) {

        bleCameraDevice = MockBleDevice(readWriteListener as MockBleReadWriteListener).apply {

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
        Looper.myLooper()?.let {
            Handler(it).postDelayed({
                Location("MockGattManager").apply {
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

        override fun write(bleOperation: BleOperation): BleEnqueueingError {
            return verifyOperationCanBeEnqueued(bleOperation)
        }

        override fun writeWithResponse(bleOperation: BleOperation): BleEnqueueingError {

            verifyOperationCanBeEnqueued(bleOperation).let { error ->

                if (error == BleEnqueueingError.None) {

                    //region mock response of this operation
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            readWriteListener.onSuccessfulWriteResponseReceived(
                                bleOperation
                            )
                        }, 30)
                    }
                    //endregion
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