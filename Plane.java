
public class Plane 
{
	int xO;
	int yO;
	private int l1;
	private int l2;
	private double rad;
	double mgt;
	double x;
	double y;
	int xMid;
	int yMid;
	int xEnd1;
	int yEnd1;
	int xEnd2;
	int yEnd2;
	
	public Plane (int xO, int yO, int l1, int l2, double rad, double mgt)
	{
		this.xO = xO;
		this.yO = yO;
		this.mgt = mgt;
		this.rad = rad;
		this.l1 = l1;
		this.l2 = l2;
		locate();
	}
	
	public Plane (int x1, int y1, int x2, int y2)
	{
		xO = (x1 + x2) / 2;
		yO = (y1 + y2) / 2;
		mgt = 0;
		if (x2 < x1)
			rad = Math.atan((double)-(y2 - y1) / (x2 - x1)) + Math.PI / 2;
		else if (x2 > x1)
			rad = Math.atan((double)-(y2 - y1) / (x2 - x1)) + Math.PI / 2 * 3;
		else if (y2 > y1)
			rad = Math.PI / 2 + Math.PI / 2;
		else
			rad = Math.PI / 2 + Math.PI / 2 * 3;
		l1 = l2 = (int)Math.round(Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) / 2);
		locate();
	}
	
	public String toString ()
	{
		return "x:" + x + " y:" + y + " rad:" + rad;
	}
	
	public void rotate (boolean clockwise)
	{
		if (clockwise)
		{
			rad -= Math.PI / 10 * 0.01;
			if (rad <= 0)
				rad += 2 * Math.PI;
		}
		else
		{
			rad += Math.PI / 10 * 0.01;
			if (rad >= 360)
				rad -= 2 * Math.PI;
		}
		locate();
	}
	
	public int xCrd (double x)
	{
		return (int)Math.round(xO + x);
	}
	
	public int yCrd (double y)
	{
		return (int)Math.round(yO - y);
	}
	
	public void point ()
	{
		xMid = xCrd(-x * mgt);
		yMid = yCrd(-y * mgt);
		xEnd1 = xCrd(-x * mgt + Math.cos(rad + Math.PI / 2) * l1);
		yEnd1 = yCrd(-y * mgt + Math.sin(rad + Math.PI / 2) * l1);
		xEnd2 = xCrd(-x * mgt + Math.cos(rad + Math.PI / 2 * 3) * l2);
		yEnd2 = yCrd(-y * mgt + Math.sin(rad + Math.PI / 2 * 3) * l2);
	}
	
	public void locate ()
	{
		x = -Math.cos(rad);
		y = -Math.sin(rad);
		point();
	}
}

