package com.alejandrohcruz.blecamera.bluetooth.utils

import java.nio.ByteBuffer
import java.nio.ByteOrder

fun Int.toByteArray(): ByteArray {
    return ByteBuffer.allocate(Int.SIZE_BYTES).putInt(this).array()
}

fun Int.toByteArrayLittleEndian(): ByteArray {
    ByteBuffer.allocate(Int.SIZE_BITS / Byte.SIZE_BITS).apply {
        order(ByteOrder.LITTLE_ENDIAN)
        putInt(this@toByteArrayLittleEndian)
        return array()
    }
}