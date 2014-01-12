
public class Atom 
{
	private static double ga = 0;
	int radius;
	int mass;
	double cor;
	double corC;
	double x;
	double y;
	private double xV;
	private double yV;
	private double xF;
	private double yF;
	private double xD;
	private double yD;
	private double xA;
	private double yA;
	private double invMass;
	
	public Atom (double x, double y, int radius, int mass, double cor, double corC, double xV, double yV)
	{
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.mass = mass;
		this.cor = cor;
		this.corC = corC;
		this.xV = xV;
		this.yV = yV;
		xF = 0; 
		yF = 0;
		xD = 0;
		yD = 0;
		xA = 0;
		yA = 0;
		invMass = 1.0 / mass;
	}
	
	public Atom (Atom a)
	{
		this(a.x, a.y, a.radius, a.mass, a.cor, a.corC, a.xV, a.yV);
	}
	
	public static void setGravity (boolean on)
	{
		if (on)
			ga = 9.80665 * 0.01;
		else
			ga = 0;
	}
	
	public String toString ()
	{
		return "x:" + x + " y:" + y + " xF:" + xF + " yF:" + yF + " xV:" + xV + " yV:" + yV + " ";
	}
	
	public double distance (double xM, double yM)
	{
		return Math.sqrt((xM - x) * (xM - x) + (yM - y) * (yM - y));
	}
	
	public double angle (double xM, double yM)
	{
		if (x == xM && y == yM)
			return 0;
		else if (x >= xM)
			return Math.atan((y - yM) / (x - xM));
		else
			return Math.atan((y - yM) / (x - xM)) + Math.PI;
	}
	
	public double netV ()
	{
		return Math.sqrt(xV * xV + yV * yV);
	}
	
	public boolean isStationary ()
	{
		return xV == 0 && yV == 0;
	}
	
	public void applyForce (int xM, int yM, double mF)
	{
		xF += (xM - x) / distance(xM, yM) * mF;
		yF += (yM - y) / distance(xM, yM) * mF;
	}
	
	public void applyForce (char dir, double mF)
	{
		if (dir == 'u')
			yF -= mF;
		else if (dir == 'd')
			yF += mF;
		else if (dir == 'l')
			xF -= mF;
		else if (dir == 'r')
			xF += mF;
		else
			;
	}
	
	public void applyElastic (int xM, int yM, double mF)
	{
		xF += (xM - x) * mF; 
		yF += (yM - y) * mF;
	}
	
	public void applyMagnet (int xM, int yM, double mF, int field, boolean repulsion)
	{
		if (distance(xM, yM) - radius <= field && distance(xM, yM) >= 2)
		{
			if (repulsion)
			{
				xF += (field - distance(xM, yM) + radius) / field * mF * Math.cos(angle(xM, yM)); 
				yF += (field - distance(xM, yM) + radius) / field * mF * Math.sin(angle(xM, yM));
			}
			else
			{
				xF += (field - distance(xM, yM) + radius) / field * -mF * Math.cos(angle(xM, yM)); 
				yF += (field - distance(xM, yM) + radius) / field * -mF * Math.sin(angle(xM, yM));
			}
		}
	}
	
	public void applyCollide (int xM, int yM, int field)
	{
		if (distance(xM, yM) <= field)
		{
			xF += (x - xM) / distance(xM, yM) * netV() * 2 * mass;
			yF += (y - yM) / distance(xM, yM) * netV() * 2 * mass - ga * mass;
		}
	}
	
	public void applyFriction (double cof)
	{
		final double netFk = -mass * 0.098 * cof;
		if (Math.abs(netFk / mass) >= netV())
		{
			xF -= xV * mass;
			yF -= yV * mass;
		}
		else if (xV != 0 ||	yV != 0)
		{
			xF += netFk / netV() * xV;
			yF += netFk / netV() * yV;
		}
	}
	
	public void initReact (double cof)
	{
		if (ga == 0)
			applyFriction (cof);
		xA = xF / mass;
		yA = yF / mass;
		xV += xA;
		yV += yA + ga;
	}
	
