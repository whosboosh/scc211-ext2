import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Directory {

    private List<DirectoryEntry> directoryEntries = new ArrayList<>();

    public Directory(int pointer, long directoryLength, RandomAccessFile file, Superblock superblock, GroupDesc groupDesc) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, pointer * 1024);

        int ptr = 0;
        while (ptr < directoryLength) {
            buffer.position(ptr);

            ByteBuffer buf = Helper.wrap(1024, file, (pointer*1024)+ptr);

            DirectoryEntry directoryEntry = new DirectoryEntry(buf, file, superblock, groupDesc);

            ptr+=(directoryEntry.getLength());

            System.out.print(directoryEntry.getInode().getNumHardLinks() + " " + directoryEntry.getInode().getUserID() + " " + directoryEntry.getInode().getGroupID() + " " + directoryEntry.getLength() + " "+ new Date(directoryEntry.getInode().getCreationTime()) + " ");

            for (byte b : directoryEntry.getFileName()) {
                char ch = (char) b;

                System.out.print(ch);
            }
            System.out.println();

            directoryEntries.add(directoryEntry);
        }

    }

    public DirectoryEntry[] getFileInfo() {
        return (DirectoryEntry[]) directoryEntries.toArray();
    }

}
