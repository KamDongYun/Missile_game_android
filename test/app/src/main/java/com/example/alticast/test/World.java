package com.example.alticast.test;


import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;


public class World{

	//all the variables used in the word class are declared
	private int width; //width of the window (JFrame)
	private int height; //height of the window (JFrame)

	private final int CURSOROFFSET; 

	private boolean died; //indicates whether the player has died
	private boolean gameWon; //indicates whether the player has won the game

	//Creates variables for the pictures imported
//	private BufferedImage actualHealthBar; //represents the entire health bar picture
//	private BufferedImage shownHealthBar; //represents parts of the health bar picture based on the current health of the planet

	private double startingHealth; //indicates the starting health of the planet
	private double actualHealth; //indicates the actual health of the planet
	private double shownHealth; //indicates the shown health of the planet (health shown on the window/JFrame)
	private double healthPicWidth; //indicates the width used by SubImage to get part of the entire health bar picture
	
	private double actualScore; //indicates the actual score
	private double shownScore; //indicates the shown score (score shown on the window/JFrame)
	private double scoreIncrement; //indicates how fast the score increases (how fast shownScore increases to catch up with actual score)


	private double xBackGround; //x position of the background
	private double yBackGround; //y position of the background
	private double yBackGroundIncrement; //how fast the background moves

	private Vector<Line_Missiles> missiles; //creates a vector of "Line_Missiles"
	private int missilesSpeed; //speed of the missiles
	private int settingsMissilesSpeed;
	private int missilesTimer; //how fast the missiles can be shoot one after the other
	private int missilesTimerTime; //indicates how many seconds need to pass before the next missile can be shoot
	
	
	
	private final int NUMOFMISSILESLVL1; //how much ammo the player gets on Level 1
	private final int NUMOFMISSILESLVL2; //how much ammo the player gets on Level 2
	private final int NUMOFMISSILESLVL3; //how much ammo the player gets on Level 3
	private final int NUMOFMISSILESLVL4; //how much ammo the player gets on Level 4
	private final int NUMOFMISSILESLVL5; //how much ammo the player gets on Level 5
	private int currNumOfMissiles; //current amount of ammo the player has

	private Vector<Explosions> explosions; //creates a vector of "Explosions"
	private Vector<Integer> explosionUpdateCount; //keeps track of how many updates the explosion had gone through
	private int explosionsSize; //size of the explosion
	private int explosionsSpeed; //speed of the explosion

	private double oppoSpeedX;
	private double settingsOppoSpeedX;
	private double universalOppoSpeed;
	private double settingsUniversalOppoSpeed;
	private int level;

	private int speedControl;
	
	private int kills; //the current ammount of the kills by the player
	
	private final int LVL1KILLS; //how many kills the player requires on Level 1
	private final int LVL2KILLS; //how many kills the player requires on Level 2
	private final int LVL3KILLS; //how many kills the player requires on Level 3
	private final int LVL4KILLS; //how many kills the player requires on Level 4
	private final int LVL5KILLS; //how many kills the player requires on Level 5

	private Vector < Line_Opponents > oppo;
	private Line_Opponents line1;
	private Line_Opponents line2;
	private Line_Opponents line3;
	private Line_Opponents line4;

	private Random rand;
	private Random speed;