	public void applyDrag (double cod)
	{
		final double TEMP = -1.292 * 0.47 * Math.PI * radius * radius / 2 * 0.01 * cod;
		xD = xV * xV * TEMP;
		yD = yV * yV * TEMP;
	}
	
	public void react (double cof, double cod)
	{
		initReact(cof);
		applyDrag(cod);
		xA = xD / mass;
		yA = yD / mass;
		xV += xA * Math.signum(xV);
		yV += yA * Math.signum(yV);
	}
	
	public boolean isReflect (Plane p)
	{
		return (x - p.xO) * p.x + (y - p.yO) * -p.y + p.mgt < radius 
			&& xV * p.x + yV * -p.y < 0;
	}
	
	//reflect on limited area
	public boolean isReflect (Plane p, int l, int r, int u, int d)
	{
		return isReflect(p) && x >= l && x <= r && y >= u && y <= d;
	}
	
	//impulse method
	public boolean isSpecReflect (Plane p)
	{
		return (x - p.xO) * p.x + (y - p.yO) * -p.y + p.mgt - radius 
			< Math.abs(xV * p.x + yV * -p.y) && xV * p.x + yV * -p.y < 0;
	}
	
	public void reflect (Plane p)
	{
		if (isReflect(p))
		{
			final double Temp = (xV * p.x + yV * -p.y) * (1 + cor);
			xV = xV - p.x * Temp;
			yV = yV - -p.y * Temp;
		}
	}
	
	public void reflect (Plane p, int l, int r, int u, int d)
	{
		if (isReflect(p, l, r, u, d))
		{
			final double Temp = (xV * p.x + yV * -p.y) * (1 + cor);
			xV = xV - p.x * Temp;
			yV = yV - -p.y * Temp;
		}
	}
	
	public void locate ()
	{
		x += xV;
		y += yV;
		xF = 0; 
		yF = 0;
	}
	
	public static boolean isCollide (Atom a, Atom b)
	{
		return a.distance(b.x, b.y) < a.radius + b.radius
			&& (a.xV - b.xV) * (a.x - b.x) + (a.yV - b.yV) * (a.y - b.y) <= 0;
	}
	//not working
	public static boolean isSpecCollide (Atom a, Atom b)
	{
		return a.distance(b.x, b.y) - a.radius - b.radius
			< -((a.xV - b.xV) * (a.x - b.x) + (a.yV - b.yV) * (a.y - b.y)) //relative normal velocity
			&& (a.xV - b.xV) * (a.x - b.x) + (a.yV - b.yV) * (a.y - b.y) <= 0;
	}
	
	public static void collide (Atom a, Atom b)
	{
		if (isCollide(a, b))
		{
			final double TEMP = ((a.xV - b.xV) * Math.cos(a.angle(b.x, b.y)) + 
				(a.yV - b.yV) * Math.sin(a.angle(b.x, b.y))) / (a.invMass + b.invMass);
			a.xV -= (1 + a.corC) * Math.cos(a.angle(b.x, b.y)) * TEMP * a.invMass;
			a.yV -= (1 + a.corC) * Math.sin(a.angle(b.x, b.y)) * TEMP * a.invMass;
			b.xV += (1 + b.corC) * Math.cos(a.angle(b.x, b.y)) * TEMP * b.invMass;
			b.yV += (1 + b.corC) * Math.sin(a.angle(b.x, b.y)) * TEMP * b.invMass;
		}
	}
	
	public static void softCollide (Atom a, Atom b, double mF)
	{
		if (isCollide(a, b))
		{
			final double TEMP = (b.radius - a.distance(b.x, b.y) + a.radius) * mF;
			a.xF = TEMP / b.radius * Math.cos(a.angle(b.x, b.y)); 
			a.yF = TEMP / b.radius * Math.sin(a.angle(b.x, b.y));
			b.xF = TEMP / a.radius * Math.cos(b.angle(a.x, a.y)); 
			b.yF = TEMP / a.radius * Math.sin(b.angle(a.x, a.y));
		}
	}
}
