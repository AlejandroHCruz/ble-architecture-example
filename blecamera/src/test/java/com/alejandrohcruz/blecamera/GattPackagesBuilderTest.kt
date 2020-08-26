package com.alejandrohcruz.blecamera

import android.location.Location
import android.location.LocationManager
import com.alejandrohcruz.blecamera.bluetooth.gatt.GattPackagesBuilder
import com.alejandrohcruz.blecamera.bluetooth.utils.toInt
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class GattPackagesBuilderTest {

    private val testLocation = Location(LocationManager.NETWORK_PROVIDER).apply {
        latitude = 25.42321
        longitude = -101.0053
        altitude = 1592.0
    }

    //region buildDateData
    @Test
    fun buildDateData_isCorrect_Now() {
        val now = Date()
        testLocation.time = now.time
        val data = GattPackagesBuilder.buildDateData(testLocation)
        assert(data.size == 4)
        assertEquals(testLocation.time.div(1000).toInt(), data.toInt())
    }

    @Test
    fun buildDateData_isCorrect_2020() {
        testLocation.time = AUGUST_26_2020
        val data = GattPackagesBuilder.buildDateData(testLocation)
        assert(data.size == 4)
        assertEquals(testLocation.time.div(1000).toInt(), data.toInt())
    }

    @Test
    fun buildDateData_isCorrect_2001() {
        testLocation.time = MAY_15_2001
        val data = GattPackagesBuilder.buildDateData(testLocation)
        assert(data.size == 4)
        assertEquals(testLocation.time.div(1000).toInt(), data.toInt())
    }

    @Test
    fun buildDateData_isCorrect_1970() {
        testLocation.time = JAN_02_1970
        val data = GattPackagesBuilder.buildDateData(testLocation)
        assert(data.size == 4)
        assertEquals(testLocation.time.div(1000).toInt(), data.toInt())
    }

    @Test
    fun buildDateData_isCorrect_2050() {
        testLocation.time = DEC_10_2050
        val data = GattPackagesBuilder.buildDateData(testLocation)
        assert(data.size == 4)
        assertEquals(testLocation.time.div(1000).toInt(), data.toInt())
    }
    //endregion

    companion object {
        const val AUGUST_26_2020 = 1598471371000L
        const val MAY_15_2001 = 989956171000L
        const val JAN_02_1970 = 129600000L
        const val DEC_10_2050 = 2554286400000L
    }
}