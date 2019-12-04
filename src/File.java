import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class File {

    private ByteBuffer buffer;

    public File(int[] pointers, RandomAccessFile file) throws IOException {

        buffer = Helper.combinePointers(pointers, file);

    }

    // Get the data out of the buffer by creating new array based on data not being 0
    public String getData() {
        byte[] buf = buffer.array();

        if (buf.length != 0) {
            int i = 0;
            while (buf[i] != 0) {
                i++;
            }

            byte[] data = new byte[i];
            for (int k = 0; k < i; k++) {
                data[k] = buf[k];
            }
            return new String(data);
        }
        return "No data found";
    }

    public byte[] getBuffer() {
        return buffer.array();
    }

}
