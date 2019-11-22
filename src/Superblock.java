import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class Superblock {

    private short magicNumber;
    private int numBlocks;
    private int inodeSize;
    private ByteBuffer volumeLabel;

    public Superblock(byte[] bytes, RandomAccessFile file) throws IOException {

        file.seek(1024);
        file.read(bytes);

        ByteBuffer buffer = Helper.wrap(bytes);

        magicNumber = buffer.getShort(56);
        numBlocks = buffer.getInt(32);
        inodeSize = buffer.getInt(88);
        byte[] labelBuffer = buffer.array();
        volumeLabel = buffer.get(labelBuffer, 120, 16);
    }

    public short getMagicNumber() {
        return magicNumber;
    }
    public int getNumBlocks() {
        return numBlocks;
    }

    public int getInodeSize() {
        return inodeSize;
    }

    public ByteBuffer getVolumeLabel() {
        return volumeLabel;
    }
}
