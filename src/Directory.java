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

            ByteBuffer buf = Helper.wrap(1024, file, (pointer*1024)+ptr);

            //System.out.println(Arrays.toString(buf.array()));

            DirectoryEntry directoryEntry = new DirectoryEntry(buf, file, superblock, groupDesc);

            ptr+=(directoryEntry.getLength());
            //System.out.println(ptr);

            for (byte b : directoryEntry.getFileName()) {
                char ch = (char) b;

                if (ch >= 32 && ch < 127) {

                    System.out.print(ch);
                }
            }
            //System.out.println(directoryEntry.g)

            System.out.println();

            //System.out.println(Arrays.toString(directoryEntry.getFileName()));
            //System.out.println(directoryEntry.getLength());
            //Helper.dumpHexBytes(buffer.array());
            //System.out.println("-------------------------");
        }


        //System.out.println(Arrays.toString(buffer.array()));
        //Helper.dumpHexBytes(buffer.array());
        //System.out.println(Arrays.toString(buffer.array()));
    }

}
