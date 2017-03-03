package com.example.alticast.test;

        import android.content.Context;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.Rect;
        import android.graphics.RectF;
        import android.util.Log;
        import android.view.Display;
        import android.view.MotionEvent;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.view.WindowManager;

/**
 * Created by dongyunkam on 2017. 3. 3..
 */

public class GameView extends SurfaceView implements SurfaceHolder.Callback {


    private GameThread mThread;

    private int Width, Height;
    private Paint redPaint;
    private Paint bluePaint;
    private World planet;

    //img variable in game
    private Bitmap explosionImg;
    private Bitmap[] explosionSplit;
    private Bitmap backGroundImg;
    private Bitmap missileDishImg;
    private Bitmap planetSurface;
    private int planetSurfaceSize = 280;
    //img variable in menu


    //game setting
    boolean playGame = false;


    public GameView(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        Width = display.getWidth();
        Height = display.getHeight();

        planet = new World(Width, Height);

        redPaint = new Paint();
        bluePaint = new Paint();
        redPaint.setColor(Color.RED);
        bluePaint.setColor(Color.BLUE);
        redPaint.setStrokeWidth(20);
        bluePaint.setStrokeWidth(20);



        //img load
        missileDishImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.missile_dish);
        backGroundImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.nightsky);
        explosionImg = BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_sprite);
        planetSurface = BitmapFactory.decodeResource(context.getResources(), R.drawable.planet_surface101);

        explosionSplit = new Bitmap[12];

        int exW=explosionImg.getWidth()/3 -90;
        int exH=explosionImg.getHeight()/4-30;


        for(int i=0;i<12;i++)
        {
            int k = i%3;
            int j = (int) i/3;
            int a = 0;
            if(i>=9)
            {
                a=30;
            }
            explosionSplit[i]=Bitmap.createBitmap(explosionImg,k*explosionImg.getWidth()/3+90,j*exH,exW,exH+a);
            explosionSplit[i]=Bitmap.createScaledBitmap(explosionSplit[i],explosionSplit[i].getWidth()/2,explosionSplit[i].getHeight()/2,true);

        }

        planetSurface = Bitmap.createScaledBitmap(planetSurface, Width, 280, true);
        mThread = new GameThread(context, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0) {
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0) {
        boolean done = true;
        while (done) {
            try {
                mThread.join();
                done = false;
            } catch (InterruptedException e) {

            }
        }

    }

    class GameThread extends Thread {
        private SurfaceHolder mHolder;
        Canvas canvas = null;

        public GameThread(Context context, SurfaceHolder holder) {
            mHolder = holder;

        }

        public void run() {

            playGame=true;
            while (true) {
                canvas = mHolder.lockCanvas(null);


                    try {
                        if (canvas != null) {

                            synchronized (mHolder) {
                                calc();
                                canvas = Draw(canvas);
                            }
                        }
                    }finally{
                        if(canvas !=null) {

                            mHolder.unlockCanvasAndPost(canvas);
                        }
                    }
            }
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (getHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                //add line_missile at list or vector
                planet.addMissiles(planetSurfaceSize,(int)event.getX(),(int)event.getY());

            }
            return true;
        }

    }



    private void calc ()
    {
        // TODO Auto-generated method stub

        if(playGame==true){//if the boolean controlling the playing of the game is true then

            setAllFalse();//calls the method which sets all of the booleans in the game to false
            playGame=true;//sets playGame to true again so that the game can continue

            if(planet.getGameWon()==true){//if a level has been completed then
                setAllFalse();//sets all booleans false
                if(planet.getLevel()!=5){//if the game is on any other level besides 5 then
             //       levelCompleteScreen=true;//call on the screen meant for all of the other levels besides 5
                }
                else if(planet.getLevel()==5){//if the game is on level 5 then
             //       levelCompleteScreenSettings=true;///call on the screen meant for level 5
                }
            }

            //calls the "updateScreenSize" method from the planet class
            planet.updateScreenSize(getWidth(), getHeight());

            //calls the "updateBackGround" method from the planet class
            planet.updateBackGround();

            //decides whether the "missileDishLightingSpritesIndex" should be updated or reset to zero
/*            if (missileDishLightingTimer == 0){

                missileDishLightingTimer = missileDishLightingTimerTime;

                if (missileDishLightingSpritesIndex < (missileDishLightingSprites.length - 1))
                    missileDishLightingSpritesIndex++;
                else
                    missileDishLightingSpritesIndex = 0;
            }
            else if (missileDishLightingTimer > 0)
                missileDishLightingTimer--;
*/


            //calls the "updateMissiles" method from the planet class
            planet.updateMissiles();
            //calls the "updateExplosions" method from the planet class
            planet.updateExplosions();

            if(!planet.getDied()){
                planet.updateEnemyMissiles(planetSurfaceSize);
            }
            //resets the variables if the player had died
            else{
                setAllFalse();
                //drawGameOverScreen=true;
                //planet.setDied(false);

                //calls the "updateScore" method
      //          updateScore();

                //calls the "reset" method from the planet class
                planet.reset(planet.getLevel());
            }
            //calls the "updateHealth" method from the planet class
            //      planet.updateHealth();
            //calls the "collisions" method from the planet class
            planet.collisions();

        }
        ////////////////////////////////////////////////////////////////////////////
    }
    private void setAllFalse(){//method that sets all of the booleans in the game to false, so as to avoid boolean confusion

    }

    private Canvas Draw(Canvas canvas)
    {
        canvas.drawBitmap(backGroundImg, (int) planet.getXBackGround(), (int) planet.getYBackGround()+280, null);
        canvas.drawBitmap(planetSurface, 0, Height - planetSurface.getHeight(), null);

        for(int i=0; i<planet.getMissiles().size();i++)
        {
            Line_Missiles tempMissile = planet.getMissiles().get(i);

            canvas.drawLine(Width/2,Height,(int)tempMissile.getXCurrent(),(int)tempMissile.getYCurrent(),bluePaint);
        }




        for (int i = 0 ; i < planet.getExplosions().size () ; i++)
        {
            Explosions tempExplosions = (Explosions) planet.getExplosions().get (i);

            int num = planet.getExplosionUpdateCount().get(i);
            int explosionSpritesIndex = 0;

            for (int j = (planet.getExplosionsSpeed()/explosionSplit.length); j<=planet.getExplosionsSpeed(); j += (planet.getExplosionsSpeed()/explosionSplit.length)){
                if (num <= j)
                    canvas.drawBitmap(explosionSplit[explosionSpritesIndex],(int)tempExplosions.getXCurrent()-explosionSplit[explosionSpritesIndex].getWidth()/2 + 90 , (int)tempExplosions.getYCurrent()-explosionSplit[explosionSpritesIndex].getHeight()/2,null);
                else if (explosionSpritesIndex < (explosionSplit.length - 1))
                    explosionSpritesIndex++;

            }
        }
        for(int x=0;x<planet.getOppo().size();x++){//for loop runs through the line opponents vector and draws every line in the vector
            Line_Opponents temp =  (Line_Opponents) planet.getOppo().get(x);
            canvas.drawLine((int) temp.getStartXCoor (), (int) temp.getStartYCoor (), (int) temp.getXCoor (), (int) temp.getYCoor (),redPaint);

        }
        return canvas;
    }
}
