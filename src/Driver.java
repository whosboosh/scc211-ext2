import java.io.IOException;

public class Driver {

    public static void main(String args[]) {
        try {
            Volume volume = new Volume("resources/ext2fs");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
