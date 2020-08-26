package com.alejandrohcruz.blecamera.bluetooth.utils

import java.nio.ByteBuffer


fun Long.toByteArray(): ByteArray {
    return ByteBuffer.allocate(Long.SIZE_BYTES).putLong(this).array().drop(4).toByteArray()
}