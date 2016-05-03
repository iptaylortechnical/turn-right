package com.example.isaiah.droidz;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.Date;
import java.util.Random;


/*interface dieListener{
    public void surfaceFinished();
}*/

/**
 * © Isaiah Taylor 3/25/15.
 */
public class ViewerMain extends SurfaceView implements SurfaceHolder.Callback {
    /*declare all vars*/
    private Pawn pawn;
    public MainThread thread;
    private Obstacle[] obs;
    Paint paint;//default paint object
    int stable;//delay timer for pawn proximity color change
    int col;//default color object
    int lives;//total count of lives
    Life[] lifeB = {null, null, null, null, null, null};
    PotentialLife pl = null;
    boolean isStillPlaying;//handle tap event, or restart
    Random rand;//default random object
    boolean firstRestart;//handle second tap for restart
    Date date;//default date object
    long DOUBLE_TAP_TIME_SIG = 0;//time measure for fast double click for restart
    double inc;//universal object speed increment; increases with game tick
    boolean strobeOn;
    int strobeVal; //strobe blinking
    int strobeLoop; //strobe length
    int dim; //global dimension var
    Message message; //on screen message
    int POTENTIAL_LIFE_SPAWNER; //how likely is a pl to spawn?
    int UNKNOWN_SPAWNER;
    private int REVERSE_SPACE = 500; //number of game ticks that must pass until reverse wears off
    private int REVERSE_CUSHION;
    private boolean REVERSE_OBS;

    private int[] gradient;

    private int grad_index;

    private Activity ac;



    //List<dieListener> listeners;


//    Unknown unk;
//    Obstacle samp_obs;

    //TAG for logging
    private static final String TAG = ViewerMain.class.getSimpleName();


    //constructor: overloads surfaceview constructor
    public ViewerMain(Context context, Activity ac) {
        super(context);
        getHolder().addCallback(this);
        //listeners = new ArrayList<dieListener>();
        setFocusable(true);

        this.ac = ac;

    }

    //start point, called after each game loss and at beginning
    @Override
    public void surfaceCreated(SurfaceHolder holder) {


        thread = new MainThread(getHolder(), this);

        varReset();

        //create default message, instantiate message object
        message = new Message("Welcome", false, (getWidth()/2), (getHeight()/2));

        objectReset();



        isStillPlaying = true;

        //paint
        paint.setColor(Color.RED);
        paint.setTextSize(50);

        thread.setRunning(true);
        Log.d(TAG, "thread.start();");

        thread.start();



    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    //redirect touch events
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            if (isStillPlaying) {
                pawn.handleTap((int) Math.round(inc));
            } else {
                //double tap restart
                if (firstRestart && ((new Date()).getTime() - DOUBLE_TAP_TIME_SIG) < 200) {
                    firstRestart = false;
                    surfaceCreated(getHolder());
                } else {
                    date = new Date();
                    DOUBLE_TAP_TIME_SIG = date.getTime();
                    firstRestart = true;
                }
            }
        }

