package com.java.nathanial292;


import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class File {

    private ByteBuffer buffer;

    /**
     * Create a file
     * @param inode inode for file,
     * @param file RandomAccessFile - fileystem to be read
     * @throws IOException
     */
    public File(Inode inode, RandomAccessFile file) throws IOException {

        buffer = Helper.combinePointers(inode, file);

    }

    public byte[] getBuffer() {
        return buffer.array();
    }

}
