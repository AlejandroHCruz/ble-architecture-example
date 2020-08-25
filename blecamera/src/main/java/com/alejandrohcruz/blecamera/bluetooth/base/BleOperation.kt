package com.alejandrohcruz.blecamera.bluetooth.base

class BleOperation(
    val bleService: BleService,
    val bleCharacteristic: BleCharacteristic,
    val data: ByteArray? = null
)