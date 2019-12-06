import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Shell {

    private DirectoryEntry[] currentDirectory;
    private DirectoryEntry[] rootDirectory;
    private String currentPath;

    public Shell(Volume volume) {
        // To start with, we need to set currentDirectory equal to root
        rootDirectory = volume.getRoot().getFileInfo();   // Get root directory
        currentDirectory = rootDirectory;
    }

    /**
     * Print the current working directory
     */
    public void ls() {
        for (DirectoryEntry directoryEntry : currentDirectory) {
            System.out.println(directoryEntry.print());
        }
    }

    public void cat(String path) throws IOException {

        byte[] buffer = setWorkingDirectory(path);

        // Print the buffer!
        System.out.format ("%s\n", new String(buffer));
    }

    public String cd(String path) throws IOException {


        if (setWorkingDirectory(path).length == 0) {
            if (currentPath == null) {
                return "/";
            }
        } else {
            currentPath = formatCurrentPath(splitPath(path));
        }

        return currentPath;
    }

    public List<String> splitPath(String path) {
        // Split the path
        List<String> paths = new LinkedList<>(Arrays.asList(path.split("/")));


        // Figure out if relative or absolute, relative will just use currentDirectory
        if (path.charAt(0) == '/') { // Absolute
            currentDirectory = rootDirectory;
        }
        if (path.charAt(0) == '/' && paths.size() > 1) {
            paths.remove(0);
        }

        return paths;
    }

    public byte[] setWorkingDirectory(String path) throws IOException {
        // The buffer the data will be saved to
        byte[] buffer = new byte[1];

        DirectoryEntry[] backup = currentDirectory;

        // Split the path
        List<String> paths = splitPath(path);

        boolean finished = false;

        // Loop over each path, find the folder matching the name and set the directory equal to the matching directory.
        for (int i = 0; i < paths.size() && !finished; i++) {

            for (int k = 0; k < currentDirectory.length; k++) {

                if (currentDirectory[k].getFileName().equals(paths.get(i))) {
                    // We found the folder / file in the directory

                    // Determine if the file is a folder or file
                    if (currentDirectory[k].isFileDirectory()) {
                        // Directory
                        currentDirectory = currentDirectory[k].getDataDirectory().getFileInfo();
                    } else {
                        // File
                        buffer = currentDirectory[k].getDataFile().getBuffer();
                        currentDirectory = backup;
                        // Now that buffer is filled, set the size attribute equal to length
                    }
                    break;
                }

                // If the code has gotten here we know the folder is not in the directory
                if (k == currentDirectory.length - 1) {
                    finished = true;
                    break;
                }

            }

            if (finished) {
                System.out.print("Path not found: ");
                System.out.println(formatCurrentPath(paths));
                currentDirectory = backup;
                return new byte[0];
            }
        }

        return buffer;
    }

    public String formatCurrentPath(List<String> paths) {

        StringBuilder builtPath = new StringBuilder();

        for (String path : paths) {
            builtPath.append("/").append(path);
        }

        return builtPath.toString();
    }



}
