import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class File {

    private ByteBuffer buffer;

    public File(int[] pointers, RandomAccessFile file) throws IOException {

        buffer = Helper.combinePointers(pointers, file);

    }

    public byte[] getBuffer() {
        return buffer.array();
    }

}
