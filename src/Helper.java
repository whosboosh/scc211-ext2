import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.ArrayList;
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

    public static ByteBuffer combinePointers(Inode inode, RandomAccessFile file) throws IOException {

        int[] pointers = inode.getPointers();
        int indirectPointer = inode.getIndirectPointer();
        int doubleIndirect = inode.getDoubleIndirect();
        int tripleIndirect = inode.getTripleIndirect();

        byte[] indirectData = getIndirectData(indirectPointer, inode, file);
        byte[] doubleIndirectData = getDblIndirectData(doubleIndirect, inode, file);
        byte[] tripleIndirectData = getTrplIndirectData(tripleIndirect, inode, file);

        // Combine buffer from pointers
        // Find how many pointers we have and add the data together
        ArrayList<Integer> pointersNotNull = new ArrayList<>();
        for (int pointer : pointers) {
            if (pointer != 0) {
                pointersNotNull.add(pointer);
            }
        }
        int i = pointersNotNull.size();

        //This will be the combined byte array
        byte[] byteBuffer = new byte[(1024*i)+indirectData.length+doubleIndirectData.length+tripleIndirectData.length];

        // Loop amount of pointers there are
        for (int k = 0; k < pointersNotNull.size(); k++) {

            // Create buffer from first pointer
            byte[] partialData = Helper.wrap(1024, file, pointersNotNull.get(k) * 1024).array();

            System.arraycopy(partialData, 0, byteBuffer, k*1024 ,partialData.length);
        }

        // Include the indirect data
        System.arraycopy(indirectData, 0, byteBuffer, 1024*i, indirectData.length);
        // Include double indirect
        System.arraycopy(doubleIndirectData, 0, byteBuffer, 1024*i+indirectData.length, doubleIndirectData.length);
        // Include triple indirect
        System.arraycopy(tripleIndirectData, 0, byteBuffer, 1024*i+indirectData.length+doubleIndirectData.length, tripleIndirectData.length);

        //System.out.println(Arrays.toString(byteBuffer));
        //Helper.dumpHexBytes(byteBuffer);
        return Helper.wrap(shortenBuffer(byteBuffer));
    }

    public static byte[] getIndirectData(int pointer, Inode inode, RandomAccessFile file) throws IOException {
        byte[] indirectData = new byte[0];
        if (pointer != 0) {
            ArrayList<Integer> buffer = reducePointers(pointer, file);
            int count = buffer.size();

            // Data to return, each block of data is 1024 bytes
            indirectData = new byte[(int)inode.getFileSize()];

            // Create the partial buffers for each pointer
            for (int k = 0; k < count; k++) {
                byte[] temp = Helper.wrap(1024, file, buffer.get(k)*1024).array();
                System.arraycopy(temp, 0, indirectData, k*1024 , temp.length);
            }

        }

        return indirectData;
    }

    public static byte[] getDblIndirectData(int pointer, Inode inode, RandomAccessFile file) throws IOException {
        byte[] indirectData = new byte[0];
        if (pointer != 0) {
            ArrayList<Integer> buffer = reducePointers(pointer, file);
            int count = buffer.size();

            indirectData = new byte[(int)inode.getFileSize()];

            for (int i = 0; i < count; i++) {
                byte[] temp = getIndirectData(buffer.get(i), inode, file);
                System.arraycopy(temp, 0, indirectData, i*1024 , temp.length);
            }

        }
        return indirectData;
    }

    public static byte[] getTrplIndirectData(int pointer, Inode inode, RandomAccessFile file) throws IOException {
        byte[] indirectData = new byte[0];
        if (pointer != 0) {
            ArrayList<Integer> buffer = reducePointers(pointer, file);
            int count = buffer.size();

            indirectData = new byte[(int)inode.getFileSize()];

            for (int i = 0; i < count; i++) {
                byte[] temp = getDblIndirectData(buffer.get(i), inode, file);
                System.arraycopy(temp, 0, indirectData, i*1024 , temp.length);
            }

        }
        return indirectData;
    }

    public static ArrayList<Integer> reducePointers(int pointer, RandomAccessFile file) throws IOException {
        ByteBuffer indirectBlock = Helper.wrap(1024, file, (pointer * 1024)); // Contains 256 4 byte pointers to data blocks

        // Count how many pointers we have, don't want an unnecessarily large array
        ArrayList<Integer> indirectNotNull = new ArrayList<>();
        for (int i = 0; i < indirectBlock.array().length; i += 4) {
            if (indirectBlock.getInt(i) != 0) indirectNotNull.add(indirectBlock.getInt(i));
        }

        return indirectNotNull;
    }

    public static byte[] shortenBuffer(byte[] buffer) {
        // Remove trailing 0's in file because the inode length isn't necessarily the actual contents length
        byte[] actualData = new byte[0];
        for (int i = buffer.length-1; i >= 0; i--) {
            if (buffer[i] != 0) {
                actualData = new byte[i+1];
                System.arraycopy(buffer, 0, actualData, 0, actualData.length);
                break;
            }
        }
        return actualData;
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
