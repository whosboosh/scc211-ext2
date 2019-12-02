import java.io.IOException;
import java.io.RandomAccessFile;

public class BlockGroup {
    private Superblock superblock;
    private GroupDesc groupDesc;
    private Inode rootInode;

    public BlockGroup(RandomAccessFile file) throws IOException {

        superblock = new Superblock(file);

        groupDesc = new GroupDesc(file);

        rootInode = new Inode(file, superblock, groupDesc, 2);

    }

    public Superblock getSuperblock() {
        return superblock;
    }

    public GroupDesc getGroupDesc() {
        return groupDesc;
    }

    public Inode getRootInode() { return rootInode; }
}
