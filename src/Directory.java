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

        buffer = Helper.combinePointers(pointers, file);

        int i = 0;
        while (pointers[i] != 0) {
            i++;
        }

        // Loop over each pointer
        for (int k = 0; k < i; k++) {
            // Create directory entries
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
