import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Volume extends RandomAccessFile {

    private Directory root;

    public Volume(String path) throws IOException {

        super(new File(path), "r");
        this.initialise();
    }

    public void initialise() throws IOException {

        BlockGroup blockGroup = new BlockGroup(this);

        // Read root directory
        root = new Directory(blockGroup.getRootInode().getPointers(), blockGroup.getRootInode().getFileSize(), this, blockGroup.getSuperblock(), blockGroup.getGroupDesc());
    }

    public Directory getRoot() {
        return root;
    }




}
