import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class Volume extends RandomAccessFile {

    public Volume(String path) throws IOException {

        super(new File(path), "r");
        this.initialise();
    }

    public void initialise() throws IOException {

        BlockGroup blockGroup = new BlockGroup(this);

        // Read root directory
        Directory rootDirectory = new Directory(blockGroup.getRootInode().getPointers()[0], blockGroup.getRootInode().getFileSize(), this, blockGroup.getSuperblock(), blockGroup.getGroupDesc());

        //System.out.println(blockGroup.getSuperblock().getMagicNumber());

    }




}
