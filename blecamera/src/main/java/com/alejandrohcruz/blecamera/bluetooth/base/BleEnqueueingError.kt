package com.alejandrohcruz.blecamera.bluetooth.base

/**
 * Note: Simplified for the exercise sake, there are many more kinds of potential errors when
 * working with BLE.
 */
enum class BleEnqueueingError {
    None,
    DeviceIsNull,
    DeviceDisconnected,
    InvalidBleOperationConfiguration
}