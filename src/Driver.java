import java.io.IOException;
import java.util.Arrays;

public class Driver {

    public static void main(String args[]) {
        try {
            Volume volume = new Volume("resources/ext2fs");
            System.out.println(volume.getBlockGroups()[0].getSuperblock().getSuperBlockInformation());
            Ext2File file = new Ext2File(volume,"/deep/down/in/asd");

            file.printFullPath();

            byte[] buf = file.read(0, file.size);
            System.out.format ("%s\n", new String(buf));

            /*
            file.seek(10);
            buf = file.read(10);
            System.out.format ("%s\n", new String(buf));*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
