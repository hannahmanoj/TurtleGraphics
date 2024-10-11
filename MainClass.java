import java.awt.FlowLayout;
import javax.swing.JFrame;
import uk.ac.leedsbeckett.oop.OOPGraphics;

public class MainClass extends OOPGraphics {
    public static void main(String[] args) {
        new MainClass(); // Create instance of class that extends OOPGraphics
    }

    public MainClass() {
        JFrame mainFrame = new JFrame(); // Create a frame to display the turtle panel on
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Make sure the app exits when closed
        mainFrame.setLayout(new FlowLayout()); // Not strictly necessary
        mainFrame.add(this); // Add the OOPGraphics panel to the frame
        mainFrame.pack(); // Set the frame to a size we can see
        mainFrame.setVisible(true); // Now display it
        about(); // Call the OOPGraphics about method to display version information
        new TurtleGraphics();
    }

    public void processCommand(String command) {
        // This method must be provided because OOPGraphics will call it when its JTextField is used
        // String parameter is the text typed into the OOPGraphics JTextField
        // lands here if return was pressed or "ok" JButton clicked
        // TO DO
    }
}
