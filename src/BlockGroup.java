import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockGroup {
    private Superblock superblock;
    private GroupDesc groupDesc;

    public BlockGroup(RandomAccessFile file, int blockGroup) throws IOException {

        superblock = new Superblock(file);

        groupDesc = new GroupDesc(file, blockGroup);

    }

    public Superblock getSuperblock() {
        return superblock;
    }

    public GroupDesc getGroupDesc() {
        return groupDesc;
    }
}
