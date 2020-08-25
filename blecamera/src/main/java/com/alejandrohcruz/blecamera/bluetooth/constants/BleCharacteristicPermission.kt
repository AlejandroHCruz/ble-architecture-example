package com.alejandrohcruz.blecamera.bluetooth.constants

enum class BleCharacteristicPermission(val value: Int) {
    None(0),
    Read(1),
    Write(0 shl 2 or 1 shl 1),
    WriteWithResponse(0 shl 1 or 1 shl 2),
    Notify(1 shl 3)
}