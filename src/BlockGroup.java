import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockGroup {
    private Superblock superblock;
    private GroupDesc groupDesc;
    private Inode inode;

    public BlockGroup(RandomAccessFile file) throws IOException {

        superblock = new Superblock(file);

        groupDesc = new GroupDesc(file);

        inode = new Inode(file, superblock, groupDesc, 1);

    }

    public Superblock getSuperblock() {
        return superblock;
    }

    public GroupDesc getGroupDesc() {
        return groupDesc;
    }

    public Inode getInode() { return inode; }
}
