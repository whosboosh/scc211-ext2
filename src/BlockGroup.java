import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockGroup {
    private Superblock superblock;
    private GroupDesc groupDesc;

    public BlockGroup(RandomAccessFile file) throws IOException {

        superblock = new Superblock(file);

        groupDesc = new GroupDesc(file);

        Inode inode = new Inode(file, superblock, groupDesc);

    }

    public Superblock getSuperblock() {
        return superblock;
    }

    public GroupDesc getGroupDesc() {
        return groupDesc;
    }
}
