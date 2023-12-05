import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// TEAM The NPC6
// Greshka Lao 22012395
// Any KWOK Hoi Yi 22000531
// Nathan Ploos Van Amstel 20010307
// Fynn Rollinson 19031106

public class A2 extends GameEngine implements MiniGameListener{

    // Main Method
    public static void main(String[] args) {
        // init menu
        new MainMenu();

        // init Game soundtrack
        try{
            gameAudio.backGroundMusic();
        } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    // Game Audio
    public static Audio gameAudio = new Audio();
    // Fuel Amount Collected
    protected int fuelCollected = 0;
    private final int maxFuel = 2;

    // Start Game Function For Menu
    public void startGame(A2 gameInstance){
        createGame(gameInstance, 60);
    }

    // Start Reaction Mini Game
    public void startGameReactionMiniGame(){
        try{
            gameAudio.objectCollectionSound();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        if(!inMiniGame) {
            inMiniGame = true;
            JFrame miniGameFrame = new JFrame("MiniGame");
            ReactionMiniGame miniGame = new ReactionMiniGame(miniGameFrame, timer);
            miniGame.setMiniGameListener(this);
            miniGameFrame.add(miniGame);
            miniGameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            miniGameFrame.setResizable(false);
            miniGameFrame.pack();
            miniGameFrame.setVisible(true);
            miniGameFrame.setLocationRelativeTo(null);
        }
    }
    public void startMiniGameTWO(){
        try{
            gameAudio.objectCollectionSound();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
        if(!inMiniGame) {
            inMiniGame = true;
            TimeBasedMiniGame game = new TimeBasedMiniGame(timer);
            createGame(game);
            game.setMiniGameListener(this);
        }
    }

    //-------------------------------------------------------
    // Level
    //-------------------------------------------------------
    public static final int windowWidth = 1280, windowHeight = 640, gridWidth = 32, gridHeight = 32;
    public static int gameWidth = windowWidth*2, gameHeight = windowHeight;
    public static BufferedImage loadImage(String filename) {
        try {
            // load image
            // return image
            return ImageIO.read(new File(filename));
        } catch (IOException e) {
            // error Message
            System.out.println("Error: could not load image " + filename);
            System.exit(1);
        }
        return null;
    }
    private ArrayList<Rectangle> lvlObjects; // for Collision
    private ArrayList<Rectangle> endGameObjects;
    private ArrayList<Rectangle> damageObjects;
    private ArrayList<Rectangle> miniGames;
    private ArrayList<Integer>[][] lvlMap;
    // Image
    BufferedImage levelLayout = loadImage("level.png");
    Image textureSpriteSheet = loadImage("spritesheet_ground.png");
    Image textureSpriteSheet_Lava = loadImage("Textures.png");
    Image planets = loadImage("pngwing.com.png");
    Image starField =  loadImage("starfield.png");
    int lvlWidth = levelLayout.getWidth();
    int lvlHeight = levelLayout.getHeight();
    public void levelInit(){
        // ========== Layout Implement
        lvlMap = new ArrayList[lvlWidth][lvlHeight];
        lvlObjects = new ArrayList<>();
        endGameObjects = new ArrayList<>();
        damageObjects = new ArrayList<>();
        miniGames = new ArrayList<>();

        for (int imgX=0 ; imgX<levelLayout.getWidth() ; imgX++) {
            for (int imgY=0; imgY<levelLayout.getHeight() ; imgY++) {
                Color c = new Color(levelLayout.getRGB(imgX, imgY));
                int temp = c.getRed();
                lvlMap[imgX][imgY] = new ArrayList<>();
                lvlMap[imgX][imgY].add(temp);
            }
        }
    }

    // Drawing the level
    int sourceWidth = 128, sourceHeight = 128;
    int scaleW = sourceWidth/ gridWidth, scaleH = sourceHeight/ gridHeight;
    Image groundImg = subImage(textureSpriteSheet,3*sourceWidth,4*sourceHeight, gridWidth*scaleW, gridHeight*scaleH);
    Image lavaImg = subImage(textureSpriteSheet_Lava,16*16,9*16, 16, 16);
    Image plantFormImg = subImage(textureSpriteSheet,2*sourceWidth,10*sourceHeight, gridWidth*scaleW, gridHeight*scaleH);
    Image planetImg = subImage(planets,128*2, 128,128,128);
    Image fuelsImg = loadImage("fuel_station.png");
    Image rocketShip =  loadImage("redRocketShip.png");
    Rectangle rocket;
    public void drawLvl(ArrayList<Integer>[][] lvlMap){

        damageObjects.clear();
        for (int indeX = 0; indeX< lvlWidth; indeX++) {
            for (int indeY = 0; indeY < lvlHeight; indeY++) {

                // Planet (R=113)
                if(lvlMap[indeX][indeY].contains(113)) {
                    drawImage(planetImg,indeX* gridWidth, indeY* gridHeight, 128, 128);
                }

                // Ground (R=0)
                if(lvlMap[indeX][indeY].contains(0)) {
                    drawImage(groundImg,indeX* gridWidth, indeY* gridHeight, gridWidth, gridHeight);
                    lvlObjects.add(new Rectangle(indeX*gridWidth, indeY*gridHeight, gridWidth, gridHeight));
                }

                // plantForm (R=136)
                if(lvlMap[indeX][indeY].contains(136)) {
                    drawImage(plantFormImg,indeX*gridWidth, indeY*gridHeight,gridWidth,gridHeight);
                    lvlObjects.add(new Rectangle(indeX*gridWidth, indeY*gridHeight, gridWidth, gridHeight));
                }

                // Lava //red (R=237)
                if(lvlMap[indeX][indeY].contains(237)) {
                    drawImage(lavaImg,indeX*gridWidth, indeY*gridHeight,gridWidth,gridHeight);
                    endGameObjects.add(new Rectangle(indeX*gridWidth, indeY*gridHeight, gridWidth, gridHeight));
                }

                // Movable Lava (R=165)
                if (lvlMap[indeX][indeY].contains(165)) {
                    double lavaY = indeY * gridHeight + lavaOffset();
                    drawImage(lavaImg, indeX*gridWidth, lavaY, gridWidth, gridHeight);
                    damageObjects.add(new Rectangle(indeX*gridWidth, (int) lavaY, gridWidth, gridHeight));
                }

                // miniGame1 //red (R=34)
                if(lvlMap[indeX][indeY].contains(34)) {
                    drawImage(fuelsImg,indeX*gridWidth, indeY*gridHeight+13, 100, 130);
                    // debugs use
                    // changeColor(orange);
                    // drawRectangle(indeX*gridWidth+25, indeY*gridHeight+27, 61, 100);
                    // debugs use
                    miniGames.add(new Rectangle(indeX*gridWidth+25, indeY*gridHeight+27, 61, 100));
                }

                // miniGame2 //red (R=253)
                if(lvlMap[indeX][indeY].contains(253)) {
                    drawImage(fuelsImg,indeX*gridWidth, indeY*gridHeight+13, 100, 130);
                    // debugs use
                    // changeColor(orange);
                    // drawRectangle(indeX*gridWidth+25, indeY*gridHeight+27, 61, 100);
                    // debugs use
                    miniGames.add(new Rectangle(indeX*gridWidth+25, indeY*gridHeight+27, 61, 100));
                }

                // space ship rocketShip //red (R=220)
                if(lvlMap[indeX][indeY].contains(220)) {
                    drawImage(rocketShip,indeX*gridWidth-80, indeY*gridHeight+3, 320, 320);
                    rocket = new Rectangle(15, 342, 120, 230);
                    // debugs use
                    // changeColor(yellow);
                    // drawRectangle(15, 342, 120, 230);
                    // debugs use
                }
            }
        }
    }

//-------------------------------------------------------
// lava
//-------------------------------------------------------
    private double lavaOffset() {
        double amplitude = 250; // maximum distance the lava can move up and down
        double frequency = 0.002;
        return Math.sin(getTime() * frequency) * amplitude;
    }
//-------------------------------------------------------
// Camera
//-------------------------------------------------------
    public static class Camera {
        private double x;
        private final double y;

        public Camera(double x, double y){
            this.x = x;
            this.y = y;
        }
        public double getX() {
            return x;
        }
        public double getY() {
            return y;
        }
    }
    public void updateCamera() {
        double diff = 0;
        double borderR = windowWidth * 0.8;
        if ((astronautPositionX > borderR)) {
            if (camera.x >= 0 && camera.x <= windowWidth) {
                diff = astronautPositionX - borderR;
                double borderL = windowWidth * 1.2;
                if (diff < windowWidth && diff > 0) {
                    //double targetX = astronautPositionX - borderR;
                    //camera.x += (targetX - camera.x) * 0.11;
                    camera.x = astronautPositionX - borderR;
                    //System.out.println("astronautVelocityX"+ astronautVelocityX);
                    //System.out.println("borderR: " + borderR + " || CX: " + camera.x + " || astronautX: " + astronautPositionX + " || diff: " + diff);
                } else if (diff >= windowWidth) {
                    camera.x += ((double) windowWidth - camera.x) * 0.11;
                } else if (diff <= borderL) {
                    //double targetX = diff - borderL;
                    //camera.x += (targetX - camera.x) * 0.11;
                    camera.x = diff - borderL;
                    //System.out.println("borderL: " + borderL + " || CX: " + camera.x + " || astronautX: " + astronautPositionX + " || diff: " + diff);
                }
            }
        }
    }

    /* --- ASTRONAUT --- */
    Image[] spriteSheet = {loadImage("astronautIdle.png"), loadImage("astronautWalk.png"), loadImage("astronautJump.png")};
    Image[] astronautIdle = new Image[17];
    Image[] astronautWalk = new Image[8];
    Image astronautJump;
    Image astronautLand;
    double astronautPositionX, astronautPositionY;
    double astronautVelocityX, astronautVelocityY;
    final double G = 10; // simulating gravity on earth.
    double astronautTimer, astronautDuration;
    int astronautState; // 0 = idle, 1 = walk, 2 = jump, 3 = fall, .
    int astronautCurrentFrame;
    boolean isRight; // if false, astronaut is facing left.
    boolean isJumping; // if false, astronaut is not jumping.
    int astronautJumpCount;
    double currentWall; // the wall the astronaut is currently hitting.
    Rectangle currentPlatform; // the platform the astronaut is currently on.
    public void initAstronaut() {
        astronautPositionX = 0;
        astronautPositionY = 50;
        astronautVelocityX = 0;
        astronautVelocityY = 0;

        for (int i = 0; i < 17; i++) {
            astronautIdle[i] = subImage(spriteSheet[0], 48*i, 0, 48, 48);
        }
        for (int i = 0; i < 8; i++) {
            astronautWalk[i] = subImage(spriteSheet[1], 48*i, 0, 48, 48);
        }
        for (int i = 2; i < 5; i++) {
            astronautJump = subImage(spriteSheet[2], 48*2, 0, 48, 48);
        }
        astronautLand = loadImage("astronautFall.png");

        astronautState = 0;
        astronautCurrentFrame = 0;

        astronautTimer = 0;

        isRight = true;
        isJumping = true;

        astronautJumpCount = 0;

        currentPlatform = null;
        currentWall = -1;
    }
    public void updateAstronaut(double dt) {
        astronautTimer += dt;

        astronautPositionX += astronautVelocityX * dt;
        astronautPositionY -= astronautVelocityY * dt;

        // ======= Check collision
        currentPlatform = setCurrentPlatform(lvlObjects, astronautHitBoxB);

        wallCollisionCheck(lvlObjects);

        if (isJumping) {
            if ( (currentPlatform==null) && (astronautVelocityY >= 0)) {
                astronautPositionY--;
            }
            astronautVelocityY -= G;
        } else { // !isJumping
            if (currentPlatform != null) {
                astronautPositionY = currentPlatform.y-144;
            }
            astronautVelocityY = 0;
        }
//        floorRoofCollisionCheck();


        // --- collision for the bottom

        if (collisionCheck(lvlObjects, astronautHitBoxB)) {
            if ((currentPlatform != null) && astronautPositionY >= (double) (currentPlatform.y-144)) {
                if (isJumping) { astronautState = 3; }
                isJumping = false;
                astronautVelocityY = 0;
                astronautPositionY = currentPlatform.y-144;
                astronautJumpCount = 0;
            }
        } else {
            isJumping = true;
        }

        /* --- collision from the top --- */

        if (collisionCheck(lvlObjects, astronautHitBoxT)) {
            Rectangle hitPlatform = null; // the platform that was hit by the astronaut's head.
            for (Rectangle object : lvlObjects) {
                if (astronautHitBoxT.intersects(object)) {
                    hitPlatform = object;
                    break;
                }
            }
            assert hitPlatform != null;
            astronautPositionY = hitPlatform.getY() + hitPlatform.getHeight() - 20;
            astronautVelocityY *= -1;
        }

        if (right && !left) {
            if ((astronautPositionX > currentWall-92) || isJumping) {
                astronautVelocityX = 250;
                if (!isJumping) { astronautState = 1; }
            } else {
                astronautVelocityX = 0;
            }
            if (!isJumping) {
            } else { astronautState = 2; }
        } else if (left && !right) {
            if ((astronautPositionX > currentWall+40) || isJumping) {
                astronautVelocityX = -250;
                if (!isJumping) { astronautState = 1; }
            } else {
                astronautVelocityX = 0;
            }
            if (isJumping) { astronautState = 2; }
        } else if (!left) {
            astronautVelocityX = 0;
            if (isJumping) { astronautState = 2; }
        }

        if (down) {
            astronautVelocityY-= 30;
        }

        //======= if there is no collision

        if (astronautState == 0) {
            astronautDuration = 2;
            astronautCurrentFrame = (int)Math.floor(((astronautTimer%astronautDuration)/astronautDuration)*17);
        } else if (astronautState == 1) {
            astronautDuration = 1;
            astronautCurrentFrame = (int)Math.floor(((astronautTimer%astronautDuration)/astronautDuration)*8);
        }

        updateCamera();
    }

    // ===== Astronaut and level object
    private final int hitBoxOffsetX = 50;
    private final int hitBoxOffsetY = 25;
    Rectangle astronautHitBoxR, astronautHitBoxL, astronautHitBoxB, astronautHitBoxT;
    private void astronautHitBoxes() {
        // Whole body
        astronautHitbox = new Rectangle((int) astronautPositionX + hitBoxOffsetX,
                (int) astronautPositionY + hitBoxOffsetY, 40, astronautHeight - 32);
        // changeColor(red);
        // drawRectangle(astronautHitbox.x, astronautHitbox.y, astronautHitbox.width, astronautHitbox.height);

        // Right
        astronautHitBoxR = new Rectangle(astronautHitbox.x + 35, (int) astronautPositionY + hitBoxOffsetY +10, 5, astronautHeight-55);
        // changeColor(green);
        // drawRectangle(astronautHitboxR.x, astronautHitboxR.y, astronautHitboxR.width, astronautHitboxR.height);

        // Left
        astronautHitBoxL = new Rectangle(astronautHitbox.x, (int) astronautPositionY + hitBoxOffsetY +10, 5, astronautHeight-55);
        // changeColor(green);
        // drawRectangle(astronautHitboxL.x, astronautHitboxL.y, astronautHitboxL.width, astronautHitboxL.height);

        // Bottom
        astronautHitBoxB = new Rectangle(astronautHitbox.x +10, astronautHitbox.y + 80 + 30, 20, 10);
        // changeColor(blue);
        // drawRectangle(astronautHitboxB.x, astronautHitboxB.y, astronautHitboxB.width, astronautHitboxB.height);

        // Top
        astronautHitBoxT = new Rectangle(astronautHitbox.x + 10, astronautHitbox.y, 20, 10);
        // changeColor(blue);
        // drawRectangle(astronautHitboxT.x, astronautHitboxT.y, astronautHitboxT.width, astronautHitboxT.height);

    }
    public void wallCollisionCheck(ArrayList<Rectangle> lvlObjectsList) {
        for (Rectangle object : lvlObjectsList) {
            if(astronautHitBoxR.intersects(object)){
                currentWall = object.getX();
                astronautPositionX = currentWall-92;
                astronautState = 0;
                astronautVelocityX = 0;
                return;
            } else if(astronautHitBoxL.intersects(object)) {
                currentWall = object.getX()+object.getWidth();
                astronautPositionX = currentWall-48;
                astronautState = 0;
                astronautVelocityX = 0;
                return;
            } else {
                currentWall = -1;
            }
        }
    }
    public boolean collisionCheck(ArrayList<Rectangle> lvlObjectsList, Rectangle hitbox) {
        for (Rectangle object : lvlObjectsList) {
            if (hitbox.intersects(object)) {
                return true;
            }
        }
        return false;
    }
    boolean inMiniGame = false;
    boolean didCompleteMiniGame = false;
    boolean didCompleteMiniGame2 = false;
    boolean inMiniGame1 = false;
    boolean inMiniGame2 = false;
    public void miniGameCollisionCheck(ArrayList<Rectangle> miniGames, Rectangle hitbox) {
        for (Rectangle miniGame : miniGames) {
            if (hitbox.intersects(miniGame)) {
                if (miniGame.equals(miniGames.get(0))) {

                    if(!didCompleteMiniGame){
                        changeColor(new Color(200, 200, 200, 200)); // Set a transparent color
                        drawSolidRectangle(camera.x,camera.y,gameWidth, gameHeight);

                        startGameReactionMiniGame();
                        astronautVelocityX = 0;
                        astronautVelocityY = G;
                        astronautPositionX = miniGame.x-100;
                        astronautPositionY = miniGame.y;
                        System.out.println("MINIG1");
                        inMiniGame1 = true;
                    }
                    else {

                        String instruction1 = "This Fuel is collected!";
                        String instruction2 = "Fuel Collected: " + fuelCollected;

                        changeColor(red);
                        mGraphics.setFont(new Font("Impact", Font.BOLD, 60));
                        FontMetrics metrics = mGraphics.getFontMetrics(mGraphics.getFont());
                        mGraphics.drawString(instruction1, ((int)camera.x +windowWidth - metrics.stringWidth(instruction1))/2,200);

                        mGraphics.setFont(new Font("Impact", Font.BOLD, 30));
                        metrics = mGraphics.getFontMetrics(mGraphics.getFont());
                        mGraphics.drawString(instruction2, ((int)camera.x +windowWidth - metrics.stringWidth(instruction2))/2,200+metrics.getHeight());
                    }
                    break;
                } else if (miniGame.equals(miniGames.get(1))) {
                    if(!didCompleteMiniGame2){

                        changeColor(new Color(200, 200, 200, 200)); // Set a transparent color
                        drawSolidRectangle(camera.x,camera.y,gameWidth, gameHeight);

                        startMiniGameTWO();
                        astronautVelocityX = 0;
                        astronautVelocityY = G;
                        astronautPositionX = miniGame.x-100;
                        astronautPositionY = miniGame.y;
                        inMiniGame2 = true;
                    }
                    else {


                        String instruction1 = "This Fuel is collected!";
                        String instruction2 = "Fuel Collected: " + fuelCollected;

                        changeColor(red);
                        mGraphics.setFont(new Font("Impact", Font.BOLD, 60));
                        FontMetrics metrics = mGraphics.getFontMetrics(mGraphics.getFont());
                        mGraphics.drawString(instruction1, ((int)(camera.x + windowWidth +1000 - metrics.stringWidth(instruction1))/2),200);
                        System.out.println(inMiniGame2);
                        mGraphics.setFont(new Font("Impact", Font.BOLD, 30));
                        metrics = mGraphics.getFontMetrics(mGraphics.getFont());
                        mGraphics.drawString(instruction2, ((int)(camera.x + windowWidth +1000 - metrics.stringWidth(instruction2))/2),200+metrics.getHeight());
                    }
                    break;
                }
            }
        }
        inMiniGame = false;
    }
    public void rocketShipCollisionCheck(Rectangle rocket, Rectangle hitbox) {
        if (hitbox.intersects(rocket)){
            if(fuelCollected==maxFuel){
                try{
                    gameCompleted = true;
                    gameCompleteScreen();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try{
                    rocketInstructionScreen();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void endGameObjCollisionCheck(ArrayList<Rectangle> endGameObjects, Rectangle hitbox) {
        for (Rectangle object : endGameObjects) {
            if (hitbox.intersects(object)) {
                heartAmount=0;
            }
        }
    }
    public void damageObjCollisionCheck(ArrayList<Rectangle> damageObjects, Rectangle hitbox) {
        for (Rectangle object : damageObjects) {
            if (hitbox.intersects(object)) {
                minusHeart();
            }
        }
    }
    public Rectangle setCurrentPlatform(ArrayList<Rectangle> lvlObjectsList, Rectangle hitbox) {
        for (Rectangle object : lvlObjectsList) {
            if (hitbox.intersects(object)) {
                if ((currentPlatform == null) || (!currentPlatform.equals(object))) {
                    return object;
                }
            }
        }
        return null;
    }

    // ===== Astronaut and game edge

    boolean isIsGameEdge;
    private void checkEdge() {
        if (astronautHitbox.x < 0) {
            astronautPositionX = -hitBoxOffsetX;
        }
        if (astronautHitbox.x >= gameWidth - hitBoxOffsetX) {
            astronautPositionX = gameWidth - astronautWidth + hitBoxOffsetX + 1;
        }
    }

    // ===== Drawing Astronaut
    private static final int astronautWidth = 150;
    private static final int astronautHeight = 150;
    public void drawAstronaut() {
        if (!isRight) {
            if ((astronautState == 0) || ((astronautState == 1) && (isJumping))) {
                drawImage(astronautIdle[astronautCurrentFrame], astronautPositionX+astronautWidth, astronautPositionY, -astronautWidth, astronautHeight);
            } else if (astronautState == 1) {
                drawImage(astronautWalk[astronautCurrentFrame], astronautPositionX+astronautWidth, astronautPositionY, -astronautWidth, astronautHeight);
            } else if (astronautState == 2) {
                drawImage(astronautJump, astronautPositionX+astronautWidth, astronautPositionY, -astronautWidth, astronautHeight);
            } else if (astronautState == 3) {
                drawImage(astronautLand, astronautPositionX+astronautWidth, astronautPositionY, -astronautWidth, astronautHeight);
            }
        } else {
            if (((astronautState == 0) || ((astronautState == 1) && (isJumping)))) {
                drawImage(astronautIdle[astronautCurrentFrame], astronautPositionX, astronautPositionY, astronautWidth, astronautHeight);
            } else if (astronautState == 1) {
                drawImage(astronautWalk[astronautCurrentFrame], astronautPositionX, astronautPositionY, astronautWidth, astronautHeight);
            } else if (astronautState == 2) {
                drawImage(astronautJump, astronautPositionX, astronautPositionY, astronautWidth, astronautHeight);
            } else if (astronautState == 3) {
                drawImage(astronautLand, astronautPositionX, astronautPositionY, astronautWidth, astronautHeight);
            }
        }

        // ========== Collision Checking
        astronautHitBoxes();
        checkEdge();
        rocketShipCollisionCheck(rocket, astronautHitbox);
        miniGameCollisionCheck(miniGames, astronautHitbox);
    }
    long startTimer, currentTime;
    long timeSeconds, timeMinutes, timeHours;
    double timerPlacement;
    public void initTimer(){
        startTimer = System.currentTimeMillis();
        currentTime=0;
        timeMinutes =0;
        timeHours=0;
        timeSeconds=0;

    }
    public void drawTimer(){
        currentTime = System.currentTimeMillis() - startTimer;
        changeColor(white);
        long seconds;
        String sec, min;
        timeSeconds = currentTime / 1000;
        seconds = timeSeconds % 60;
        timeMinutes = timeSeconds / 60;
        sec = String.valueOf(seconds);
        min = String.valueOf(timeMinutes);
        restoreLastTransform();
        drawText(timerPlacement, 20, min+":"+sec, "Arial", 25);
        saveCurrentTransform();
    }
    Image temp;
    Image[] hearts  = new Image[5];
    int heartAmount;
    double dist;
    long timerDifference, tempSeconds, tempNow;
    public void initHeart(){
        temp = loadImage("Heart.png");
        heartAmount = 5;
        for(int i=0 ; i < heartAmount ; i++) {
            hearts[i] = temp;
        }
        dist = 0;
    }
    public void drawHeart(){
        saveCurrentTransform();
        translate(0,0);
        for(int i =0; i < heartAmount ; i++) {
            drawImage(hearts[i], 5+dist, 15);
            dist += 20;
        }
        dist=0;
        restoreLastTransform();
    }
    public void minusHeart(){
        if(timerDifference==0){
            heartAmount--;
            timerDifference=currentTime;
        }else{
            tempSeconds =(timerDifference/1000)%60;
            tempNow = (currentTime/1000)%60;
            if((tempNow - tempSeconds) > 2){
                timerDifference=0;
            }
        }
    }

    // Keep track of keys
    public static boolean left, right, up, down, space;
    public boolean gameCompleted, gameOver;
    private final Camera camera = new Camera(0, 0);
    private static Rectangle astronautHitbox = new Rectangle();
    PauseMenu pause; // for pause menu;
    public void init(){

        // Setup booleans
        left  = false;
        right = false;
        up    = false;
        down  = false;
        space = false;

        isIsGameEdge = false;

        gameCompleted = false;
        gameOver = false;

        levelInit();
        initAstronaut();
        initTimer();
        initHeart();
    }
    @Override
    public void update(double dt) {
        if (!gameCompleted && !gameOver) {
            updateAstronaut(dt);
            endGameObjCollisionCheck(endGameObjects, astronautHitbox);
            damageObjCollisionCheck(damageObjects, astronautHitbox);
            //System.out.println(timer);
        } else {
            timer.stop();
        }
    }
    public void gameOverScreen() throws InterruptedException {
        if(heartAmount == 0){
            // Score
            mGraphics.setColor(Color.red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 40));
            FontMetrics metrics1 = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString("Fuel Collected: " + fuelCollected, (windowWidth - metrics1.stringWidth("Fuel Collected: " + fuelCollected))/2, mGraphics.getFont().getSize());

            // Game over text
            mGraphics.setColor(Color.red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 75));
            FontMetrics metrics2 = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString("Game Over", (windowWidth - metrics2.stringWidth("Game Over"))/2, windowHeight/2);

            // Quick Instruction
            mGraphics.setColor(Color.red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 25));
            FontMetrics metrics3 = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString("Close Window To Continue", (windowWidth - metrics3.stringWidth("Close Window To Continue"))/2, windowHeight/4);

            // Exit Back to menu
            timer.stop();

            gameOver = true;
        }

    }
    public void gameCompleteScreen() throws InterruptedException {
        if(fuelCollected == maxFuel){

            mGraphics.setColor(Color.red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 40));
            String text1 = "Congratulation!";
            FontMetrics metrics = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString(text1, (windowWidth-metrics.stringWidth(text1))/2, mGraphics.getFont().getSize()+50);

            mGraphics.setColor(Color.red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 75));
            String text2 = "Game Completed!!";
            metrics = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString(text2, (windowWidth-metrics.stringWidth(text2))/2, windowHeight/2+50);

            // Quick Instruction
            mGraphics.setColor(Color.red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 25));
            String text3 = "Close Window And Go Back To Main Menu";
            metrics = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString(text3, (windowWidth - metrics.stringWidth(text3))/2, windowHeight/4+50);

            // Exit Back to menu
            timer.stop();

            gameCompleted = true;
        }

    }
    public void rocketInstructionScreen() throws InterruptedException {
        if(fuelCollected<maxFuel){

            changeColor(new Color(200, 200, 200, 128));
            drawSolidRectangle(camera.x+100,camera.y+100,windowWidth-200, windowHeight-200);

            // Quick Instruction
            String instruction1 = "Collect Enough Fuels And Come Back!";
            String instruction2 = "Fuel Collected: " + fuelCollected;
            String instruction3 = "  ←  Left key  =  Move Left  ";
            String instruction4 = "  →  right key  =  Move Right  ";
            String instruction5 = "  Spacebar  =  Jump  ";

            changeColor(red);
            mGraphics.setFont(new Font("Impact", Font.BOLD, 60));
            FontMetrics metrics = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString(instruction1, (windowWidth - metrics.stringWidth(instruction1))/2,200);

            mGraphics.setFont(new Font("Impact", Font.BOLD, 30));
            metrics = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString(instruction2, (windowWidth - metrics.stringWidth(instruction2))/2,200+metrics.getHeight());

            mGraphics.setFont(new Font("Impact", Font.BOLD, 40));
            metrics = mGraphics.getFontMetrics(mGraphics.getFont());
            mGraphics.drawString(instruction3, (windowWidth - metrics.stringWidth(instruction3))/2,350);
            mGraphics.drawString(instruction4, (windowWidth - metrics.stringWidth(instruction4))/2,350+ metrics.getHeight());
            mGraphics.drawString(instruction5, (windowWidth - metrics.stringWidth(instruction5))/2,350+metrics.getHeight()*2);
        }
    }
    @Override
    public void paintComponent() {

//-------------------------------------------------------
// Game board setting
//-------------------------------------------------------

        setWindowSize(windowWidth,windowHeight);
        mFrame.setTitle("The NPC6 - Assignment 2"); // Set the title of the frame

        if (!gameCompleted && !gameOver) {
            // Background
            changeBackgroundColor(Color.black);
            clearBackground(windowWidth, windowHeight);

            // Camera
            translate(-camera.getX(), -camera.getY());

            for (int indexX = 0; indexX < starField.getWidth(null) * 5; indexX += starField.getWidth(null)) {
                drawImage(starField, indexX, 0);
            }

            drawLvl(lvlMap);
            drawAstronaut();
            drawTimer();
            drawHeart();
            if (heartAmount == 0) {
                try {
                    gameOverScreen();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            // Reset camera translation
            translate(+camera.getX(), +camera.getY());

            isIsGameEdge = false;
        }
    }

    /* --- KEYBOARD METHODS --- */
    boolean menuOpened = false;
    @Override
    public void onReactionMiniGameComplete(boolean passed) {
        if (passed) {
            if (!didCompleteMiniGame) {
                fuelCollected++;
                didCompleteMiniGame = true;
            }
        }
        inMiniGame1 = false;
    }
    @Override
    public void onTimeBasedMiniGameComplete(boolean passed) {
        if (passed) {
            if (!didCompleteMiniGame2) {
                fuelCollected++;
                didCompleteMiniGame2 = true;
            }
        }
        inMiniGame2 = false;
    }
    @Override
    public void onPauseMenu() {
        menuOpened = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameCompleted && !gameOver && !inMiniGame1 && !inMiniGame2) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = true;
                isRight = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = true;
                isRight = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                space = true;
                if (astronautJumpCount < 2) {
                    astronautState = 2;
                    isJumping = true;
                    astronautPositionY--;
                    astronautVelocityY = 250;
                    astronautJumpCount++;
                    astronautTimer = 0;
                }
                try {
                    gameAudio.jumpingSound();
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
                    throw new RuntimeException(ex);
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                // Pause Game and Open Pause MainMenu
                if(!menuOpened){
                    timer.stop();

                    // create object
                    pause = new PauseMenu(timer);
                    pause.setMenuOpenedListener(this);
                    menuOpened = true;

                    // reset Velocities
                    astronautVelocityX = 0;
                    astronautVelocityY = 0;
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameCompleted && !gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                down = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                left = false;
                astronautTimer = 0;
                astronautState = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                right = false;
                astronautTimer = 0;
                astronautState = 0;
            }
            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                space = false;
            }
        }
    }

    public void keyTyped(KeyEvent event) {}
}