	public World(int w, int h){
		//Instantiates all the variables created above
		width = w;
		height = h;

		CURSOROFFSET = 35;

		died = false;

		xBackGround = 0;
		yBackGround = -height;
		yBackGroundIncrement = 0.15;

		startingHealth = 100;
		actualHealth=startingHealth;
		shownHealth = actualHealth;

		shownScore = 0;
		actualScore = shownScore;
		scoreIncrement = 50;
/*
		//try and catch statement, if the image file could not be found
		try {
			actualHealthBar = ImageIO.read(new File("Health_Bar.png")); //Imports the entire health bar picture
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Could not load the image files.");
		}
*/
		missiles = new Vector<Line_Missiles> ();
		missilesSpeed = 25;
		settingsMissilesSpeed = missilesSpeed/3;
		missilesTimer = 0;
		missilesTimerTime = 5;
		NUMOFMISSILESLVL1 = 30;
		NUMOFMISSILESLVL2 = 50;
		NUMOFMISSILESLVL3 = 80;
		NUMOFMISSILESLVL4 = 150;
		NUMOFMISSILESLVL5 = 350;

		explosions = new Vector<Explosions> ();
		explosionUpdateCount = new Vector<Integer> ();
		explosionsSize = 170;
		explosionsSpeed = 54;

		//universal opponent speed is set here
		///////////////////////////////////////////////////////////////////////////////
		universalOppoSpeed=1.2;
		settingsUniversalOppoSpeed = universalOppoSpeed/2;
		//////////////////////////////////////////////////////////////////////////////

		oppoSpeedX=0.3;
		settingsOppoSpeedX = oppoSpeedX/2;
		level=1;
		
		//decides the ammo the player gets depending on the level
		if (level == 1)
			currNumOfMissiles = NUMOFMISSILESLVL1;
		else if (level == 2)
			currNumOfMissiles = NUMOFMISSILESLVL2;
		else if (level == 3)
			currNumOfMissiles = NUMOFMISSILESLVL3;
		else if (level == 4)
			currNumOfMissiles = NUMOFMISSILESLVL4;
		else if (level == 5)
			currNumOfMissiles = NUMOFMISSILESLVL5;

		speedControl = 0;
		
		kills = 0;
		
		LVL1KILLS = 25;
		LVL2KILLS = 50;
		LVL3KILLS = 100;
		LVL4KILLS = 200;
		LVL5KILLS = 400;

		oppo = new Vector < Line_Opponents > ();
		line1 = new Line_Opponents (0, 0, 0, 0,level);

		line2 = new Line_Opponents (30, 0, 30, 0,level);

		line3 = new Line_Opponents (200, 0, 200, 0,level);

		line4 = new Line_Opponents (550, 0, 550, 0,level);

		oppo.add (line1);
		oppo.add (line2);
		oppo.add (line3);
		oppo.add (line4);

		rand = new Random ();
		speed = new Random ();
	}

	/*
	 * Gets the width and height of the window/JFrame
	 */
	public void updateScreenSize (int w, int h){
		width = w;
		height = h;
	}
	
	/*
	 * Returns the "gameWon" boolean
	 */
	public boolean getGameWon(){
		return gameWon;
	}

	/*
	 * Updates the x and y position of the back ground
	 */
	public void updateBackGround() {
		// TODO Auto-generated method stub
		if (yBackGround < 0)
			yBackGround += yBackGroundIncrement;
		else
			yBackGround = -height;
	}

	/*
	 * Returns the x position of the background
	 */
	public double getXBackGround(){
		return xBackGround;
	}

	/*
	 * Returns the y position of the background
	 */
	public double getYBackGround(){
		return yBackGround;
	}

	/*
	 * Returns the current level of the game
	 */
	public int getLevel(){
		return level;
	}
	
	/*
	 * Changes the level of the game
	 */
	public void setLevel(int level){
		this.level=level;
	}

	/*
	 * Adds a new missile (ally missile) on the window/JFrame
	 */
	public void addMissiles(double planetSurfaceSize, int cursorX, int cursorY) {
		// TODO Auto-generated method stub
		
		//missile if only added if the required time has passed
		if (missilesTimer == 0){
			Line_Missiles temp = new Line_Missiles (width / 2 - 8, height - planetSurfaceSize - CURSOROFFSET, cursorX, cursorY, missilesSpeed);
			missiles.add (temp);
			missilesTimer = missilesTimerTime;
			currNumOfMissiles--;
		}
		//decreases the missilesTimer and once it reaches zero, the next missile can be fired
		else if (missilesTimer > 0)
			missilesTimer--;
	}

	/*
	 * Returns the "CURSOROFFSET" variable
	 */
	public int getCURSOROFFSET(){
		return CURSOROFFSET;
	}

	/*
	 * Returns the current number of missiles the player has
	 */
	public int getCurrNumOfMissiles(){
		return currNumOfMissiles;
	}

