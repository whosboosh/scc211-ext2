package com.java.nathanial292;


import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Shell {

    private DirectoryEntry[] currentDirectory; // The current working directory
    private DirectoryEntry[] returnDirectory; // Return value of searching for correct directory given path
    private DirectoryEntry[] rootDirectory; // Keep reference to root to search absolute
    DirectoryEntry[] workingDirectory;

    private String currentPath;

    /**
     * Creates a shell instance.
     * @param volume
     */
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

    public void hex(String path) throws IOException {
        byte[] buffer = setWorkingDirectory(path);

        Helper.dumpHexBytes(buffer);
    }

    /**
     * Prints the contents of the buffer returned from setWorkingDirectory method
     * @param path path of the file the user is accessing
     * @throws IOException
     */
    public void cat(String path) throws IOException {

        byte[] buffer = setWorkingDirectory(path);

        // Print the buffer!
        System.out.format ("%s\n", new String(buffer));
    }

    /**
     * Moves the current directory to a new directory. Works absolute or relative
     * @param path String path - the path the user wants to go to
     * @return byte of the file at the location or length 0 if file not found
     * @throws IOException
     */
    public String cd(String path) throws IOException {


        if (setWorkingDirectory(path).length == 0) {
            if (currentPath == null) {
                return "/";
            }
            this.currentDirectory = this.returnDirectory;
        } else {
            currentPath = formatCurrentPath(splitPath(path));
        }

        this.currentDirectory = returnDirectory;

        return currentPath;
    }

    /**
     *
     * @param path String of the path the user wants to go to
     * @return List<String> of each path in the string
     */
    public List<String> splitPath(String path) {
        // Split the path
        List<String> paths = new LinkedList<>(Arrays.asList(path.split("/")));


        // Figure out if relative or absolute, relative will just use currentDirectory
        if (path.charAt(0) == '/') { // Absolute
            workingDirectory = rootDirectory;
        }
        if (path.charAt(0) == '/' && paths.size() > 1) {
            paths.remove(0);
        }


        return paths;
    }

    /**
     * Moves the currentDirectory parameter to the specified directory
     * @param path String path the user wants to go to
     * @return byte[] of the data at the path or 0 if not found or directory
     * @throws IOException
     */
    public byte[] setWorkingDirectory(String path) throws IOException {
        // The buffer the data will be saved to
        byte[] buffer = new byte[1];

        DirectoryEntry[] backup = currentDirectory;
        workingDirectory = currentDirectory;


        // Split the path
        List<String> paths = splitPath(path);

        boolean finished = false;

        // Loop over each path, find the folder matching the name and set the directory equal to the matching directory.
        for (int i = 0; i < paths.size() && !finished; i++) {

            for (int k = 0; k < workingDirectory.length; k++) {

                if (workingDirectory[k].getFileName().equals(paths.get(i))) {
                    // We found the folder / file in the directory

                    // Determine if the file is a folder or file
                    if (workingDirectory[k].isFileDirectory()) {
                        // Directory
                        workingDirectory = workingDirectory[k].getDataDirectory().getFileInfo();
                    } else {
                        // File
                        buffer = workingDirectory[k].getDataFile(false).getBuffer();
                        workingDirectory = backup;
                        // Now that buffer is filled, set the size attribute equal to length
                    }
                    break;
                }

                // If the code has gotten here we know the folder is not in the directory
                if (k == workingDirectory.length - 1) {
                    finished = true;
                    break;
                }

            }

            // Path not found
            if (finished) {
                System.out.print("Path not found: ");
                System.out.println(formatCurrentPath(paths));
                this.returnDirectory = backup;
                return new byte[0];
            }
        }

        this.returnDirectory = workingDirectory;
        return buffer;
    }


    /**
     * Add prefixing slashes to the path for shell logging
     * @param paths
     * @return String with updated absolute path
     */
    public String formatCurrentPath(List<String> paths) {

        StringBuilder builtPath = new StringBuilder();

        for (String path : paths) {
            builtPath.append("/").append(path);
        }

        return builtPath.toString();
    }



}
