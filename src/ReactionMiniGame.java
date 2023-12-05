import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

public class ReactionMiniGame extends JPanel implements ActionListener{
    //
    private MiniGameListener listener;

    // Timers
    protected Timer timerInstance;
    protected Timer miniTimer;

    // Display
    protected int DIS_WIDTH = 400;
    protected int DIS_HEIGHT = 400;

    // Circle vars
    protected int radius = 50;
    protected int maxRadius = 150;
    protected int circleX, circleY;

    // Game Vars
    protected boolean running;
    protected int DELAY = 1;
    protected int score = 0;
    protected boolean didWin;
    public int mouseX, mouseY;
    protected Random random;
    protected JFrame frame;

    public void setMiniGameListener(MiniGameListener listener) {
        this.listener = listener;
    }

    private void notifyGameComplete() {
        if (score == 15) {
            listener.onReactionMiniGameComplete(true);
        }
    }

    ReactionMiniGame(JFrame current, Timer gameTimerInstance){
        gameTimerInstance.stop(); // stop the main game timer
        this.timerInstance = gameTimerInstance;
        this.frame = current;
        this.random = new Random();
        this.setPreferredSize(new Dimension(DIS_WIDTH, DIS_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addMouseListener(new MyMouseListener());
        startGame();
    }
    public void startGame(){
        newCircle();
        running = true;
        miniTimer = new Timer(DELAY, this);
        miniTimer.start();
    }
    public void newCircle(){
        mouseX = 0;
        mouseY = 0;
        radius = 50;
        circleX = random.nextInt(radius,DIS_WIDTH-radius);
        circleY = random.nextInt(radius,DIS_HEIGHT-radius);
    }
    public void checkCircle() throws InterruptedException {
        int dx = mouseX - circleX;
        int dy = mouseY - circleY;
        int distance = (int) Math.sqrt(dx*dx + dy*dy);

        if(distance <= radius){
            newCircle();
            score++;
        }

        // Mini Game Over
        if(radius == maxRadius){
            didWin = false;
            Thread.sleep(3000);
            miniTimer.stop();
            listener.onReactionMiniGameComplete(false);
            frame.dispose();
            timerInstance.start(); // start the main game timer again
        }

        // Mini Game Won
        if(score == 15){
            didWin = true;
            miniTimer.stop();
            frame.dispose();
            Thread.sleep(1000);
            notifyGameComplete();
            timerInstance.start(); // start the main game timer again
        }
        radius++;
    }
    public void draw(Graphics graphics){
        if(running) {
            // draw circles
            graphics.setColor(Color.GREEN);
            if(radius >= 148){
                graphics.setColor(Color.red);
            }
            graphics.drawOval(circleX, circleY, radius, radius);
            graphics.fillOval(circleX, circleY, radius, radius);

            // draw score
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Impact", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + score, (DIS_WIDTH - metrics.stringWidth("Score: " + score))/2, graphics.getFont().getSize());

            graphics.setColor(Color.red);
            graphics.setFont(new Font("Impact", Font.BOLD, 20));
            FontMetrics metrics2 = getFontMetrics(graphics.getFont());
            graphics.drawString("Click Circles Before They Turn Red", (DIS_WIDTH - metrics2.stringWidth("Click Circles Before They Turn Red"))/2, graphics.getFont().getSize()+50);
        }
    }

    public boolean getDidWin(){
        return score == 15;
    }

    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        draw(graphics);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running){
            try {
                checkCircle();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        repaint();
    }
    public class MyMouseListener implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }
}