	/*
	 * Returns the "missile" vector
	 */
	public  Vector<Line_Missiles> getMissiles(){
		return missiles;
	}

	/*
	 * Updates the current position of the missiles (ally missiles)
	 */
	public void updateMissiles ()
	{
		// TODO Auto-generated method stub
		//goes through the entire vector missiles, to update each and every missile
		for (int i = 0 ; i < missiles.size () ; i++)
		{
			Line_Missiles tempMissiles = missiles.get (i);
			boolean comparator = tempMissiles.updateMissile ();

			//removes a missile if it has reached its destination and a explosion is added at the location (head of the missile) instead
			if (!comparator)
			{
				addExplosions(tempMissiles.getXCurrent (), tempMissiles.getYCurrent ());

				missiles.remove (i);
			}
		}
	}

	/*
	 * Adds an explosion on the window/JFrame
	 */
	public void addExplosions(double x, double y) {
		// TODO Auto-generated method stub
		Explosions tempExplosions = new Explosions (x, y, explosionsSize, explosionsSpeed);
		explosions.add (tempExplosions);
		explosionUpdateCount.add(0);
	}

	/*
	 * Adds an explosion on the window/Jframe
	 * This method manipulates the size and speed of the explosion
	 */
	private void addExplosions(double x, double y, double size, double speed) {
		// TODO Auto-generated method stub
		Explosions tempExplosions = new Explosions (x, y, explosionsSize*size, explosionsSpeed*speed);
		explosions.add (tempExplosions);
		explosionUpdateCount.add(0);
	}

	/*
	 * Returns the "explosions" vector
	 */
	public Vector<Explosions> getExplosions(){
		return explosions;
	}

	/*
	 * Returns the speed of the explosions
	 */
	public int getExplosionsSpeed(){
		return explosionsSpeed;
	}

	/*
	 * Updates the x and y position of the explsoions
	 */
	public void updateExplosions ()
	{
		// TODO Auto-generated method stub
		//goes through the entire vector explosions, to update each and every explosion
		for (int i = 0 ; i < explosions.size () ; i++)
		{
			Explosions tempExplosions = explosions.get (i);
			boolean comparator = tempExplosions.updateExplosion ();

			//"explosionUpdateCount" for the specific explosion is increased by 1
			int num = explosionUpdateCount.get(i);
			num++;
			explosionUpdateCount.remove(i);
			explosionUpdateCount.add(i, num);

			//removes a explosion if it has reached its suggested size
			if (!comparator){
				explosions.remove(i);
				explosionUpdateCount.remove(i);
			}
		}
	}

	/*
	 * Returns "exlopsionUpdateCount" vector
	 */
	public Vector<Integer> getExplosionUpdateCount(){
		return explosionUpdateCount;
	}


