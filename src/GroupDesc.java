import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class GroupDesc {

    private int inodeTablePointer;

    public GroupDesc(byte[] bytes, RandomAccessFile file) throws IOException {
        file.seek(2048);
        file.read(bytes);

        ByteBuffer buffer = Helper.wrap(bytes);

        inodeTablePointer = buffer.getInt(12);
    }

    public int getInodeTablePointer() {
        return inodeTablePointer;
    }
}
