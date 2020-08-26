package com.alejandrohcruz.blecamera.bluetooth.base

class BleOperation(
    val bleService: BleService,
    val bleCharacteristic: BleCharacteristic,
    val data: ByteArray? = null
) {
    constructor(bleOperation: BleOperation, data: ByteArray?) : this(
        bleOperation.bleService,
        bleOperation.bleCharacteristic,
        data
    )
}