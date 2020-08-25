import java.nio.ByteBuffer

fun Short.toByteArray(): ByteArray {
    return ByteBuffer.allocate(Short.SIZE_BYTES).putShort(this).array()
}