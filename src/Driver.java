import java.io.IOException;

public class Driver {

    public static void main(String args[]) {
        try {
            Volume volume = new Volume("resources/ext2fs");
            Ext2File file = new Ext2File(volume,"/deep/down/in/the/filesystem/there/lived/a/file");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
