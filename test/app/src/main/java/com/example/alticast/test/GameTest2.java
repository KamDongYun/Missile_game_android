package com.example.alticast.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Random;
import java.util.Vector;


/**
 * Created by alticast on 17. 2. 28.
 */
public class GameTest2 extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread mThread;
    private ArrayList<Line_Missiles> list = new ArrayList<>();
    private int width, height;
    private Bitmap imageBack ;
    private Bitmap imageRador ;
    private Bitmap missileimg ;
    private Paint paint;
    private Paint paint2;


    private Vector<Line_Opponents> oppo = new Vector<>();


    public GameTest2(Context context){
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(20);

        paint2 = new Paint();
        paint2.setColor(Color.RED);
        paint2.setStrokeWidth(20);

        Display display = ((WindowManager) context.getSystemService(context.WINDOW_SERVICE)).getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
        imageBack = BitmapFactory.decodeResource(context.getResources(),R.drawable.nightsky);
        imageRador = BitmapFactory.decodeResource(context.getResources(),R.drawable.missile_dish);
        missileimg = BitmapFactory.decodeResource(context.getResources(),R.drawable.missilesammo101);
        imageBack = Bitmap.createScaledBitmap(imageBack,width,height,true);
        imageRador = Bitmap.createScaledBitmap(imageRador,150,150,true);
        missileimg = Bitmap.createScaledBitmap(missileimg,100,100,true);



        oppo.add(new Line_Opponents(0,0,0,0,5));
        oppo.add(new Line_Opponents(30,0,30,0,5));
        oppo.add(new Line_Opponents(200,0,200,0,5));
        oppo.add(new Line_Opponents(550,0,550,0,5));


        mThread = new GameThread(context, holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3){

    }

    @Override
    public void surfaceCreated(SurfaceHolder arg0){
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder arg0){
        boolean done = true;
        while(done){
            try{
                mThread.join();
                done =false;
            }catch(InterruptedException e){

            }
        }

    }
    class GameThread extends Thread{
        private SurfaceHolder mHolder;
        private int xMissiledish, yMissiledish;
        private double oppoSpeedX =0.8;
        private double universalOppoSpeed=1.2;

        public GameThread(Context context, SurfaceHolder holder){

            mHolder = holder;

            xMissiledish = width/2 - imageRador.getWidth()/2;
            yMissiledish = height - imageRador.getHeight();
        }
        public void run(){


            while(true){
                Canvas canvas = null;
                canvas = mHolder.lockCanvas(null);

                try {
                    if (canvas != null) {
                        synchronized (mHolder) {
                            canvas.drawBitmap(imageBack, 0, 0, null);
                            canvas.drawBitmap(imageRador, xMissiledish, yMissiledish, null);
                            if (list.size() != 0) {


                                for (int i = list.size(); i > 0; i--) {

                                    boolean result = list.get(i - 1).updateMissile();
                                    if (result == false) {
                                        list.remove(i - 1);
                                    }
                                }
                                for (Line_Missiles mi : list) {
                                    double x = mi.getXCurrent();
                                    double y = mi.getYCurrent();
                                    canvas.drawBitmap(missileimg, (int) x -missileimg.getWidth()/2, (int) y -missileimg.getHeight()/2, null);
                                    canvas.drawLine((float) ((width - missileimg.getWidth()) / 2), (float) (height - missileimg.getHeight()), (float) x, (float) y, paint);
                                }

                            }

                        }
                        int temp = oppo.size();//a variable that stores the size of the vector that contains all of the
                        //line opponent objects
                        int timer = 0;//variable that keeps track of number of "updates" in run method and is initialized as 0

                        for (int x = 0; x < temp; x++) //for loop that runs through the vector containing line opponents
                        {

                            Line_Opponents tempObj = (Line_Opponents) oppo.get(x); //temporary object which gets the individual line opponent
                            //objects from within the 'oppo' vector

                            if (tempObj.getStartXCoor() < (width / 2))//if the line spawns on the left half of the screen, then the following code executes
                            {
                                if (tempObj.getStartXCoor() < 0) {//if line spawns outside screen (on the left) then fix it by setting
                                    //starting x-coordinate to one on-screen
                                    tempObj.setStartX(10);
                                }
                                tempObj.moveObjectHor(oppoSpeedX);//move line from left to right
                                tempObj.moveObjectVer(universalOppoSpeed);//move line downwards
                            }
                            if (tempObj.getStartXCoor() > (width / 2))//if line spawns on right half of screen then...
                            {
                                if (tempObj.getStartXCoor() > width) {//if line spawns outside of screen on the right
                                    //then reset the starting coordinate to one on the screen
                                    tempObj.setStartX(width - 10);
                                }
                                oppoSpeedX = 0.8;//sets the speed of the line opponents
                                tempObj.moveObjectHorBack(oppoSpeedX);//move the line from right to left to prevent exiting from screen
                                tempObj.moveObjectVer(universalOppoSpeed);//move object downwards

                            }
                        }
                            for (int x_ = 0; x_ < oppo.size(); x_++)//goes through line opponents array again
                            {
                                Line_Opponents tempObj2 = (Line_Opponents) oppo.get(x_);//arbitrary line opponents object to get the line objects from the vector

                                if (tempObj2.getYCoor() >= height)//if the line hits the surface of the planet then...
                                {
                                    oppo.remove(tempObj2);//remove the line that struck the planet

				/*
				 * the following lines control the spawning of a new line to replace the one that struck the surface of the planet. there is a limit set so that the
				 * number of lines that can appear at the top are limited, depending on the level.
				 */
                                    Random rand = new Random();

                                    if (oppo.size() < 4) {//if the number of lines in the vector is less than 4 then
                                        int tempCoor = rand.nextInt(width);//location of the new line is randomly generated
                                        Line_Opponents line5 = new Line_Opponents(tempCoor, 0, tempCoor, 0, 5);//new line declared using arbitrary line opponents object
                                        oppo.add(line5);//new line added to the vector
                                    }


                                }
                            }

                        for(int ii=0; ii<oppo.size();ii++){
                            canvas.drawLine((float)oppo.get(ii).getStartXCoor(),(float)oppo.get(ii).getStartYCoor(),(float)oppo.get(ii).getXCoor(),(float)oppo.get(ii).getYCoor(),paint2);
                        }
                        }

                }
                finally {
                    if (canvas != null) {
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
                Line_Missiles missile = new Line_Missiles((width-missileimg.getWidth())/2, height-missileimg.getHeight(), event.getX(), event.getY(), 100);
                list.add(missile);
            }
            return true;
        }
    }

}
