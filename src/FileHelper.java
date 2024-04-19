import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class FileHelper {
    public static int getHighScoreFromFile(String fileName) {
        File file = new File(fileName);
        
        if (!file.exists()) {
            try {
                file.createNewFile();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }
        
        try (Scanner scanner = new Scanner(file)) {
            if (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                return Integer.parseInt(line.trim());
            } else {
                return 0;
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    public static void writeLineToFile(String fileName, int highScore) {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write(highScore + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
