import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Ext2File {

    private byte[] buffer;
    public static int size;
    public static int position;
    private String path;
    DirectoryEntry[] rootDirectoryEntries;

    public Ext2File(Volume volume, String path) throws IOException {
        // Takes volume and the path, now recursively traverse volume to find the file

        Directory root = volume.getRoot();

        this.path = path;

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
                        buffer = directoryEntries[k].getDataFile().getBuffer();
                        // Now that buffer is filled, set the size attribute equal to length
                        size = buffer.length;
                    }
                    break;
                }
            }

        }

    }

    /**
     * Prints the files and folders of each directory until reaching the specified path
     * @throws IOException
     */
    public void printFullPath() throws IOException {
        String[] temp = path.split("/");

        String[] paths = new String[temp.length+1];
        System.arraycopy(temp, 0, paths, 0, temp.length);

        DirectoryEntry[] directoryEntries = rootDirectoryEntries;
        DirectoryEntry[] previousEntries = rootDirectoryEntries;

        boolean finished = false;

        for (int i = 1; i < paths.length && !finished; i++) {

            for (int k = 0; k < directoryEntries.length; k++) {

                if (directoryEntries[k].getFileName().equals(paths[i])) {

                    if (directoryEntries[k].isFileDirectory()) {
                        previousEntries = directoryEntries;
                        directoryEntries = directoryEntries[k].getDataDirectory().getFileInfo();
                    }

                    break;
                }

                if (i != paths.length-1) {

                    // If the code has gotten here we know the folder is not in the directory
                    if (k == directoryEntries.length - 1) {
                        finished = true;
                    }
                }

            }

            if (!finished || i == paths.length-2) {
                System.out.println("---------------------");
                for (int j = 1; j < i+1; j++) {
                    if (j == 1) {
                        System.out.print("/");
                    } else if (j > 2) {
                        System.out.print("/"+paths[j-1]);
                    } else {
                        System.out.print(paths[j-1]);
                    }
                }
                System.out.println();
                System.out.println("---------------------");
                if (i == paths.length-1 && !finished || i == paths.length-2 && finished) {
                    for (DirectoryEntry directoryEntry : directoryEntries) {
                        System.out.println(directoryEntry.print());
                    }
                } else {
                    for (DirectoryEntry directoryEntry : previousEntries) {
                        System.out.println(directoryEntry.print());
                    }
                }
            }
            if (finished) {
                System.out.println("----------------------");
                System.out.print("Specified path does not exist: ");
                for (int j = 1; j < i + 1; j++) {
                    System.out.print("/" + paths[j]);
                }
                System.out.println();
                System.out.println("----------------------");
            }

        }
    }


    public byte[] read(int startByte, int length) {
        byte[] data = new byte[length-startByte];
        for (int i = startByte; i < length; i++) {
            data[i-startByte] = buffer[i];
        }

        return data;
    }


    public byte[] read(int length) {
        byte[] data = new byte[size-position];
        for(int i = position; i < length+position; i++) {
            data[i-position] = buffer[i];
        }

        return data;
    }

    public void seek(int position) {
        this.position = position;
    }
}
