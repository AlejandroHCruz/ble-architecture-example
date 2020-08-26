package com.alejandrohcruz.blecamera.bluetooth.constants

import com.alejandrohcruz.blecamera.bluetooth.base.BleCharacteristic
import com.alejandrohcruz.blecamera.bluetooth.base.BleService
import java.util.*

object BleCameraProfile {

    const val Mtu = 23

    val notifyingCharacteristics =
        listOf(
            CameraService.GpsRequestCharacteristic
        )

    object CameraService : BleService {

        // Fixme: This should be a valid UUID. Get the specifications from the client.
        override val uuid: UUID = UUID.fromString("00001101-0000-1000-1000-001010100000")

        object GpsRequestCharacteristic : BleCharacteristic {
            override val uuid: UUID = UUID.fromString("1af4d6c9-463b-4536-9bd9-035c9ae0511b")
            override val minPacketLength = 1
            override val maxPacketLength = 1
            override val permissionType = BleCharacteristicPermission.Notify.value

            enum class GpsRequest {
                Invalid,
                Requested
            }
        }

        object GpsResponseCharacteristic : BleCharacteristic {
            override val uuid: UUID = UUID.fromString("f952fb3d-27b6-4e93-a000-fafe94e863b7")
            override val minPacketLength = 14
            override val maxPacketLength = 14
            override val permissionType = BleCharacteristicPermission.Write.value
        }

        object TriggerCameraCharacteristic : BleCharacteristic {
            override val uuid: UUID = UUID.fromString("2776a249-325d-411d-9124-c7c52d032d18")
            override val minPacketLength = 1
            override val maxPacketLength = 1
            override val permissionType = BleCharacteristicPermission.WriteWithResponse.value

            enum class CameraTriggerEnum {
                Invalid,
                TakePhoto,
                StartVideoRecording,
                StopVideoRecording
            }

            enum class CameraTriggerResponseEnum(value: Int) {
                Invalid(0),
                SuccessfulPhoto(1),
                SuccessfulVideoStart(2),
                SuccessfulVideoEnd(3),
                FailedVideoStartAlreadyStarted(4);

                companion object {

                    // Keep in memory, avoid re-creating a new array every time fromInt() is called
                    private val values = values()

                    fun fromInt(value: Int?) = values.firstOrNull { it.ordinal == value } ?: Invalid
                }
            }
        }
    }
}