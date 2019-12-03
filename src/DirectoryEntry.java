import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Date;

public class DirectoryEntry {

    private RandomAccessFile file;
    private Inode inode;
    private short length;
    private byte nameLen;
    private byte fileType;
    private byte[] fileName;
    private Superblock superblock;
    private BlockGroup[] blockGroups;
    private ByteBuffer buffer;

    public DirectoryEntry(ByteBuffer buffer, RandomAccessFile file, Superblock superblock, BlockGroup[] blockGroups) throws IOException {

        int index = (buffer.getInt(0) - 1) / superblock.getNumInodesPerGroup();

        System.out.println("Inode pointer: "+buffer.getInt(0)+ ", Index: "+index);

        // See specification of directory for clarity on chosen byte values
        inode = new Inode(file, superblock, blockGroups[index].getGroupDesc(), buffer.getInt(0));
        length  = buffer.getShort(4);
        nameLen = buffer.get(6);
        fileType = buffer.get(7);
        fileName = new byte[nameLen];
        for (int i = 8; i < nameLen + 8; i++) {
            fileName[i-8] = buffer.get(i);
        }

        this.buffer = buffer;
        this.file = file;
        this.superblock = superblock;
        this.blockGroups = blockGroups;
    }

    public Directory getDataBlock() throws IOException {
        return new Directory(inode.getPointers(), inode.getFileSize(), file, superblock, blockGroups);
    }

    public String print() {
        String outputString;
        outputString = getInode().getNumHardLinks() + " " + getInode().getUserID() + " " + getInode().getGroupID() + " " + getLength() + " "+ new Date(getInode().getCreationTime()) + " ";

        outputString = outputString + getFileName();
        return outputString;
    }

    public int getInodeValue() {
        return buffer.getInt(0);
    }

    public short getLength() {
        return length;
    }

    public Inode getInode() {
        return inode;
    }

    public String getFileName() {
        return new String(fileName);

    }

    public byte getFileType() {
        return fileType;
    }

    public byte getNameLen() {
        return nameLen;
    }

    public byte[] getBuffer() {
        return buffer.array();
    }


}