	public void updateEnemyMissiles(double planetSurfaceSize) { //this method controls all of the attributes of the enemy line missiles
		//including speed, locations of spawn and split, and number present at any particular given time
		// TODO Auto-generated method stub
		Line_Opponents tempObj2=new Line_Opponents(0,0,0,0,level);//arbitrary line opponents object
		boolean changed=false;//boolean that controls whether a line splits or not and is initialized as false

		int temp = oppo.size ();//a variable that stores the size of the vector that contains all of the 
		//line opponent objects
		int timer=0;//variable that keeps track of number of "updates" in run method and is initialized as 0

		for (int x = 0 ; x < temp ; x++) //for loop that runs through the vector containing line opponents
		{

			Line_Opponents tempObj = (Line_Opponents) oppo.get (x); //temporary object which gets the individual line opponent
			//objects from within the 'oppo' vector

			if (tempObj.getStartXCoor () < (width / 2))//if the line spawns on the left half of the screen, then the following code executes
			{
				if(tempObj.getStartXCoor ()<0){//if line spawns outside screen (on the left) then fix it by setting
					//starting x-coordinate to one on-screen
					tempObj.setStartX(10);
				}
				tempObj.moveObjectHor (oppoSpeedX);//move line from left to right
				tempObj.moveObjectVer (universalOppoSpeed);//move line downwards
			}
			if (tempObj.getStartXCoor () > (width / 2))//if line spawns on right half of screen then...
			{
				if(tempObj.getStartXCoor ()>width){//if line spawns outside of screen on the right
					//then reset the starting coordinate to one on the screen
					tempObj.setStartX(width-10);
				}                              
				oppoSpeedX=0.3;//sets the speed of the line opponents                                
				tempObj.moveObjectHorBack (oppoSpeedX);//move the line from right to left to prevent exiting from screen
				tempObj.moveObjectVer (universalOppoSpeed);//move object downwards

			}

		}


		for (int x = 0 ; x < oppo.size() ; x++) //goes through the line opponents vector again
		{
			changed=false;//boolean changed is set to false to reset each time for loop runs
			timer++;//timer increases for each cycle of for loop
			Line_Opponents tempObj = (Line_Opponents) oppo.get (x);//temporary line opponents object that gets the individual
			//line objects from the vector
			
			/*
			 * the following lines of code found in this method control the splitting of the lines, in terms of probability of occurrence, 
			 * location of occurrence, and the frequency in terms of the level the game is being played in
			 */
			
			
			
			if(tempObj.getYCoor()>=100 && tempObj.getYCoor()<=(height-(planetSurfaceSize-50))){//if the line is located on the screen (in terms of y-coordinates)
				//within a certain range, then...
				
				
				/*
				 * the following 'if' statements control the specific conditions (such as probability of occurrence) according to level, 
				 * so that the user experiences a gradual increase in difficulty as each level passes
				 */
				if(level>=5){//if level is 5 or greater
					Random probability = new Random();//new random generator
					double prob=probability.nextDouble();//takes values from random generator
					double probChanger=probability.nextDouble();
					
					if((prob-probChanger)>=0.75){//if probability is within a certain range then execute code
						if((timer%4)==0){//runs if value of timer is evenly divisible by 4
							tempObj2=new Line_Opponents(tempObj.getXCoor(),tempObj.getYCoor(),tempObj.getXCoor(),tempObj.getYCoor(),level);//new temporary
							//line opponents object containing all of the coordinates of where the old one is located originally
							tempObj.moveObjectHorBack (oppoSpeedX);//move the new line in the opposite direction of the original one, so as to create
							//splitting effect
							changed=true;
						}
					}
				}
				if(level>=4){
					Random probability = new Random();
					double prob=probability.nextDouble();
					double probChanger=probability.nextDouble();

					if((prob-probChanger)>=0.8){
						if((timer%4)==0){
							tempObj2=new Line_Opponents(tempObj.getXCoor(),tempObj.getYCoor(),tempObj.getXCoor(),tempObj.getYCoor(),level);
							tempObj.moveObjectHorBack (oppoSpeedX);
							changed=true;
						}
					}
				}
				if(level>=3){
					Random probability = new Random();
					double prob=probability.nextDouble();
					double probChanger=probability.nextDouble();

					if((prob-probChanger)>=0.85){
						if((timer%4)==0){
							tempObj2=new Line_Opponents(tempObj.getXCoor(),tempObj.getYCoor(),tempObj.getXCoor(),tempObj.getYCoor(),level);
							tempObj.moveObjectHorBack (oppoSpeedX);
							changed=true;
						}
					}
				}
				else if(level>=2){
					Random probability = new Random();
					double prob=probability.nextDouble();
					double probChanger=probability.nextDouble();
					if((timer%2)==0){
						if((prob-probChanger)>=0.9){
							tempObj2=new Line_Opponents(tempObj.getXCoor(),tempObj.getYCoor(),tempObj.getXCoor(),tempObj.getYCoor(),level);
							tempObj.moveObjectHorBack (oppoSpeedX);
							changed=true;
						}
					}
				}
				else if(level>=1){
					Random probability = new Random();
					double prob=probability.nextDouble();
					double probChanger=probability.nextDouble();
					if((timer%2)==0){
						if((prob-probChanger)>=0.95){
							tempObj2=new Line_Opponents(tempObj.getXCoor(),tempObj.getYCoor(),tempObj.getXCoor(),tempObj.getYCoor(),level);
							tempObj.moveObjectHorBack (oppoSpeedX);
							changed=true;
						}
					}
				}
			}
			
			
			if(changed==true){//if the conditions for a split are met, then the boolean changed (which was made true) should be true, in which case
				//the following code executes...
				
				
				/*
				 * the following lines of code add a limit to the size of the line opponents vector so that it cannot be filled
				 * more than a certain amount per level. this prevents too many lines from being drawn on the screen, due to the
				 * lines splitting up on the screen.
				 */
				if(level==1&&oppo.size()<6){//if the player is playing at level 1 and the number of lines in the vector is less than 6 then
					//the new object (split line) can be added to the array
				oppo.add(tempObj2);
				}
				else if(level==2&&oppo.size()<8){
					oppo.add(tempObj2);
				}
				else if(level==3&&oppo.size()<10){
					oppo.add(tempObj2);
				}
				else if(level==4&&oppo.size()<13){
					oppo.add(tempObj2);
				}
				else if(level==5&&oppo.size()<16){
					oppo.add(tempObj2);
				}
			}
		}
		if(changed==true){//if a split happens, then the timer has to be reset to 0
			timer=0;
		}

		for (int x = 0 ; x < oppo.size () ; x++)//goes through line opponents array again
		{
			Line_Opponents tempObj = (Line_Opponents) oppo.get (x);//arbitrary line opponents object to get the line objects from the vector

			if (tempObj.getYCoor () >= height - planetSurfaceSize - CURSOROFFSET + 20)//if the line hits the surface of the planet then...
			{
				addExplosions(tempObj.getXCoor(), tempObj.getYCoor(), 0.4, 1);//add the explosion there
				oppo.remove(tempObj);//remove the line that struck the planet
				
				/*
				 * the following lines control the spawning of a new line to replace the one that struck the surface of the planet. there is a limit set so that the 
				 * number of lines that can appear at the top are limited, depending on the level.
				 */
				
				kills++;
				
				if(level==1){//if the level the user is playing at is level 1 then
					if (kills == LVL1KILLS)
						gameWon = true;
					
					if (oppo.size() < 4){//if the number of lines in the vector is less than 4 then
						int tempCoor=rand.nextInt (width);//location of the new line is randomly generated
						Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);//new line declared using arbitrary line opponents object
						oppo.add(line5);//new line added to the vector
					}
				}
				else if(level==2){
					if (kills == LVL2KILLS)
						gameWon = true;
					
					if (oppo.size() < 4){
						int tempCoor=rand.nextInt (width);
						Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
						oppo.add(line5);
					}
				}
				else if(level==3){
					if (kills == LVL3KILLS)
						gameWon = true;
					
					if (oppo.size() < 5){
						int tempCoor=rand.nextInt (width);
						Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
						oppo.add(line5);
					}
				}
				else if(level==4){
					if (kills == LVL4KILLS)
						gameWon = true;
					
					if (oppo.size() < 5){
						int tempCoor=rand.nextInt (width);
						Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
						oppo.add(line5);
					}
				}
				else if(level==5){
					if (kills == LVL5KILLS)
						gameWon = true;
					
					if (oppo.size() < 6){
						int tempCoor=rand.nextInt (width);
						Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
						oppo.add(line5);
					}
				}
				
				/*
				 * controls health reduction based on level, so that the health reduced as the levels increase decreases, so as to prevent the game from being 
				 * too difficult
				 */
				
				if (level == 1)//if the level is 1 then the reduction in health should be 10
					actualHealth-=10;
				else if (level == 2)
					actualHealth-=8;
				else if (level == 3)
					actualHealth-=6;
				else if (level == 4)
					actualHealth-=4;
				else if (level == 5)
					actualHealth-=2;
				
				if(actualHealth<=0){//if the health reaches 0, then the 'died' boolean, controlling the ability of the player to play the game, is set to true
					died=true;
				}


			}

		}

	}

	public Vector<Line_Opponents> getOppo(){
		return oppo;
	}

	/*
	 * Returns the "died" boolean
	 */
	public boolean getDied(){
		return died;
	}

	/*
	 * Updates the showHealthBar picture
	 */
