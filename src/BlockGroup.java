import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockGroup {
    private Superblock superblock;
    private GroupDesc groupDesc;

    public BlockGroup(byte[] bytes, RandomAccessFile file) throws IOException {
        //  Get superblock
        superblock = new Superblock(bytes, file);

        groupDesc = new GroupDesc(bytes, file);

    }

    public Superblock getSuperblock() {
        return superblock;
    }

    public GroupDesc getGroupDesc() {
        return groupDesc;
    }
}
