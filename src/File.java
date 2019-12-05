import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class File {

    private ByteBuffer buffer;

    public File(Inode inode, RandomAccessFile file) throws IOException {

        buffer = Helper.combinePointers(inode, file);

    }

    public byte[] getBuffer() {
        return buffer.array();
    }

}
