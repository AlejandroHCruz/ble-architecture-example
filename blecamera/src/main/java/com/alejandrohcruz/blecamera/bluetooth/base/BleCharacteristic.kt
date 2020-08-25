package com.alejandrohcruz.blecamera.bluetooth.base

import java.util.UUID

interface BleCharacteristic {
    val uuid: UUID
    val minPacketLength: Int
    val maxPacketLength: Int
    val packetLengthRange: IntRange
        get() = minPacketLength..maxPacketLength
    val permissionType: Int
}