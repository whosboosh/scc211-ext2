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

        // Read bytes from files
        //System.out.println(read());
        //System.out.println(length());

        BlockGroup blockGroup = new BlockGroup(this);

        System.out.println(blockGroup.getSuperblock().getMagicNumber());

        System.out.println(blockGroup.getGroupDesc().getInodeTablePointer());
    }




}
