import java.nio.ByteBuffer;

public class Superblock {

    private ByteBuffer buffer;

    public Superblock(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return this.buffer;
    }
}
