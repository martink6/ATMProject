import java.io.IOException;

public class Utils {
    public static void clearScreen() {
        try {
            String operatingSystem = System.getProperty("os.name");

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("An error occurred while trying to clear the screen: " + e.getMessage());
        }
    }

    public static void sleep(int milliseconds)  {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("An error occurred while trying to sleep (ctrl + c pressed?): " + e.getMessage());
        }
    }

}