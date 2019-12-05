import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Superblock {

    private short magicNumber;
    private int totalInodes;
    private int totalBlocks;
    private int numBlocksPerGroup;
    private int inodeSize;
    private int numInodesPerGroup;
    private byte[] volumeLabel = new byte[16];

    public Superblock(RandomAccessFile file) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, 1024);

        magicNumber = buffer.getShort(56);
        totalInodes = buffer.getInt(0);
        totalBlocks = buffer.getInt(4);
        numBlocksPerGroup = buffer.getInt(32);
        numInodesPerGroup = buffer.getInt(40);
        inodeSize = buffer.getInt(88);

        for (int i = 0; i < 16; i++) {
            volumeLabel[i] = buffer.get(i+120);
        }

    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public int getTotalInodes() { return totalInodes; }

    public int getTotalBlocks() { return totalBlocks; }

    public int getNumBlocksPerGroup() {
        return numBlocksPerGroup;
    }

    public int getNumInodesPerGroup() { return numInodesPerGroup; }

    public int getInodeSize() {
        return inodeSize;
    }

    public String getVolumeLabel() {
        return new String(volumeLabel);
    }

    public String getSuperBlockInformation() {
        return ("---------------------\nSUPERBLOCK INFORMATION\nMagic Number: "+getMagicNumber()+"\nTotal Inodes: "+getTotalInodes()+"\nTotal Blocks: "+getTotalBlocks()
                +"\nNumber blocks per Group: "+getNumBlocksPerGroup()+"\nNumber inodes per Group: "+getNumInodesPerGroup()
                +"\nSize of inode: "+getInodeSize()+"\nVolume Label: "+getVolumeLabel());
    }
}
