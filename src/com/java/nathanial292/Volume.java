package com.java.nathanial292;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Volume extends RandomAccessFile {

    private Inode rootInode;
    private Directory root;
    private BlockGroup[] blockGroups;

    /**
     * Creates a volume
     * @param path Path of the ext2-filesystem
     * @throws IOException
     */
    public Volume(String path) throws IOException {

        // Make the file readable
        super(new File(path), "r");
        this.initialise();
    }

    public void initialise() throws IOException {

        // Create block group 0
        BlockGroup blockGroup = new BlockGroup(this, 0);

        // Get total number of blocks
        int totalBlocks = blockGroup.getSuperblock().getTotalBlocks();

        // Initialse the blockgroups array with total size
        blockGroups = new BlockGroup[totalBlocks];

        // Create block groups
        int i = 0;
        while (i < totalBlocks) {
            blockGroups[i] = new BlockGroup(this, i);
            if (i < 3)
            System.out.println(blockGroups[i].getGroupDesc().getGroupDescriptorInformation());
            i++;
        }

        // Create root inode
        rootInode = new Inode(this, blockGroup.getSuperblock(), blockGroup.getGroupDesc(), 2);

        // Read root directory
        root = new Directory(rootInode, this, blockGroup.getSuperblock(), blockGroups);
    }

    public BlockGroup[] getBlockGroups() { return blockGroups; }

    public Inode getRootInode() { return rootInode; }

    public Directory getRoot() {
        return root;
    }




}
