import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;

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

    public static ByteBuffer combinePointers(int[] pointers, RandomAccessFile file) throws IOException {
        // Combine buffer from pointers
        // Find how many pointers we have and add the data together
        int i = 0;
        while (pointers[i] != 0) {
            i++;
        }

        //This will be the combined byte array
        byte[] byteBuffer = new byte[1024*i];

        // Loop amount of pointers there are
        for (int k = 0; k < i; k++) {

            // Create buffer from first pointer
            byte[] partialData = Helper.wrap(1024, file, pointers[k] * 1024).array();

            // Loop over overall buffer, append new data
            for (int l = k * 1024; l < partialData.length + (k * 1024); l++) {

                byteBuffer[l] = partialData[l - (k * 1024)];

            }

        }

        return Helper.wrap(byteBuffer);
    }

    public static void dumpHexBytes(byte[] bytes) {
        for (int i = 0; i < bytes.length / 16; i++) {
            for (int k = 0; k < 32; k++) {

                if (k % 8 == 0 && k != 0) {
                    System.out.print(" | ");
                }

                if (k < 16) {
                    System.out.print(String.format("%02X", bytes[(i*16)+k]));
                    System.out.print(" ");
                } else {
                    // Not all bytes are ASCII characters
                    char ch = (char) bytes[(i*16)+(k-16)];
                    // ASCII characters are valid if the value is between 32 and 127
                    if (ch >= 32 && ch < 127) {
                        System.out.print(ch);
                        System.out.print(" ");
                    } else if (bytes[(i*16)+(k-16)] != 00) {
                        System.out.print(bytes[(i*16)+(k-16)]);
                        System.out.print(" ");
                    } else {
                        System.out.print("*");
                        System.out.print(" ");
                    }
                }
            }
            System.out.println();
        }
    }
    

}
