package com.alejandrohcruz.blecamera

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.alejandrohcruz.blecamera.bluetooth.base.BleEnqueueingError
import com.alejandrohcruz.blecamera.bluetooth.constants.BleCameraProfile.CameraService.TriggerCameraCharacteristic.CameraTriggerResponseEnum
import com.alejandrohcruz.blecamera.bluetooth.constants.BleDeviceState
import com.alejandrohcruz.blecamera.bluetooth.gatt.contracts.BleCameraListenerContract
import junit.framework.Assert.assertEquals

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class GattManagerInstrumentedTest {

    private val appContext = InstrumentationRegistry.getInstrumentation().targetContext

    //region connection
    @Test
    fun deviceConnection_350ms_isCorrect() {

        var deviceState: BleDeviceState? = null

        val gattManager = MockGattManager.getInstance(
            appContext,
            object : BleCameraListenerContract {
                override fun showCustomForegroundLocationPermissionDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableLocationServicesDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableBluetoothDialog() {
                    // Intentionally left empty
                }

                override fun updateDeviceStateInUi(
                    macAddress: String,
                    connectionState: BleDeviceState
                ) {
                    deviceState = connectionState
                }

                override fun onCameraActionSuccess(responseEnum: CameraTriggerResponseEnum) {
                    // Intentionally left empty
                }

                override fun onCameraActionError(responseEnum: CameraTriggerResponseEnum) {
                    // Intentionally left empty
                }
            }
        )

        gattManager.connect(macAddress)
        Thread.sleep(350)
        assertEquals(BleDeviceState.Connected, deviceState)
    }

    @Test
    fun deviceConnection_immediate_connecting_isCorrect() {

        var deviceState: BleDeviceState? = null

        val gattManager = MockGattManager.getInstance(
            appContext,
            object : BleCameraListenerContract {
                override fun showCustomForegroundLocationPermissionDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableLocationServicesDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableBluetoothDialog() {
                    // Intentionally left empty
                }

                override fun updateDeviceStateInUi(
                    macAddress: String,
                    connectionState: BleDeviceState
                ) {
                    deviceState = connectionState
                }

                override fun onCameraActionSuccess(responseEnum: CameraTriggerResponseEnum) {
                    // Intentionally left empty
                }

                override fun onCameraActionError(responseEnum: CameraTriggerResponseEnum) {
                    // Intentionally left empty
                }
            }
        )

        gattManager.connect(macAddress)
        assertEquals(BleDeviceState.Connecting, deviceState)
    }
    //endregion

    //region writeWithResponse
    @Test
    fun writeWithResponse_takePhoto_isCorrect() {

        var cameraActionSuccess = CameraTriggerResponseEnum.Invalid
        var cameraActionError = CameraTriggerResponseEnum.Invalid

        val gattManager = MockGattManager.getInstance(
            appContext,
            object : BleCameraListenerContract {
                override fun showCustomForegroundLocationPermissionDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableLocationServicesDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableBluetoothDialog() {
                    // Intentionally left empty
                }

                override fun updateDeviceStateInUi(
                    macAddress: String,
                    connectionState: BleDeviceState
                ) {
                    // Intentionally left empty
                }

                override fun onCameraActionSuccess(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionSuccess = responseEnum
                }

                override fun onCameraActionError(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionError = responseEnum
                }
            }
        )

        gattManager.connect(macAddress)
        Thread.sleep(350)
        val enqueueingError = gattManager.takePhoto()
        assertEquals(BleEnqueueingError.None, enqueueingError)
        Thread.sleep(90)
        assertEquals(CameraTriggerResponseEnum.SuccessfulPhoto, cameraActionSuccess)
        assertEquals(CameraTriggerResponseEnum.Invalid, cameraActionError)
    }

    @Test
    fun writeWithResponse_startVideoRecording_isCorrect() {

        var cameraActionSuccess = CameraTriggerResponseEnum.Invalid
        var cameraActionError = CameraTriggerResponseEnum.Invalid

        val gattManager = MockGattManager.getInstance(
            appContext,
            object : BleCameraListenerContract {
                override fun showCustomForegroundLocationPermissionDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableLocationServicesDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableBluetoothDialog() {
                    // Intentionally left empty
                }

                override fun updateDeviceStateInUi(
                    macAddress: String,
                    connectionState: BleDeviceState
                ) {
                    // Intentionally left empty
                }

                override fun onCameraActionSuccess(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionSuccess = responseEnum
                }

                override fun onCameraActionError(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionError = responseEnum
                }
            }
        )

        gattManager.connect(macAddress)
        Thread.sleep(350)
        val enqueueingError = gattManager.startVideoRecording()
        assertEquals(BleEnqueueingError.None, enqueueingError)
        Thread.sleep(60)
        assertEquals(CameraTriggerResponseEnum.SuccessfulVideoStart, cameraActionSuccess)
        assertEquals(CameraTriggerResponseEnum.Invalid, cameraActionError)
    }

    @Test
    fun writeWithResponse_stopVideoRecording_isCorrect() {

        var cameraActionSuccess = CameraTriggerResponseEnum.Invalid
        var cameraActionError = CameraTriggerResponseEnum.Invalid

        val gattManager = MockGattManager.getInstance(
            appContext,
            object : BleCameraListenerContract {
                override fun showCustomForegroundLocationPermissionDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableLocationServicesDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableBluetoothDialog() {
                    // Intentionally left empty
                }

                override fun updateDeviceStateInUi(
                    macAddress: String,
                    connectionState: BleDeviceState
                ) {
                    // Intentionally left empty
                }

                override fun onCameraActionSuccess(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionSuccess = responseEnum
                }

                override fun onCameraActionError(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionError = responseEnum
                }
            }
        )

        gattManager.connect(macAddress)
        Thread.sleep(350)
        val enqueueingError = gattManager.stopVideoRecording()
        assertEquals(BleEnqueueingError.None, enqueueingError)
        Thread.sleep(60)
        assertEquals(CameraTriggerResponseEnum.SuccessfulVideoEnd, cameraActionSuccess)
        assertEquals(CameraTriggerResponseEnum.Invalid, cameraActionError)
    }

    @Test
    fun writeWithResponse_startVideoRecordingTwice_isWrong() {

        var cameraActionSuccess = CameraTriggerResponseEnum.Invalid
        var cameraActionError = CameraTriggerResponseEnum.Invalid

        val gattManager = MockGattManager.getInstance(
            appContext,
            object : BleCameraListenerContract {
                override fun showCustomForegroundLocationPermissionDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableLocationServicesDialog() {
                    // Intentionally left empty
                }

                override fun showCustomEnableBluetoothDialog() {
                    // Intentionally left empty
                }

                override fun updateDeviceStateInUi(
                    macAddress: String,
                    connectionState: BleDeviceState
                ) {
                    // Intentionally left empty
                }

                override fun onCameraActionSuccess(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionSuccess = responseEnum
                }

                override fun onCameraActionError(responseEnum: CameraTriggerResponseEnum) {
                    cameraActionError = responseEnum
                }
            }
        )

        gattManager.connect(macAddress)
        Thread.sleep(350)
        val enqueueingError = gattManager.startVideoRecording()
        assertEquals(BleEnqueueingError.None, enqueueingError)
        Thread.sleep(60)
        val enqueueingError2 = gattManager.startVideoRecording()
        assertEquals(BleEnqueueingError.None, enqueueingError2)
        Thread.sleep(60)
        assertEquals(CameraTriggerResponseEnum.SuccessfulVideoStart, cameraActionSuccess)
        assertEquals(CameraTriggerResponseEnum.FailedVideoStartAlreadyStarted, cameraActionError)
    }
    //endregion

    companion object {
        private const val macAddress = "00:01:02:03:04"
    }
}