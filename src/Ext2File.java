import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Ext2File {

    private byte[] buffer;
    public static int size;
    public static int position;

    public Ext2File(Volume volume, String path) throws IOException {
        // Takes volume and the path, now recursively traverse volume to find the file

        Directory root = volume.getRoot();

        String[] paths = path.split("/");

        DirectoryEntry[] directoryEntries = root.getFileInfo();

        System.out.println("-------------- Looking for file: "+path+" --------------");

        // Create the buffer from the size of the file.
        // We first have to traverse the file-system until we find the file specified.

        // Offset the index by 1 because split causes the first entry to be blank
        for (int i = 1; i < paths.length; i++) { // Two loops for /big-dir/55

            for (int k = 0; k < directoryEntries.length; k++) {

                //System.out.println(directoryEntries[k].print());

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
                        buffer = directoryEntries[k].getDataFile().getBuffer();
                        // Now that buffer is filled, set the size attribute equal to length
                        size = buffer.length;
                    }
                    break;
                }
            }

        }

    }


    public byte[] read(long startByte, long length) {
        byte[] data = new byte[(int)length-(int)startByte];
        for (long i = startByte; i < length; i++) {
            data[(int)i-(int)startByte] = buffer[(int)i];
        }

        return data;
    }


    public byte[] read(long length) {
        byte[] data = new byte[size-position];
        for(int i = position; i < length+position; i++) {
            data[i-position] = buffer[i];
        }

        return data;
    }

    public void seek(long position) {
        this.position = (int)position;
    }
}
