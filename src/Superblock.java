import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Superblock {

    private short magicNumber;
    private int totalBlocks;
    private int numBlocksPerGroup;
    private int inodeSize;
    private int numInodesPerGroup;
    private ByteBuffer volumeLabel;

    public Superblock(RandomAccessFile file) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, 1024);

        magicNumber = buffer.getShort(56);
        totalBlocks = buffer.getInt(4);
        numBlocksPerGroup = buffer.getInt(32);
        numInodesPerGroup = buffer.getInt(40);
        inodeSize = buffer.getInt(88);
        byte[] labelBuffer = buffer.array();
        volumeLabel = buffer.get(labelBuffer, 120, 16);
    }

    public short getMagicNumber() {
        return magicNumber;
    }

    public int getTotalBlocks() { return totalBlocks; }

    public int getNumBlocksPerGroup() {
        return numBlocksPerGroup;
    }

    public int getNumInodesPerGroup() { return numInodesPerGroup; }

    public int getInodeSize() {
        return inodeSize;
    }

    public ByteBuffer getVolumeLabel() {
        return volumeLabel;
    }
}
