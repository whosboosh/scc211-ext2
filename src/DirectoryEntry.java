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

    /**
     * Create a Directory Entry
     * @param buffer Buffer for the entry
     * @param file System File to be read
     * @param superblock Reference to SuperBlock
     * @param blockGroups Reference to all Block Groups so we can find what one to use later on using inode of entry
     * @throws IOException
     */
    public DirectoryEntry(ByteBuffer buffer, RandomAccessFile file, Superblock superblock, BlockGroup[] blockGroups) throws IOException {

        // Need to find what index in blockGroups array the inode is in.
        int index = (buffer.getInt(0) - 1) / superblock.getNumInodesPerGroup();

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

    /**
     * Returns a new directory for the entry
     * @return Directory
     * @throws IOException
     */
    public Directory getDataDirectory() throws IOException {
        return new Directory(inode, file, superblock, blockGroups);
    }

    /**
     * Returns a new File object for the entry
     * @param logging
     * @return
     * @throws IOException
     */
    public File getDataFile(boolean logging) throws IOException {
        if (logging) {
            int index = (buffer.getInt(0) - 1) / superblock.getNumInodesPerGroup();
            System.out.println(blockGroups[index].getGroupDesc().getGroupDescriptorInformation());
        }
        return new File(inode, file);
    }

    /**
     * Checks if the entry is a directory or file
     * @return
     */
    public boolean isFileDirectory() {
        return (inode.getFileMode() & 0x4000) > 0;
    }

    /**
     * ls equivelant command on directoryEntry.
     * @return String of the ls row
     */
    public String print() {

        boolean directory = (inode.getFileMode() & 0x4000) > 0;
        boolean file = (inode.getFileMode() & 0x8000) > 0;
        boolean userRead = (inode.getFileMode() & 0x0100) > 0;
        boolean userWrite = (inode.getFileMode() & 0x0080) > 0;
        boolean userExecute = (inode.getFileMode() & 0x0040) > 0;
        boolean groupRead = (inode.getFileMode() & 0x0020) > 0;
        boolean groupWrite = (inode.getFileMode() & 0x0010) > 0;
        boolean groupExecute = (inode.getFileMode() & 0x0008) > 0;
        boolean otherRead = (inode.getFileMode() & 0x0004) > 0;
        boolean otherWrite = (inode.getFileMode() & 0x0002) > 0;
        boolean otherExecute = (inode.getFileMode() & 0x0001) > 0;

        String permissions = directory ? "d" : "-";
        permissions+= userRead ? "r" : "-";
        permissions+= userWrite ? "w" : "-";
        permissions+= userExecute ? "x" : "-";
        permissions+= groupRead ? "r" : "-";
        permissions+= groupWrite ? "w" : "-";
        permissions+= groupExecute ? "x" : "-";
        permissions+= otherRead ? "r" : "-";
        permissions+= otherWrite ? "w" : "-";
        permissions+= otherExecute ? "x " : "- ";

        String outputString;
        outputString = permissions + getInode().getNumHardLinks() + " " + getInode().getUserID() + " " + getInode().getGroupID() + " " + getLength() + " "+ new Date(getInode().getCreationTime()) + " ";

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
