package com.java.nathanial292;

import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Driver {

    public static void main(String args[]) {
        try {
            Volume volume = new Volume("resources/ext2fs");
            System.out.println(volume.getBlockGroups()[0].getSuperblock().getSuperBlockInformation());


            Ext2File file = new Ext2File(volume,"/files/dbl-ind-s");

            byte[] buf = file.read(0, file.size);

            Helper.dumpHexBytes(buf);

            System.out.format ("%s\n", new String(buf));

            Shell shell = new Shell(volume);
            Scanner scanner = new Scanner(System.in);

            // Loop this for continuous user input
            System.out.println("Welcome to SCC 211 Ext2 File System Driver Shell, type 'exit' to close");
            String path = "";
            while (true) {
                String shellEntry = ":"+path+"/$";
                System.out.print(volume.getBlockGroups()[0].getSuperblock().getVolumeLabel()+shellEntry);
                String input = scanner.nextLine();

                String[] commands = input.split(" ");

                if (commands[0].equals("cd")) {
                    if (commands.length != 2) continue;
                    path = shell.cd(commands[1]);
                }

                if (commands[0].equals("ls")) {
                    shell.ls();
                }

                if (commands[0].equals("cat")) {
                    if (commands.length != 2) continue;
                    shell.cat(commands[1]);
                }

                if (input.equals("exit")) {
                    break;
                }
            }


            /*
            file.seek(10);
            buf = file.read(10);
            System.out.format ("%s\n", new String(buf));*/

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
