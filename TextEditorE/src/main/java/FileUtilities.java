import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//class for work with files
public class FileUtilities {
    //load data from a file
    public String load(String fileName, Component parent) {
        String fileContent = "";
        try {
            fileContent = Files.readString(Paths.get(fileName));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(parent, e.getMessage());
        }
        return fileContent;
    }

    //save data to a file
    public boolean save(String fileName, String fileContent, Component parent) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(fileContent);
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, e.getMessage());
            return false;
        }

    }
}