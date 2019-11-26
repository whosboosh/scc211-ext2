import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Directory {


    public Directory(int pointer, int directoryLength, RandomAccessFile file) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, pointer * 1024);

        int ptr = 0;
        // loop buffer until at end of directory
        //use .position to seek into buffer by pointer
        // increment pointer by length value
        //create data entry from buffer

        while (ptr < size) {

        }

        //System.out.println(Arrays.toString(buffer.array()));
        Helper.dumpHexBytes(buffer.array());
        //System.out.println(Arrays.toString(buffer.array()));
    }

}
