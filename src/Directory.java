import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Directory {

    private List<DirectoryEntry> directoryEntries = new ArrayList<>();
    private ByteBuffer buffer;

    public Directory(int[] pointers, long directoryLength, RandomAccessFile file, Superblock superblock, BlockGroup[] blockGroups) throws IOException {

        // Combine buffer from pointers
        // Find how many pointers we have and add the data together
        int i = 0;
        while (pointers[i] != 0) {
            i++;
        }

        //This will be the combined byte array
        byte[] byteBuffer = new byte[1024*i];

        // Loop amount of pointers there are
        for (int k = 0; k < i; k++) {

            // Create buffer from first pointer
            byte[] partialData = Helper.wrap(1024, file, pointers[k] * 1024).array();

            // Loop over overall buffer, append new data
            for (int l = k*1024; l < partialData.length+(k*1024); l++) {

                byteBuffer[l] = partialData[l-(k*1024)];

            }

            // Create directory entries
            // directoryLength is the specified inode filesize
            // Traverse the directory file by file adding the length from each file
            // The next file starts at the end the previous file... Last file is padded with 0's
            int ptr = 0;
            while (ptr < directoryLength) {

                // Create a new buffer of size 1024 bytes, seek to the existing pointer + value of ptr
                // which is the value of length from the file (directoryEntry).
                ByteBuffer buf = Helper.wrap(1024, file, (pointers[k]*1024)+ptr);

                DirectoryEntry directoryEntry = new DirectoryEntry(buf, file, superblock, blockGroups);

                ptr+=(directoryEntry.getLength());

                directoryEntries.add(directoryEntry);
            }

        }

        buffer = Helper.wrap(byteBuffer);

    }

    public DirectoryEntry[] getFileInfo() {
        DirectoryEntry[] array = new DirectoryEntry[directoryEntries.size()];
        for (int i = 0; i < directoryEntries.size(); i++) {
            array[i] = directoryEntries.get(i);
        }

        return array;
    }

    public byte[] getBuffer() {
        return buffer.array();
    }
}