        return true;
    }






    //IN-GAME FEATURES

    //remove one Life objects
    private void dockLife(){
        lifeB[lives-1].setExists(false);
        lives--;
    }

    //add one Life object
    private void addLife(){
        if(lives<6) {
            lifeB[lives].setExists(true);
            lives++;
        }
    }

    //add all possible Life objects
    private void addAll(){
        for(int b = 0; b<lifeB.length; b++){
            if(!lifeB[b].getExists()){
                lives++;
            }
            lifeB[b].setExists(true);
        }
    }

    //slow down game
    private void slowInc(){
        if(inc-1>0){
            inc-=1;
        }else{
            inc = 0;
        }
    }

    //speed up
    private void speedInc(){
        inc+=1;
    }

    //increase universal speed every 400 game ticks
    private void tickUp(){
        if(thread.getREAL_TICK_COUNT()%400 == 0){
            inc+=0.2;
            Log.d(TAG, "up"+inc);
        }
    }

    //takes away all Life objects
    private void dockAll(){
        for(int b = 0; b<lifeB.length; b++){
            if(lifeB[b].getExists()){
                lives--;
            }
            lifeB[b].setExists(false);
        }

    }

    //adds an obstacle to gameplay
    private void addObstacle(){
        Obstacle[] temp = obs;

        obs = new Obstacle[obs.length+1];

        for(int i = 0; i < temp.length; i++){
            obs[i] = temp[i];
        }

        obs[obs.length-1] = new Obstacle(rand.nextInt(getWidth()-40), rand.nextInt(5)+8, getHeight(), 40, REVERSE_OBS);
    }

    //subtracts an obstacle from gameplay
    private void subtractObstacle(){
        Obstacle[] temp = new Obstacle[obs.length-1];

        for(int i = 0; i < temp.length; i++){
            temp[i] = obs[i];
        }

        obs = temp;
    }

    private void reverser(){
        if(pawn.getIsReverseMode()) {

            if (thread.getREAL_TICK_COUNT() > REVERSE_CUSHION) {
                pawn.setIsReverseMode(false);

                REVERSE_OBS = false;

                message.show("Regular", pawn.getX(), pawn.getY());
            }
        }
    }

    private void makeReverse(){
        pawn.setIsReverseMode(true);

        REVERSE_OBS = true;

        REVERSE_CUSHION = REVERSE_SPACE + thread.getREAL_TICK_COUNT();
    }

    private void behavior(int i){
        int ox = obs[i].x;
        int oy = obs[i].y;
        switch (rand.nextInt(12)){
            case 0:
                message.show("NO LIVES!", ox, oy);
                dockAll();
                break;
            case 1:
                message.show("ALL LIVES!", ox, oy);
                addAll();
                break;
            case 2:
                message.show("STROBE!", ox, oy);
                strobeOn = true;
                break;
            case 3:
                message.show("Slower", ox, oy);
                slowInc();
                break;
            case 4:
                message.show("Faster!", ox, oy);
                speedInc();
                break;
            case 5:
                message.show("+500", ox, oy);
                thread.jumpCountBy(500);
                break;
            case 6:
                message.show("+1000", ox, oy);
                thread.jumpCountBy(1000);
                break;
            case 7:
                message.show("Lucky!", ox, oy);
                UNKNOWN_SPAWNER-=(UNKNOWN_SPAWNER*.5);
                POTENTIAL_LIFE_SPAWNER-=(POTENTIAL_LIFE_SPAWNER*.5);
                break;
            case 8:
                message.show("Unlucky", ox, oy);
                UNKNOWN_SPAWNER+=(UNKNOWN_SPAWNER*.5);
                POTENTIAL_LIFE_SPAWNER+=(POTENTIAL_LIFE_SPAWNER*.5);
                break;
            case 9:
                message.show("REVERSE!", ox, oy);
                makeReverse();
//                addLife();
//                message.show("+", ox, oy);
                break;
            case 10:
                message.show("+ Obstacle", ox, oy);
                addObstacle();
                break;
            case 11:
                message.show("- Obstacle", ox, oy);
                subtractObstacle();
            default:
                break;
        }
    }

    //handles pawn strobe effect
    private int strobe(){
        int col = 0;
        if(strobeOn){
            switch ((strobeVal/30)){
                case 0:
                    col = Color.BLACK;
                    break;
                case 1:
                    col = Color.WHITE;
                    break;
                case 2:
                    col = Color.BLACK;
                    break;
                case 3:
                    col = Color.WHITE;
                    break;
                case 4:
                    col = Color.BLACK;
                    break;
                case 5:
                    col = Color.WHITE;
                    break;
                case 6:
                    if(strobeLoop < 5){
                        strobeLoop++;
                        strobeVal = 0;
                    }else {
                        col = Color.WHITE;
                        strobeOn = false;
                        strobeVal = 0;
                        strobeLoop = 0;
                    }
                    break;
                default:
                    col = Color.WHITE;
            }

            strobeVal++;
        }else{
            col = this.col;
        }
        return col;
    }

    private void gradient(){
        if(thread.getREAL_TICK_COUNT() % 50 == 0) {
            Log.d(TAG, gradient[0] + " " + gradient[1] + " " + gradient[2]);
            Log.d(TAG, grad_index+"");

            switch(grad_index){
                case 0:
                    if (gradient[0] == 0) {
                        grad_index = 1;
                    } else {
                        gradient[0]--;
                    }
                    break;
                case 1:
                    if(gradient[1] == 40){
                        grad_index = 2;
                    }else{
                        gradient[1]++;
                    }
                    break;
                case 2:
                    if (gradient[1] == 0) {
                        grad_index = 3;
                    } else {
                        gradient[1]--;
                    }
                    break;
                case 3:
                    if (gradient[2] == 40) {
                        grad_index = 4;
                    } else {
                        gradient[0]++;
                    }
                    break;
                case 4:
                    if (gradient[2] == 0){
                        grad_index = 4;
                    } else {
                        gradient[2]--;
                    }
                    break;
                case 5:
                    if (gradient[0] == 40){
                        grad_index = 0;
                    } else {
                        gradient[0]++;
                    }
                break;
            }
        }
    }










    //Main method called by game-loop thread.
    //called for every screen update and automatically by thread
    //equivalent to game tick
    @Override
    protected void onDraw(Canvas canvas) {
        //background color
        canvas.drawColor(/*Color.rgb(gradient[0], gradient[1], gradient[2])*/Color.BLACK);

        //gradient();

        tickUp();

        reverser();

        //if obstacle is still alive, move and draw
        for(int i = 0; i < obs.length; i++){
            if(obs[i].getAlive()){
                obs[i].move((int)Math.round(inc), REVERSE_OBS);
                obs[i].draw(canvas);
            }
            //else, if unknown obj, replace with normal obs and instantiate. if normal, recycle and set alive
            else{
                if(obs[i].amUnk()){
                    obs[i] = new Obstacle(rand.nextInt(getWidth()-dim), rand.nextInt(5)+8, getHeight(), dim, REVERSE_OBS);
                }else {
                    obs[i].recycle(rand.nextInt(getWidth() - dim), rand.nextInt(5) + 8, REVERSE_OBS);
                }

                if(rand.nextInt(UNKNOWN_SPAWNER) == 0){
                    obs[i] = new Unknown(rand.nextInt(getWidth()-dim), rand.nextInt(5)+8, getHeight(),dim, REVERSE_OBS);
                }

                obs[i].setAlive(true);
            }

            //if pawn hits obstacle
            if(((obs[i].x-pawn.getX())<dim && (obs[i].y-pawn.getY())<dim) && ((obs[i].x-pawn.getX())>-dim && (obs[i].y-pawn.getY())>-(dim/2))){
                //if normal object, dock count and subtract life or end game
                if(!obs[i].amUnk()) {
                    if (lives == 0) {
                        pawn.setAlive(false);
                    } else {
                        message.show("-", obs[i].x, obs[i].y);
                        dockLife();
                        thread.dockCount();

                    }
                }
                //if unknown, chose special behavior: add 1 life, add all lives, or dock all lives
                else{
                    behavior(i);
                }
                //in any event, kill object. object will be recycled next game tick
                if(i != obs.length){
                    obs[i].setAlive(false);
                }

            }

            //if pawn within 20 pixels, turn green, set stable, add to count
            if(((obs[i].x-pawn.getX())<((dim)+(int)(dim*.3)) && (obs[i].y-pawn.getY())<((dim)+(int)(dim*.3))) && ((obs[i].x-pawn.getX())>-((dim)+(int)(dim*.3)) && (obs[i].y-pawn.getY())>-((dim/2)+(int)(dim*.3)))){
                thread.jumpCount();
                col = Color.GREEN;
                stable = thread.getREAL_TICK_COUNT();
            }

        }

        //if proper amount of game ticks has past, set pawn color back to white
        if(thread.getREAL_TICK_COUNT()-stable == 5){
            col = Color.WHITE;
        }

        //if another Potential Life doesn't exist, run 1/1000 chance to add another potential life
        if((rand.nextInt(POTENTIAL_LIFE_SPAWNER) == 15) && !pl.isReal()){
            pl = new PotentialLife(rand.nextInt(getWidth()-(dim/2)), getHeight());
            pl.setExists(true);
        }

        //if a pl exists, move and draw it
        if(pl.isReal()){
            pl.move((int)Math.round(inc));
            pl.draw(canvas);
        }

        //draw all existing lives
        for(int j = 0; j<lifeB.length; j++){
            if(lifeB[j].getExists()){
                lifeB[j].draw(canvas);
            }
        }

        //if pawn hits potential life object, create a new life
        if(((pl.x-pawn.getX())<dim && (pl.y-pawn.getY())<dim) && ((pl.x-pawn.getX())>-(dim/2) && (pl.y-pawn.getY())>-10) && pl.isReal()){
            addLife();
            message.show("+", pl.x, pl.y);
            pl.setExists(false);
        }

        col = strobe(); //get color for pawn based on strobe

        message.draw(canvas); //draw a message if there is any

        //if pawn is alive, move, draw, set proper color, draw score
        if(pawn.getAlive()) {
            pawn.move();
            pawn.draw(canvas, col);
            paint.setColor(col);
            canvas.drawText(""+ thread.tickCount, 50, 50, paint);
        }
        //otherwise, kill the thread, draw the pawn for the last time, create a new paint
        //object and draw the final score
        else{
            Log.d(TAG, "falsifying");
            pawn.draw(canvas, col);
            Paint text = new Paint();
            text.setColor(Color.GREEN);
            text.setTextSize(50);
            canvas.drawText("Score: " + thread.tickCount, 50, 50, text);
            isStillPlaying = false;
            score(canvas);
            thread.setRunning(false);
        }
    }

    private void score(Canvas canvas) {
        Score score = new Score(thread.tickCount, ac);

        Log.d(TAG, "score: " + score.getScore());

        if(score.isHighScore()){
            Log.d(TAG, "new high score");
            Paint text = new Paint();
            text.setColor(Color.WHITE);
            text.setTextSize(50);
            canvas.drawText("NEW HIGH SCORE", 10, (getHeight()/2), text);
        }

    }

    OnKeyListener key;


    //clean
    public void varReset(){
        //initialize all variables for restart
        lives = 3;
        col = 0;
        stable = 0;
        paint = new Paint();
        firstRestart = false;
        inc = 0.0;
        dim = (int)(getHeight()*0.05);
        strobeOn = false;
        strobeVal = 0;
        strobeLoop = 0;
        POTENTIAL_LIFE_SPAWNER = 1000;
        UNKNOWN_SPAWNER = 15;//15
        REVERSE_OBS = false;
        gradient = new int[]{
                40,
                0,
                0
        };
        grad_index = 0;
    }
    public void objectReset(){
        //create all lives; default is dead, so they will not be drawn
        lifeB[0] = new Life(200);
        lifeB[1] = new Life(250);
        lifeB[2] = new Life(300);
        lifeB[3] = new Life(350);
        lifeB[4] = new Life(400);
        lifeB[5] = new Life(450);

        pl = new PotentialLife(0, getWidth());//create potential life for use, default is dead, will not be drawn

        pawn = new Pawn(getHeight(), getWidth(), dim);
        rand = new Random();

//        unk = new Unknown(rand.nextInt(getWidth()-40), rand.nextInt(5)+8, getHeight());
//        unk.setAlive(false);
//
//        samp_obs = new Obstacle(rand.nextInt(getWidth()-40), rand.nextInt(5)+8, getHeight());
//        samp_obs.setAlive(false);false

        obs = new Obstacle[]{null, null};

        obs[0] = new Obstacle(rand.nextInt(getWidth()-dim), rand.nextInt(5)+8, getHeight(), dim, REVERSE_OBS);
        obs[1] = new Obstacle(rand.nextInt(getWidth()-dim), rand.nextInt(5)+8, getHeight(), dim, REVERSE_OBS);
//        obs[2] = new Obstacle(rand.nextInt(getWidth()-dim), rand.nextInt(5)+8, getHeight(), dim, REVERSE_OBS);
//        obs[3] = new Obstacle(rand.nextInt(getWidth()-dim), rand.nextInt(5)+8, getHeight(), dim);
//
//        set last three lives to non-exist, start with only three lives
        lifeB[3].setExists(false);
        lifeB[4].setExists(false);
        lifeB[5].setExists(false);


    }


    /*public void addListener(dieListener listener){
        listeners.add(listener);
    }*/
}