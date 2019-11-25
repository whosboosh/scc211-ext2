import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Inode {

    private short fileMode;
    private short userID;
    private int fileSizeLower;
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
    private int fileSizeUpper;

    public Inode(RandomAccessFile file, Superblock superblock, GroupDesc groupDesc) throws IOException {

        // Read Inode from Inode Table using Inode Table pointer from Group Descriptor
        int inodePointer = groupDesc.getInodeTablePointer();
        int inodeSize = superblock.getInodeSize();

        System.out.println("Inode Size: "+inodeSize);

        ByteBuffer buffer = Helper.wrap(inodeSize, file, (1024*inodePointer)+128);

        fileMode = buffer.getShort(0);
        userID = buffer.getShort(2);
        fileSizeLower = buffer.getInt(4);
        lastAccessTime = buffer.getInt(8);
        creationTime = buffer.getInt(12);
        lastModified = buffer.getInt(16);
        deletedTime = buffer.getInt(20);
        groupID = buffer.getShort(24);
        numHardLinks = buffer.getShort(26);

        for (int i = 0; i < 12; i++) {
            pointers[i] = buffer.getInt((i*4)+40);
            System.out.println(i);
        }

        indirectPointer = buffer.getInt(90);
        doubleIndirect = buffer.getInt(94);
        tripleIndirect = buffer.getInt(98);
        fileSizeUpper = buffer.getInt(106);


        System.out.println(Arrays.toString(buffer.array()));
        //Helper.dumpHexBytes(buffer.array());
        System.out.println(Arrays.toString(pointers));
        //System.out.println(fileMode);
    }

    public short getFileMode() {
        return fileMode;
    }

    public short getUserID() {
        return userID;
    }

    public int getFileSizeLower() {
        return fileSizeLower;
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



    public int getIndirectPointer() {
        return indirectPointer;
    }

    public int getTripleIndirect() {
        return tripleIndirect;
    }

    public int getFileSizeUpper() {
        return fileSizeUpper;
    }

}
