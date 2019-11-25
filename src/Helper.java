import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Helper {

    public static ByteBuffer wrap(byte[] byteArray) {
        ByteBuffer buffer = ByteBuffer.wrap(byteArray);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer;
    }

    public static ByteBuffer wrap(int length, RandomAccessFile file, int seekLength) throws IOException {
        byte[] bytes = new byte[length];
        file.seek(seekLength);
        file.read(bytes);
        return wrap(bytes);
    }
    
    public static void dumpHexBytes(byte[] bytes) {
        int i = 0;
        for (byte b : bytes) {
            if (i % 24 == 0 ) {
                System.out.println(" ");
            }

            if (i % 8 == 0 && i % 24 != 0) {
                System.out.print(" | ");
            }

            if (i >= 16 && i <= 24) {
                char ch = (char)b;
                System.out.print(ch);
                System.out.print(" ");
            } else {
                System.out.print(String.format("%02X", b));
                System.out.print(" ");
            }
            i++;
            if (i == 24) i = 0;
        }
    }

    

}
