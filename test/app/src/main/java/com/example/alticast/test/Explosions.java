package com.example.alticast.test;

public class Explosions {

	private double xStart; //starting x position of the explosion
	private double yStart; //starting y position of the explosion

	private double xCurrent; //current x position of the explosion
	private double yCurrent; //current y position of the explosion
	private double sizeCurrent; //current size of the explosion

	private double size; //the size of the explosion
	private double speed; //the speed of the explosion

	private double increment;

	Explosions(double xS, double yS, double size, double speed){

		/*
		 * sets the input parameters to the values found in this class and modified by the programmer
		 * and player
		 */

		xStart = xS;
		yStart = yS;
		this.size = size;
		this.speed = speed;

		xCurrent = xStart;
		yCurrent = yStart;
		sizeCurrent = 0;

		increment = size/speed; //decides the increment (how fast the explosion grows) based on the size and speed
	}

	/*
	 * Update the coordinates of the explosion
	 * Returns true, if the explosions are updated
	 * Returns false, if the explosion have reached their suggested size and are not updated
	 */
	public boolean updateExplosion(){

		//calculates whether the explosions have reached their suggested size
		if (xCurrent > (xStart - size)){
			xCurrent -= increment;
			yCurrent -= increment;
			sizeCurrent += increment*2;

			return true;
		}
		return false;
	}
	
	/*
	 * Gets the starting x position of the explosion
	 */
	public double getXStart(){
		return xStart;
	}

	/*
	 * Gets the starting y position of the explosion
	 */
	public double getYStart(){
		return yStart;
	}

	/*
	 * Gets the current x position (outer) of the explosion
	 */
	public double getXCurrent(){
		return xCurrent;
	}

	/*
	 * Gets the current y position (outer) of the explosion
	 */
	public double getYCurrent(){
		return yCurrent;
	}

	/*
	 * Gets the current size of the explosion
	 */
	public double getSizeCurrent(){
		return sizeCurrent;
	}

	/*
	 * Tests if anything had collided with the explosions (enemy missiles)
	 */
	public boolean collision(double x, double y){

		int miss = 5;

		if (x > (xCurrent + miss) && x < (xCurrent + sizeCurrent - miss) && y > (yCurrent + miss) && y < (yCurrent + sizeCurrent - miss)){
			return true;
		}
		else
			return false;
	}

}
