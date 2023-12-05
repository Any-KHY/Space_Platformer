import javax.swing.*;
import java.awt.*;

public class MainMenu extends JFrame{

    // MainMenu params
    static final protected int width = 400, height = 350;

    // Game Instance
    protected A2 game;

    // JComponents
    protected JButton startGameButton, helpButton, infoButton, exitButton;
    protected JLabel title;

    // Constructor
    MainMenu(){
        this.ButtonSetUp();
        this.setTitle("Space Platformer MainMenu");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(width,height);
        this.setLayout(null);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    // Button Layout
    private void ButtonSetUp(){
        // Title Setup
        title = new JLabel("Space Platformer");
        title.setFont(new Font("Impact", Font.PLAIN, 24));
        title.setBounds(110, 5, 200, 40);
        this.add(title);

        // Start Game Button
        startGameButton = new JButton("Start Game");
        startGameButton.setBounds(130,50,130,40);
        startGameButton.addActionListener(e -> {
            // Start Game
            game = new A2();
            game.startGame(game);
        });
        this.add(startGameButton);

        // Help Button
        helpButton = new JButton("Help");
        helpButton.setBounds(130, 100, 130, 40);
        helpButton.addActionListener(e -> {
            JFrame newFrame = new JFrame("Help");
            // Edit Panel
            JTextArea instructionText = new JTextArea("""
                    
                    --Game Instruction--
                    
                    Movement:
                    
                        SpaceKey = Jump
                            |_ SpaceKey Hit Twice = Double Jump
                    
                        LeftArrowKey = Move Left
                    
                        RightArrowKey = Move Right
                    
                        DownArrowKey = Downward Movement
                    
                    Game Objective:
                    
                        Objective is to refuel your ship by collecting all fuel canisters and completing the
                        mini games associated with each fuel canister. Player is only given five lives and needs to
                        dodge the obstacles to obtain the fuel canisters.
                                        
                    """);
            instructionText.setEditable(false);

            // JFrame Settings
            newFrame.add(instructionText);
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setResizable(false);
            newFrame.pack();
            newFrame.setVisible(true);
            newFrame.setLocationRelativeTo(null);
        });
        this.add(helpButton);

        // Info Button
        infoButton = new JButton("Info");
        infoButton.setBounds(130, 150, 130, 40);
        infoButton.addActionListener(e -> {
            JFrame newFrame = new JFrame("Info");
            // Edit Panel
            JTextArea infoText = new JTextArea("""
                    
                    --Space Platformer--
                    
                    Team: The NPC6
                    
                    Members: Greshka Lao,
                             Any Kwok,
                             Flynn Rollinson,
                             Nathan Ploos Van Amstel
                                                  
                    """);
            infoText.setEditable(false);

            // JFrame Settings
            newFrame.add(infoText);
            newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            newFrame.setResizable(false);
            newFrame.pack();
            newFrame.setVisible(true);
            newFrame.setLocationRelativeTo(null);
        });
        this.add(infoButton);

        // Exit Button
        exitButton = new JButton("Exit");
        exitButton.setBounds(130, 200, 130, 40);
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        this.add(exitButton);
    }
}
