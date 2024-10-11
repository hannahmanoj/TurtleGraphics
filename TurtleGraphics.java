import uk.ac.leedsbeckett.oop.OOPGraphics;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TurtleGraphics extends OOPGraphics {
    public List<String> commands = new ArrayList<>();
    private final JLabel nameLabel;
    private boolean imageSaved = true;



    public static void main(String[] args) {
        new TurtleGraphics();
    }

    public TurtleGraphics() {

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JButton saveImageButton = new JButton("Save Image");
        saveImageButton.addActionListener(e -> save());

        JButton loadImageButton = new JButton("Load Image");
        loadImageButton.addActionListener(e -> load());


        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveImageButton);
        buttonPanel.add(loadImageButton);

        frame.add(buttonPanel, BorderLayout.NORTH);
        frame.add(this, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        nameLabel = new JLabel();
        frame.add(nameLabel, BorderLayout.SOUTH);
    }

    @Override
    public void processCommand(String command) {
        System.out.println("command = " + command);
        commands.add(command); // Add the command to the list

        if (command.equalsIgnoreCase("about")) {
            about();
        } else {
            String[] cmd = command.toLowerCase().trim().split("\\s++");
            switch (cmd[0]) {
                case "penup":
                    penUp();
                    break;
                case "pendown":
                    penDown();
                    break;
                case "turnright":
                    ToTurn(cmd, true);
                    break;
                case "turnleft":
                    ToTurn(cmd, false);
                    break;
                case "forward":
                    ToMoveForward(cmd, true);

                    break;
                case "backward":
                    ToMoveForward(cmd,false);
                    break;
                case "pink":
                    setPenColour(Color.PINK);
                    break;
                case "yellow":
                    setPenColour(Color.YELLOW);
                    break;
                case "red":
                    setPenColour(Color.RED);
                    break;
                case "white":
                    setPenColour(Color.WHITE);
                    break;
                case "reset":
                    reset();
                    break;
                case "clear":
                    clearImage();
                    break;
                case "square":
                    drawSquare(cmd);
                    break;
                case "pencolour":
                    setPenColour(cmd);
                    break;
                case "penwidth":
                    SetPenWidth(cmd);
                    break;
                case "triangle":
                    equilateralTriangle(cmd);
                    break;
                case "savecmd":
                    saveCommands();
                    break;
                case "loadcmd":
                    loadCommands();
                    break;
                default:
                    System.out.println("invalid command");
            }
        }

    }

    @Override
    public void about() {
        super.about();
        nameLabel.setText("Hannah Manoj");

    }

    private void ToTurn(String[] cmd, boolean isRight) {
        if (cmd.length > 1) {
            try {
                int degrees = Integer.parseInt(cmd[1]);
                if (degrees >= 0 && degrees <= 360) {
                    if (isRight) {
                        turnRight(degrees);
                    } else {
                        turnLeft(degrees);
                    }
                } else {
                    System.out.println("value must be between 0 and 360.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter an integer value between 0 and 360.");
            }
        } else {
            System.out.println("Missing degree value.");
        }
    }

    private void ToMoveForward(String[] cmd, boolean isForward) {
        if (cmd.length > 1) {
            try {
                int distance = Integer.parseInt(cmd[1]);
                if (distance >= 0) {
                    if (isForward) {
                        if (distance>1000){
                            System.out.println("Turtle will move off the screen" );
                        }else{
                            forward(distance);}
                    }else {
                        reset();
                    }
                } else {
                    System.out.println("Distance has to greater than 0 or equal to zero.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Distance has to be integer value.");
            }
        } else {
            System.out.println("Missing distance value.");
        }
    }



    public void save() {

        String format = "PNG";
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Image");
        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try {
                BufferedImage image = getBufferedImage();
                ImageIO.write(image, format, fileToSave);
                System.out.println("Image saved.");
            } catch (IOException e) {
                System.out.println("Error: Cannot save image: " + e.getMessage());
            }
        }
    }



    public void load() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Image");
        int UserChoice = fileChooser.showOpenDialog(null);
        if (UserChoice == JFileChooser.APPROVE_OPTION) {
            File fileLoad = fileChooser.getSelectedFile();
            try {
                BufferedImage image = ImageIO.read(fileLoad);
                setBufferedImage(image);
                System.out.println("Image Loaded. ");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void saveCommands() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Commands");
        int userChoice = fileChooser.showSaveDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (PrintWriter writer = new PrintWriter(fileToSave)) {
                for (String command : commands) {
                    writer.println(command);
                }
                System.out.println("Commands saved.");
            } catch (FileNotFoundException e) {
                System.out.println("Error: Cannot save commands: " + e.getMessage());
            }
        }
    }
    public void loadCommands() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Load Commands");
        int userChoice = fileChooser.showOpenDialog(null);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File fileToLoad = fileChooser.getSelectedFile();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileToLoad))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    processCommand(line); // Process each command from the file
                }
                System.out.println("Commands loaded.");
            } catch (IOException e) {
                System.out.println("Error: Failed to load commands: " + e.getMessage());
            }
        }
    }
    public void clearImage() {
        if (!imageSaved) {
            int option = JOptionPane.showConfirmDialog(null,
                    "The current image is not saved. Do you want to continue and lose changes?",
                    "Warning",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.NO_OPTION) {
                return;
            }
        }
        clear();
        imageSaved = true;
    }



    private void drawSquare(String[] cmd) {
        if (cmd.length > 1) {
            try {
                int length = Integer.parseInt(cmd[1]);
                if (length > 0) {
                    penDown();
                    for (int i = 0; i < 4; i++) {
                        forward(length);
                        turnRight(90);
                    }
                    penUp();
                } else {
                    System.out.println("Length must be greater than 0.");
                }

            } catch (NumberFormatException e) {
                System.out.println("please enter a valid integer.");
            }
        } else {
            System.out.println("Missing length value.");
        }
    }
    private void setPenColour(String[] cmd) {
        if (cmd.length == 4) {
            try {
                int red = Integer.parseInt(cmd[1]);
                int green = Integer.parseInt(cmd[2]);
                int blue = Integer.parseInt(cmd[3]);
                if (red >= 0 && red <= 255 && green >= 0 && green <= 255 && blue >= 0 && blue <= 255) {
                    setPenColour(new Color(red, green, blue));
                } else {
                    System.out.println("RGB values must be between 0 and 255.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter valid integer values for RGB.");
            }
        } else {
            System.out.println("Incorrect number of parameters for pencolour command.");
        }
    }
    public void SetPenWidth(String[] cmd){
        if (cmd.length==2){
            try{
                int width = Integer.parseInt(cmd[1]);
                if (width>0){
                    setStroke(width);
                    System.out.println("pen width set to "+width);

                }else{
                    System.out.println("Pen width must be greater than 0.");
                }
            }catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric value for pen width.");}

        }
        else {
            System.out.println("Incorrect number of parameters for penwidth command.");
        }
    }

    public void drawEquilateralTriangle(int size) {

        penDown();
        forward(size);
        turnRight(120);
        forward(size);
        turnRight(120);
        forward(size);
        turnRight(120);
        penUp();
    }

    private void customTriangle(int s1, int s2, int s3) {
        int angle1 = (int)Math.toDegrees(Math.acos((s2 * s2 + s3 * s3 - s1 * s1) / (2.0 * s2 * s3)));
        int angle2 = (int)Math.toDegrees(Math.acos((s1 * s1 + s3 * s3 - s2 * s2) / (2.0 * s1 * s3)));
        int angle3 =  180 - angle1 - angle2;

        penDown();
        forward(s1);
        turnLeft(180 - angle3);
        forward(s2);
        turnLeft(180 - angle1);
        forward(s3);
        penUp();
    }

    public void equilateralTriangle(String[] cmd) {
        if (cmd.length == 2) {
            try {
                int size = Integer.parseInt(cmd[1]);
                if (size > 0) {
                    drawEquilateralTriangle(size);
                    System.out.println("Equilateral triangle of size " + size + " drawn.");
                } else {
                    System.out.println("Triangle size must be greater than 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid numeric value for triangle size.");
            }
        } else if (cmd.length == 4){
            int s1 = Integer.parseInt(cmd[1]);
            int s2 = Integer.parseInt(cmd[2]);
            int s3 = Integer.parseInt(cmd[3]);

            if (s1 > 0 && s2 > 0 && s3 > 0){
                customTriangle(s1,s2,s3);

                System.out.println("triangle of sides "+s1+" " +s2+" "+s3+" has been drawn.");

            }else{
                System.out.println("Triangle size must be greater than 0.");
            }
        }
    }
}