/*	public void updateHealth() {
		// TODO Auto-generated method stub
		//"showHealth" is decreased by 1 each time, until it equals "actualHealth"
		if (shownHealth != actualHealth)
			shownHealth--;

		healthPicWidth = shownHealth/startingHealth; //determines the width used by SubImage to get part of the entire health bar picture 

		//try and catch statement, if the width used in "getSubImage" is zero
		try {
			//gets part of the entire health bar picture
			shownHealthBar = actualHealthBar.getSubimage(0, 0, (int) (healthPicWidth * actualHealthBar.getWidth()), actualHealthBar.getHeight());
		} catch (Exception RasterFormatException) {
			// TODO: handle exception
			//the width used in "getSubImage" is change to 1, so no error occures
			shownHealthBar = actualHealthBar.getSubimage(0, 0, 1, actualHealthBar.getHeight());
		}

	}
*/
	/*
	 * Returns the "shownHealthBar" picture
	 */
/*	public BufferedImage getShownHealthBar(){
		return shownHealthBar;
	}
*/
	/*
	 * Returns the "shownHealth" variable
	 */
	public double getShownHealth(){
		return shownHealth;
	}

	/*
	 * Returns the "startingHealth" variable
	 */
	public double getStartingHealth(){
		return startingHealth;
	}

	/*
	 * Checks for collisions (whether the enemy missiles had collided with the explosions)
	 */
	public void collisions() {
		// TODO Auto-generated method stub

		//goes through the entire vector of explosions
		for (int i = 0 ; i < explosions.size () ; i++){
			Explosions tempExplosions = explosions.get (i);

			//goes through the entire vector of oppo (enemy missiles)
			for (int x = 0 ; x < oppo.size () ; x++){
				Line_Opponents tempObj = oppo.get (x);

				boolean comparator = tempExplosions.collision(tempObj.getXCoor(), tempObj.getYCoor());

				//If the enemy missile has collided with an explosion, score is updated, another explosion is added at the point of collision, the enemy missile if removed, kills is increased by 1 and another enemy missile might be added
				if (comparator){

					//updates the score
					updateActualScore(tempExplosions.getXStart(), tempExplosions.getYStart(), tempObj.getXCoor(), tempObj.getYCoor());

					//adds an explosion
					addExplosions(tempObj.getXCoor(), tempObj.getYCoor(), 1, 1);

					//removes the enemy missile
					oppo.remove(x);
					
					//increases the number of kills by the player
					kills++;
					
					//adds a new enemy missile, based on the number of missiles on the window/JFrame and decides whether the player has won the game
					if(level==1){
						if (kills == LVL1KILLS)
							gameWon = true;
						
						//another missile is added if only 3 missiles are present on the window/JFrame
						if (oppo.size() < 4){
							int tempCoor=rand.nextInt (width);
							if(tempCoor>width){
								tempCoor=width-10;
							}
							else if (tempCoor<0){
								tempCoor=10;
							}
							Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
							oppo.add(line5);
						}
					}
					else if(level==2){
						if (kills == LVL2KILLS)
							gameWon = true;
						
						//another missile is added if only 3 missiles are present on the window/JFrame
						if (oppo.size() < 4){
							int tempCoor=rand.nextInt (width);
							if(tempCoor>width){
								tempCoor=width-10;
							}
							else if (tempCoor<0){
								tempCoor=10;
							}
							Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
							oppo.add(line5);
						}
					}
					else if(level==3){
						if (kills == LVL3KILLS)
							gameWon = true;

						//another missile is added if only 4 missiles are present on the window/JFrame
						if (oppo.size() < 5){
							int tempCoor=rand.nextInt (width);
							if(tempCoor>width){
								tempCoor=width-10;
							}
							else if (tempCoor<0){
								tempCoor=10;
							}
							Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
							oppo.add(line5);
						}
					}
					
					else if(level==4){
						if (kills == LVL4KILLS)
							gameWon = true;

						//another missile is added if only 4 missiles are present on the window/JFrame
						if (oppo.size() < 5){
							int tempCoor=rand.nextInt (width);
							if(tempCoor>width){
								tempCoor=width-10;
							}
							else if (tempCoor<0){
								tempCoor=10;
							}
							Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
							oppo.add(line5);
						}
					}
					else if(level==5){
						if (kills == LVL5KILLS)
							gameWon = true;

						//another missile is added if only 5 missiles are present on the window/JFrame
						if (oppo.size() < 6){
							int tempCoor=rand.nextInt (width);
							if(tempCoor>width){
								tempCoor=width-10;
							}
							else if (tempCoor<0){
								tempCoor=10;
							}
							Line_Opponents line5 = new Line_Opponents(tempCoor,0,tempCoor,0,level);
							oppo.add(line5);
						}
					}
				}
			}
		}
	}

	/*
	 * Calculates the new score based on the distance from the starting point of the explosion and head of the enemy missile 
	 */
	private void updateActualScore(double xS, double yS, double xC, double yC) {
		// TODO Auto-generated method stub
		actualScore += (explosionsSize/(Math.sqrt(((Math.pow((xC - xS), 2)) + (Math.pow((yC - yS), 2))))))*scoreIncrement;
	}

	/*
	 * Updates the score shown on the window/JFrame
	 */
	public void updateScore() {
		// TODO Auto-generated method stub
		if (shownScore < actualScore)
			shownScore++;
	}

	/*
	 * Returns the show score on the window/JFrame
	 */
	public double getShownScore() {
		// TODO Auto-generated method stub
		return shownScore;
	}

	/*
	 * Resets the variables related to the actual playing of the game
	 */
	public void reset(int level){
		//all the missiles and explosion are cleared off the window/JFrame
		missiles.clear();
		explosions.clear();
		explosionUpdateCount.clear();
		oppo.clear();

		//"gameWon", score, health and kills variables are reset
		gameWon=false;
		
		shownScore = 0;
		actualScore = shownScore;
		
		actualHealth=startingHealth;
		shownHealth = actualHealth;
		
		kills = 0;
		
		setLevel(level); //changes the level of the game

		//decides the ammo the player gets depending on the level
		if (level == 1){
			currNumOfMissiles = NUMOFMISSILESLVL1;
		}
		else if (level == 2){
			currNumOfMissiles = NUMOFMISSILESLVL2;
		}
		else if (level == 3){
			currNumOfMissiles = NUMOFMISSILESLVL3;
		}
		else if (level == 4){
			currNumOfMissiles = NUMOFMISSILESLVL4;
		}
		else if (level == 5){
			currNumOfMissiles = NUMOFMISSILESLVL5;
		}
		
		/*
		 * Generates new enemy lines
		 */
		line1 = new Line_Opponents (0, 0, 0, 0,level);
		line2 = new Line_Opponents (30, 0, 30, 0,level);
		line3 = new Line_Opponents (200, 0, 200, 0,level);
		line4 = new Line_Opponents (550, 0, 550, 0,level);
		
		//adds all the enemy lines back into the "oppo" vector
		oppo.add (line1);
		oppo.add (line2);
		oppo.add (line3);
		oppo.add (line4);
	}

	/*
	 * Changes the "died" boolean
	 */
	public void setDied(boolean d){
		died = d;
	}
	
	/*
	 * Changes the speed of the ally and enemy missiles (increases the speed)
	 */
	public void setSpeedUp (){
		missilesSpeed -= (settingsMissilesSpeed);
		oppoSpeedX += (settingsOppoSpeedX);
		universalOppoSpeed += (settingsUniversalOppoSpeed);
	}
	
	/*
	 * Changes the speed of the ally and enemy missiles (decreases the speed)
	 */
	public void setSpeedDown (){
		missilesSpeed += (settingsMissilesSpeed);
		oppoSpeedX -= (settingsOppoSpeedX);
		universalOppoSpeed -= (settingsUniversalOppoSpeed);
	}
}
