import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class GroupDesc {

    private int inodeTablePointer;

    public GroupDesc(RandomAccessFile file) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, 2048);

        inodeTablePointer = buffer.getInt(8);
    }

    public int getInodeTablePointer() {
        return inodeTablePointer;
    }
}
