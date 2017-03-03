package com.example.alticast.test;

public class Line_Missiles {

	private double xStart; //starting x position of the missile
	private double yStart; //starting y position of the missile

	private double xDestination; //x destination of the missile
	private double yDestination; //y destination of the missile

	private double xCurrent; //current x position of the missile
	private double yCurrent; //current y position of the missile

	private double speed; //speed of the missile (decides the number of updates required before the missile reaches the destination)

	private double xIncrement; //how fast the x value of the missile head changes
	private double yIncrement; //how fast the y value of the missile head changes

	Line_Missiles(double xS, double yS, double xD, double yD, double speed){

		/*
		 * sets the input parameters to the values found in this class and modified by the programmer
		 * and player
		 */

		xStart = xS;
		yStart = yS;

		xDestination = xD;
		yDestination = yD;

		xCurrent = xStart;
		yCurrent = yStart;

		this.speed = speed;

		xIncrement = ((xDestination-xStart)/speed); //decides the x increment for the missile based on the speed and distance to destination
		yIncrement = ((yDestination-yStart)/speed); //decides the y increment for the missile based on the speed and distance to destination
	}

	/*
	 * Update the position (head) of the ally missile (the missiles the player controls)
	 * Returns true, if the missiles are updated
	 * Returns false, if the missiles have reached their destination and are not updated
	 */
	public boolean updateMissile(){

		//calculates whether the missiles have reached their destination
		if (yDestination > yStart && yCurrent < yDestination || yDestination < yStart && yCurrent > yDestination){
			xCurrent+=xIncrement;
			yCurrent+=yIncrement;

			return true;
		}
		return false;
	}

	/*
	 * Gets the speed of the missile
	 */
	public double getSpeed(){
		return speed;
	}

	/*
	 * Gets the starting x position of the missile
	 */
	public double getXStart(){
		return xStart;
	}

	/*
	 * Gets the starting y position of the missile
	 */
	public double getYStart(){
		return yStart;
	}

	/*
	 * Gets the current x position (head) of the missile
	 */
	public double getXCurrent(){
		return xCurrent;
	}

	/*
	 * Gets the current y position (head) of the missile
	 */
	public double getYCurrent(){
		return yCurrent;
	}
}
