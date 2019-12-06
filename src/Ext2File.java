import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Ext2File {

    private byte[] buffer;
    public static int size;
    public static int position;
    DirectoryEntry[] rootDirectoryEntries;

    /**
     * Create an ext2file, seeks to where the file is and creates a buffer of the data
     * @param volume Volume to read
     * @param path Path to find
     * @throws IOException
     */
    public Ext2File(Volume volume, String path) throws IOException {
        // Takes volume and the path, now recursively traverse volume to find the file

        Directory root = volume.getRoot();

        String[] paths = path.split("/");

        rootDirectoryEntries = root.getFileInfo();
        DirectoryEntry[] directoryEntries = rootDirectoryEntries;

        System.out.println("--------------\nLooking for file: "+path+"\n---------------");


        // We first have to traverse the file-system until we find the file specified.
        // Offset the index by 1 because split causes the first entry to be blank
        for (int i = 1; i < paths.length; i++) {

            for (int k = 0; k < directoryEntries.length; k++) {

                if (directoryEntries[k].getFileName().equals(paths[i])) {
                    // We found the folder / file in the directory

                    // Determine if the file is a folder or file
                    if (directoryEntries[k].isFileDirectory()) {
                        // Directory
                        directoryEntries = directoryEntries[k].getDataDirectory().getFileInfo();
                    } else {
                        // File
                        if (i == paths.length-1) { // last iteration
                            System.out.println("File found: "+ path +". Loading data into buffer");
                        }
                        buffer = directoryEntries[k].getDataFile(true).getBuffer();
                        // Now that buffer is filled, set the size attribute equal to length
                        size = buffer.length;
                    }
                    break;
                }
            }

        }

    }


    /**
     * Read buffer from specified start and length bytes
     * @param startByte int
     * @param length int
     * @return byte[] of the cut data
     */
    public byte[] read(int startByte, int length) {
        byte[] data = new byte[length-startByte];
        for (int i = startByte; i < length; i++) {
            data[i-startByte] = buffer[i];
        }

        return data;
    }


    /**
     * Read from current position and length bytes
     * @param length length of new bytearray
     * @return
     */
    public byte[] read(int length) {
        byte[] data = new byte[size-position];
        for(int i = position; i < length+position; i++) {
            data[i-position] = buffer[i];
        }

        return data;
    }

    /**
     * Moves the position of the buffer
     * @param position int position
     */
    public void seek(int position) {
        this.position = position;
    }
}
