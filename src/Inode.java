import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Inode {

    private short fileMode;
    private short userID;
    private int lastAccessTime;
    private int creationTime;
    private int lastModified;
    private int deletedTime;
    private short groupID;
    private short numHardLinks;
    private int[] pointers = new int[12];
    private int indirectPointer;
    private int tripleIndirect;
    private int doubleIndirect;
    private long fileSize;

    public Inode(RandomAccessFile file, Superblock superblock, GroupDesc groupDesc, int inodeNumber) throws IOException {

        // Read Inode from Inode Table using Inode Table pointer from Group Descriptor
        int inodePointer = groupDesc.getInodeTablePointer();
        int inodeSize = superblock.getInodeSize();

        //System.out.println("Inode Size: "+inodeSize);

        ByteBuffer buffer = Helper.wrap(inodeSize, file, (1024*inodePointer)+(inodeNumber*128));

        fileMode = buffer.getShort(0);
        userID = buffer.getShort(2);
        int fileSizeLower = buffer.getInt(4);
        lastAccessTime = buffer.getInt(8);
        creationTime = buffer.getInt(12);
        lastModified = buffer.getInt(16);
        deletedTime = buffer.getInt(20);
        groupID = buffer.getShort(24);
        numHardLinks = buffer.getShort(26);

        for (int i = 0; i < 12; i++) {
            pointers[i] = buffer.getInt((i*4)+40);
        }

        indirectPointer = buffer.getInt(88);
        doubleIndirect = buffer.getInt(92);
        tripleIndirect = buffer.getInt(96);
        int fileSizeUpper = buffer.getInt(108);

        fileSize = (((long)fileSizeUpper) << 32) | (fileSizeLower & 0xffffffffL);

        //System.out.println(Arrays.toString(buffer.array()));
        //Helper.dumpHexBytes(buffer.array());
        //System.out.println(Arrays.toString(pointers));
        //System.out.println(fileMode);
    }

    public short getFileMode() {
        return fileMode;
    }

    public short getUserID() {
        return userID;
    }

    public int getLastAccessTime() {
        return lastAccessTime;
    }

    public int getCreationTime() {
        return creationTime;
    }

    public int getLastModified() {
        return lastModified;
    }

    public int getDeletedTime() {
        return deletedTime;
    }

    public short getGroupID() {
        return groupID;
    }

    public short getNumHardLinks() {
        return numHardLinks;
    }

    public int[] getPointers() {
        return pointers;
    }

    public long getFileSize() { return fileSize; }

    public int getIndirectPointer() {
        return indirectPointer;
    }

    public int getTripleIndirect() {
        return tripleIndirect;
    }


}
