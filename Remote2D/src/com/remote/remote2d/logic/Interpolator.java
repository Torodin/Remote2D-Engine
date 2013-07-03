package com.remote.remote2d.logic;

public class Interpolator {
	
	public static double linearInterpolate(double y1, double y2, double mu)
	{
		return(y1*(1-mu)+y2*mu);
	}
	
	public static Vector2D linearInterpolate(Vector2D y1, Vector2D y2, double mu)
	{
		return new Vector2D((int)linearInterpolate(y1.x,y2.x,mu),(int)linearInterpolate(y1.y,y2.y,mu));
	}
	
	public static double cosineInterpolate(double y1, double y2, double mu)
	{
		double mu2 = (1-Math.cos(mu*3.1415926535d))/2;
		return(y1*(1-mu2)+y2*mu2);
	}
	
	public static double cubicInterpolate(double y0, double y1, double y2, double y3, double mu)
	{
		double a0,a1,a2,a3,mu2;
		
		mu2 = mu*mu;
		a0 = y3 - y2 - y0 + y1;
		a1 = y0 - y1 - a0;
		a2 = y2 - y0;
		a3 = y1;
		return(a0*mu*mu2+a1*mu2+a2*mu+a3);
	}
}
