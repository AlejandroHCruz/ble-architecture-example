package com.alejandrohcruz.blecamera.bluetooth.utils

import java.nio.ByteBuffer
import kotlin.experimental.and

fun ByteArray.toIntLittleEndian(): Int {

    return (((this[3].toInt() and 255) shl 24) or ((this[2].toInt() and 255) shl 16) or ((this[1]
        .toInt() and 255) shl 8) or (this[0].toInt() and 255))
}

/**
 * Source: https://stackoverflow.com/questions/7619058/convert-a-byte-array-to-integer-in-java-and-vice-versa
 */
fun ByteArray.toIntBigEndian(): Int {
    return this[0].toInt() shl 24 or ((this[1] and 0xFF.toByte()).toInt() shl 16) or ((this[2]
            and 0xFF.toByte()).toInt() shl 8) or (this[3].toInt() and 0xFF)
}

fun ByteArray.toInt() = ByteBuffer.wrap(this).getInt(0)

fun ByteArray.toFloat() = ByteBuffer.wrap(this).getFloat(0)