import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Directory {
    public Directory(int pointer, RandomAccessFile file) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, pointer * 1024);
        //System.out.println(Arrays.toString(buffer.array()));
        Helper.dumpHexBytes(buffer.array());
        //System.out.println(Arrays.toString(buffer.array()));
    }
}
