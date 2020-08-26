package com.alejandrohcruz.blecamera.bluetooth.gatt

import android.location.Location
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerEnum
import com.alejandrohcruz.blecamera.bluetooth.utils.toByteArray
import com.alejandrohcruz.blecamera.bluetooth.utils.toReversedByteArray
import toByteArray
import java.util.concurrent.TimeUnit

object GattPackagesBuilder {

    fun buildCameraTriggerData(cameraTriggerEnum: CameraTriggerEnum): ByteArray {
        return cameraTriggerEnum.ordinal.toByteArray()
    }

    fun buildGpsData(location: Location): ByteArray {
        ArrayList<Byte>().apply {
            buildDateData(location).forEach {
                add(it)
            }
            buildLocationData(location).forEach {
                add(it)
            }
            // Data is expected as little endian
            return toReversedByteArray()
        }
    }

    /**
     * Produces 4 bytes (seconds since January 1st 1970)
     */
    private fun buildDateData(location: Location):ByteArray {
        return try {
            TimeUnit.MILLISECONDS.toSeconds(location.time).toInt().toByteArray()
        } catch (e: RuntimeException) {
            // Will produce a InvalidBleOperationConfiguration if the length does not match the requirements
            ByteArray(0)
        }
    }

    /**
     * Encodes the latitude, longitude and altitude.
     */
    private fun buildLocationData(location: Location): ArrayList<Byte> {
        ArrayList<Byte>().apply {
            buildLatitudeOrLongitudeData(location.latitude).forEach {
                add(it)
            }
            buildLatitudeOrLongitudeData(location.longitude).forEach {
                add(it)
            }
            buildAltitudeData(location).forEach {
                add(it)
            }
            return this
        }
    }

    /**
     * Produces 4 bytes (Degree x 10^7)
     */
    private fun buildLatitudeOrLongitudeData(latOrLong: Double): ByteArray {
        return latOrLong.times(10_000_000).toInt().toByteArray()
    }

    /**
     * Produces 2 bytes (meters as short)
     */
    private fun buildAltitudeData(location: Location): ByteArray {
        return location.altitude.toInt().toShort().toByteArray()
    }
}