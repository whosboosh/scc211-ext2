import java.io.IOException;

public class Driver {

    public static void main(String args[]) {
        try {
            Volume volume = new Volume("resources/ext2fs");
            System.out.println(volume.getBlockGroups()[0].getSuperblock().getSuperBlockInformation());
            Ext2File file = new Ext2File(volume,"/two-cities");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
