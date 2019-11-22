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
        System.out.println(read());
        System.out.println(length());

        BlockGroup blockGroup = new BlockGroup(new byte[4096], this);

        System.out.println(blockGroup.getSuperblock().getMagicNumber());

        System.out.println(blockGroup.getGroupDesc().getInodeTablePointer());

        /*
        byte[] bytes = new byte[1024];
        seek(1024);
        read(bytes);
        //System.out.println(Arrays.toString(bytes));

        ByteBuffer superBlock = Helper.wrap(bytes);
        System.out.println(superBlock.getShort(56));
        */
    }




}
