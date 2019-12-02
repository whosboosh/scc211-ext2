import java.io.IOException;

public class Driver {

    public static void main(String args[]) {
        try {
            Volume volume = new Volume("resources/ext2fs");
            Ext2File file = new Ext2File(volume,"/deep");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
