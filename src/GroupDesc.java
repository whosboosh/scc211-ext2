import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class GroupDesc {

    private ByteBuffer buffer;
    private int inodeTablePointer;

    /**
     * Create a groupdescriptor
     * @param file file to be read, creates the buffer of groupdescriptor for inode table pointer
     * @param blockGroup block group pointer
     * @throws IOException
     */
    public GroupDesc(RandomAccessFile file, int blockGroup) throws IOException {

        // We assume that the block size is 1024 bytes
        // All group descriptors are stored in block 0, located at 2048 bytes
        // 1024 + superBlockSize (32 * blockGroup)

        int superBlockSize = 1024;
        int blockGroupSeek = (1024 + superBlockSize) + (blockGroup * 32);

        buffer = Helper.wrap(1024, file, blockGroupSeek);

        inodeTablePointer = buffer.getInt(8);
    }

    /**
     * Format the contents of groupdescriptor nicely
     * @return
     */
    public String getGroupDescriptorInformation() {
        return ("---------------------\nGROUP DESCRIPTOR INFORMATION\nInode Table Pointer : "+getInodeTablePointer()+"\n---------------------");
    }

    public int getInodeTablePointer() {
        return inodeTablePointer;
    }

    public byte[] getBuffer() { return buffer.array(); }
}
