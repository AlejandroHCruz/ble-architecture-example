package com.alejandrohcruz.blecamera.bluetooth.gatt

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.alejandrohcruz.blecamera.bluetooth.base.*
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCharacteristicPermission
import com.alejandrohcruz.blecamera.bluetooth.gatt.listeners.BleCameraListener
import com.alejandrohcruz.blecamera.bluetooth.gatt.listeners.BleNotificationListener
import com.alejandrohcruz.blecamera.bluetooth.gatt.listeners.BleReadWriteListener
import java.util.*
import kotlin.collections.ArrayList

class MockGattManager(
    applicationContext: Context?,
    bleCameraListener: BleCameraListener?
) : BaseGattManager(applicationContext, bleCameraListener) {

    // TODO: Bluetooth adapter state. Add a delay in the tests for that?

    override var bleCameraDevice: BleDeviceContract? = null

    // TODO: Mock these 2?
    override val readWriteListener = BleReadWriteListener()
    override val notificationListener = BleNotificationListener()

    override fun connect(macAddress: String) {

        // Scanning & connecting are not in the scope of this mock class

        Handler(Looper.getMainLooper()).postDelayed({
            onDeviceConnected(macAddress)
        },300L)
    }

    override fun onDeviceConnected(macAddress: String) {

        bleCameraDevice = MockBleDevice().apply {

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
        // TODO: Request GPS directly if possible, otherwise ask the listener to show custom dialog
        // TODO: Either bleCameraListener?.onGpsRequested() OR request GPS inside the library direct
        // TODO: Use https://github.com/mcharmas/Android-ReactiveLocation ?
    }

    internal class MockBleDevice : BaseBleDevice() {

        private var notifyingCharacteristicUuids = ArrayList<UUID>()

        override fun write(bleOperation: BleOperation): BleEnqueueingError {
            return verifyOperationCanBeEnqueued(bleOperation)
        }

        override fun writeWithResponse(bleOperation: BleOperation): BleEnqueueingError {

            verifyOperationCanBeEnqueued(bleOperation).let { error ->

                if (error == BleEnqueueingError.None) {
                    Looper.myLooper()?.let {
                        Handler(it).postDelayed({
                            // TODO: Trigger response
                        }, 30)
                    }
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
}