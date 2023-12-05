import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PauseMenu extends JFrame{

    // JComponents
    protected JButton resumeButton, exitButton;
    protected JLabel frameTitle;
    private MiniGameListener listener;

    // Vars
    protected int width = 400, height = 250;
    protected Timer timerInstance;
    public void setMenuOpenedListener(MiniGameListener listener) {
        this.listener = listener;
    }

    private void notifyMenuClosed() {
        listener.onPauseMenu();
    }

    PauseMenu(Timer timer){
        timer.stop(); // stop game timer to pause
        timerInstance = timer;
        this.buttonSetup();
        this.setTitle("Pause Menu");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setResizable(false);
        this.setSize(width,height);
        this.setLayout(null);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
    }

    protected void buttonSetup(){
        frameTitle = new JLabel("Game Paused");
        frameTitle.setFont(new Font("Impact", Font.PLAIN, 24));
        frameTitle.setBounds(110, 5, 200, 40);
        this.add(frameTitle);

        // Resume Button
        resumeButton = new JButton("Resume Game");
        resumeButton.setBounds(130,50,130,40);
        resumeButton.addActionListener(e -> {
            // Resume game
            timerInstance.start();

            //notify game
            notifyMenuClosed();

            // Destruct Window
            this.dispose();
        });
        this.add(resumeButton);

        // Exit Game Button
        exitButton = new JButton("Exit Game");
        exitButton.setBounds(130,100,130,40);
        exitButton.addActionListener(e -> {
            System.exit(0);
        });
        this.add(exitButton);
    }
}
