import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class TimeBasedMiniGame extends GameEngine {

    private MiniGameListener listener;
    Image mainSprite, circle1, circle2, circle3, side;
    double bar1angle, bar2angle, bar3angle;
    double bar1Speed, bar2Speed, bar3Speed;
    protected Timer mainGameTimer;
    boolean space, bar1Complete, bar2Complete, bar3Complete;

    public void setMiniGameListener(MiniGameListener listener) {
        this.listener = listener;
    }
    private void notifyGameComplete() {
        if (bar3Complete) {
            listener.onTimeBasedMiniGameComplete(true);
        }
    }
    public TimeBasedMiniGame(Timer gameTimerInstance) {
        this.mainGameTimer = gameTimerInstance;
        mainGameTimer.stop(); // stop the main game timer
        //mPanel.requestFocus();
    }
    public void initCircles(){
        circle1 = subImage(mainSprite,0, 0, 32, 32);
        circle2 = subImage(mainSprite, 0, 32, 32, 32);
        circle3 = subImage(mainSprite, 32, 32, 32, 32);
        bar1Speed= 60;
        bar2Speed= 90;
        bar3Speed= 120;
    }
    public void drawCircle(){
        drawImage(circle1, 200, 75, 100, 100);
        drawImage(circle2, 200, 200, 100, 100);
        drawImage(circle3, 200, 325, 100, 100);
    }
    public void drawBar1(){
        saveCurrentTransform();
        translate(250, 125);
        rotate(bar1angle);
        changeColor(Color.gray);
        drawSolidRectangle(30, 0 , 25, 10);

        restoreLastTransform();
    }
    public void updateBar1(double dt){
        bar1angle+=bar1Speed *dt;
        if(bar1angle >= 360){
            bar1angle=0;
        }
    }
    public void drawBar2(){
        saveCurrentTransform();
        translate(250, 248);
        rotate(bar2angle);
        changeColor(Color.gray);
        drawSolidRectangle(30, 0 , 25, 10);
        restoreLastTransform();
    }
    public void updateBar2(double dt){
        bar2angle+=bar2Speed*dt;
        if(bar2angle >= 360){
            bar2angle=0;
        }
    }
    public void drawBar3(){
        saveCurrentTransform();
        translate(250, 375);
        rotate(bar3angle);
        changeColor(Color.gray);
        drawSolidRectangle(30, 0 , 25, 10);
        restoreLastTransform();
    }
    public void updateBar3(double dt){
        bar3angle+=bar3Speed*dt;
        if(bar3angle >= 360){
            bar3angle=0;
        }
    }
    public void init(){
        setWindowSize(500, 500);
        mainSprite = loadImage("timerEvent.png");
        side = loadImage("Side.png");

        bar1angle=0;
        bar2angle=0;
        bar3angle=0;

        space=false;
        bar1Complete = false;
        bar2Complete = false;
        bar3Complete = false;

        initCircles();
    }
    @Override
    public void update(double dt) {
        updateBar1(dt);
        updateBar2(dt);
        updateBar3(dt);

        if(bar2Complete){
            if(space==true && (bar3angle > 355 || bar3angle < 5)){
                bar3angle=0;
                bar3Speed=0;
                bar3Complete=true;
            }
        }
        if(bar1Complete){
            if(space==true && (bar2angle > 355 || bar2angle < 5)){
                bar2Complete=true;
                bar2angle=0;
                bar2Speed=0;
            }
        }
        if(space==true && (bar1angle > 355 || bar1angle < 5)){
            bar1Complete=true;
            bar1angle=0;
            bar1Speed=0;
        }

    }
    @Override
    public void paintComponent() {
        changeBackgroundColor(black);
        clearBackground(width(), height());
        changeColor(white);
        drawText(10, 20, "Press 'Space' to line up the levers!", "SansSerif" , 20);
        drawImage(side, 65, 92, 400, 400);
        drawCircle();
        drawBar1();
        drawBar2();
        drawBar3();
        if(bar3Complete){
            changeColor(white);
            drawBoldText(75, 250, "Task Complete!", "Dialog", 50 );
            drawText(75, 300, "Press 'Space-Bar' to exit Mini Game.", 25);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = true;
        }
        if(bar3Complete){
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                timer.stop(); // stop mini game timer
                mainGameTimer.start(); // start the main game timer again
                mFrame.dispose();
                notifyGameComplete();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            space = false;
        }
    }
}
