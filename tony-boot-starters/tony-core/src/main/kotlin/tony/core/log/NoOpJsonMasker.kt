package tony.core.log

import dev.blaauwendraad.masker.json.JsonMasker
import java.io.InputStream
import java.io.OutputStream

/**
 * 空白 JsonMasker
 * @author tangli
 * @date 2025/08/05 11:32
 */
public class NoOpJsonMasker : JsonMasker {
    override fun mask(input: ByteArray): ByteArray =
        ByteArray(0)

    override fun mask(
        inputStream: InputStream,
        outputStream: OutputStream,
    ) {
    }
}
