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

    public Directory(int pointer, long directoryLength, RandomAccessFile file, Superblock superblock, GroupDesc groupDesc) throws IOException {

        // Inital buffer contains 1024 bytes, gets the location using pointer which is
        // the first value of the soecificed inode pointers byte array. Each block is 1024 bytes
        // So we can seek pointer * 1024
        buffer = Helper.wrap(1024, file, pointer * 1024);

        // directoryLength is the specified inode filesize
        // Traverse the directory file by file adding the length from each file
        // The next file starts at the end the previous file... Last file is padded with 0's
        int ptr = 0;
        while (ptr < directoryLength) {

            // Create a new buffer of size 1024 bytes, seek to the existing pointer + value of ptr
            // which is the value of length from the file (directoryEntry).
            ByteBuffer buf = Helper.wrap(1024, file, (pointer*1024)+ptr);

            DirectoryEntry directoryEntry = new DirectoryEntry(buf, file, superblock, groupDesc);

            ptr+=(directoryEntry.getLength());

            directoryEntries.add(directoryEntry);
        }

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
