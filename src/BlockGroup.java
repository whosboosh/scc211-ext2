import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class BlockGroup {
    private Superblock superblock;
    private GroupDesc groupDesc;

    public BlockGroup(RandomAccessFile file) throws IOException {

        superblock = new Superblock(file);

        groupDesc = new GroupDesc(file);

        // Read Inode from Inode Table using Inode Table pointer from Group Descriptor
        int inodePointer = groupDesc.getInodeTablePointer();
        int inodeSize = superblock.getInodeSize();

        System.out.println("Inode Size: "+inodeSize);

        // Seek file 5120 bytes in to access Inode Table
        ByteBuffer buffer = Helper.wrap(inodeSize, file, 5120);
        System.out.println(Arrays.toString(buffer.array()));
        //Inode inode = buffer.get

    }

    public Superblock getSuperblock() {
        return superblock;
    }

    public GroupDesc getGroupDesc() {
        return groupDesc;
    }
}
