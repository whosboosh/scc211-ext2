import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Directory {


    public Directory(int pointer, long directoryLength, RandomAccessFile file, Superblock superblock, GroupDesc groupDesc) throws IOException {

        ByteBuffer buffer = Helper.wrap(1024, file, pointer * 1024);


        // loop buffer until at end of directory
        //use .position to seek into buffer by pointer
        // increment pointer by length value
        //create data entry from buffer

        int ptr = 0;
        while (ptr < directoryLength) {
            buffer.position(ptr);

            System.out.println(Arrays.toString(buffer.array()));

            ptr+=12;
            //ptr+=(new DirectoryEntry(buffer, file, superblock, groupDesc).getLength());
            System.out.println(ptr);


            //System.out.println(Arrays.toString(directoryEntry.getFileName()));
            //System.out.println(directoryEntry.getLength());
            //Helper.dumpHexBytes(buffer.array());
            System.out.println("-------------------------");
        }


        //System.out.println(Arrays.toString(buffer.array()));
        //Helper.dumpHexBytes(buffer.array());
        //System.out.println(Arrays.toString(buffer.array()));
    }

}
