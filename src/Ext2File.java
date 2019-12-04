import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Ext2File {

    private byte[] bytes;
    private int size;
    private int position;

    public Ext2File(Volume volume, String path) throws IOException {
        // Takes volume and the path, now recursively traverse volume to find the file

        Directory root = volume.getRoot();

        String[] paths = path.split("/");

        DirectoryEntry[] directoryEntries = root.getFileInfo();

        int currentPath = 1;
        for (int i = 1; i < paths.length; i++) {

            System.out.println(paths[currentPath]+" Directory entries: "+directoryEntries.length);

            for (int k = 0; k < directoryEntries.length; k++) {

                System.out.println(directoryEntries[k].print()+" ");

                if (directoryEntries[k].getFileName().equals(paths[currentPath])) {
                    System.out.println("Inode value: "+directoryEntries[k].getInodeValue());
                    System.out.println("Now getting new directory using data pointer: "+Arrays.toString(directoryEntries[k].getInode().getPointers()));

                    // Traverse to this directory by setting the directory entries array
                    // to the new directory
                    if (directoryEntries[k].isFileDirectory()) {
                        directoryEntries = directoryEntries[k].getDataDirectory().getFileInfo();
                        i = 1;
                    } else {
                        System.out.println(directoryEntries[k].getDataFile().getData());
                    }
                    break;
                }

            }
            currentPath++;
            System.out.println("------------------------");
        }

    }

    /*
    public byte[] read(long startByte, long length) {

    }

    public byte[] read(long length) {

    }

    public void seek(long position) {

    }

    public int position() {
        return position;
    }

    public int size() {

    }
    */
}
