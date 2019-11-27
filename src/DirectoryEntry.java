import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class DirectoryEntry {

    private Inode inode;
    private short length;
    private byte nameLen;
    private byte fileType;
    private byte[] fileName;

    public DirectoryEntry(ByteBuffer buffer, RandomAccessFile file, Superblock superblock, GroupDesc groupDesc) throws IOException {

        inode = new Inode(file, superblock, groupDesc, buffer.getInt(0));
        length  = buffer.getShort(4);
        nameLen = buffer.get(6);
        fileType = buffer.get(7);

        fileName = new byte[length];
        for (int i = 8; i < length + 8; i++) {
            fileName[i-8] = buffer.get(i);
        }

    }

    public short getLength() {
        return length;
    }

    public Inode getInode() {
        return inode;
    }

    public byte[] getFileName() {
        return fileName;
    }

    public byte getFileType() {
        return fileType;
    }

    public byte getNameLen() {
        return nameLen;
    }



}
