/*********************************************************
*  Name: Tommy Chen                                      *
*  Course: ICS 3U1-03  Pd. 1                             *
*  Assignment summative                                  *
*  Purpose: a simple pool game based on a physics engine *
*  Due Date: May 30, 2011                                *
*********************************************************/

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class Impulse implements MouseMotionListener, MouseListener, KeyListener
{
	int xM = 0;
	int yM = 0;
	int ballNum = 16;
	int wallNum = 8;
	int mForce = 0;
	int player1 = 0;
	int player2 = 0;
	boolean isMagnet = false;
	boolean isGravity = false;
	boolean isPrint = false;
	boolean isPause = false;
	boolean is1Turn = true;
	boolean isChangeTurn = true;
	boolean isTurn = true;
	final int mRadius = 50;
	final int keyForce = 1000;
	final int maxF = 20000;
	final int incF = 100;
	final int fps = 100;
	final double cof = 0.15;
	final double cod = 0.1;
	final int maxBall = 1000;
	final int mass = 1000;
	final double cor = 0.7;
	final double corC = 0.95;
	final int radius = 10;
	final double dist = Math.sqrt(3) * radius;
	final int length = 900;
	final int height = 450;
	final int x = 50;
	final int y = 150;
	final int cPocket = 40;
	final int sPocket = 50;
	final int powerBarL = 200;
	final int powerBarW = 20;
	Atom[] ball = new Atom[maxBall];
	Plane[] wall = new Plane[wallNum];
	JFrame frame = new JFrame("Impulse Physics Engine - By: Tommy Chen");
	Drawing draw = new Drawing();
	Dynamics pool = new Dynamics();
	Power shoot = new Power();
	
	public Impulse ()
	{
		frame.getContentPane().setBackground(Color.white);
		frame.getContentPane().add(draw, "Center");
		draw.addMouseMotionListener(this);
		draw.addMouseListener(this);
		frame.addKeyListener(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1024, 740);
		frame.setVisible(true);
		
		Atom.setGravity(isGravity);
		ball[0] = new Atom(x + 150, y + height / 2, radius, mass, cor, corC, 0, 0);
		ball[1] = new Atom(x + length - 200, y + height / 2, radius, mass, cor, corC, 0, 0);
		ball[2] = new Atom(ball[1].x + dist, ball[1].y - radius, radius, mass, cor, corC, 0, 0);
		ball[3] = new Atom(ball[1].x + dist, ball[1].y + radius, radius, mass, cor, corC, 0, 0);
		ball[4] = new Atom(ball[2].x + dist, ball[2].y - radius, radius, mass, cor, corC, 0, 0);
		ball[5] = new Atom(ball[3].x + dist, ball[3].y - radius, radius, mass, cor, corC, 0, 0);
		ball[6] = new Atom(ball[3].x + dist, ball[3].y + radius, radius, mass, cor, corC, 0, 0);
		ball[7] = new Atom(ball[4].x + dist, ball[4].y - radius, radius, mass, cor, corC, 0, 0);
		ball[8] = new Atom(ball[5].x + dist, ball[5].y - radius, radius, mass, cor, corC, 0, 0);
		ball[9] = new Atom(ball[6].x + dist, ball[6].y - radius, radius, mass, cor, corC, 0, 0);
		ball[10] = new Atom(ball[6].x + dist, ball[6].y + radius, radius, mass, cor, corC, 0, 0);
		ball[11] = new Atom(ball[7].x + dist, ball[7].y - radius, radius, mass, cor, corC, 0, 0);
		ball[12] = new Atom(ball[8].x + dist, ball[8].y - radius, radius, mass, cor, corC, 0, 0);
		ball[13] = new Atom(ball[9].x + dist, ball[9].y - radius, radius, mass, cor, corC, 0, 0);
		ball[14] = new Atom(ball[10].x + dist, ball[10].y - radius, radius, mass, cor, corC, 0, 0);
		ball[15] = new Atom(ball[10].x + dist, ball[10].y + radius, radius, mass, cor, corC, 0, 0);
		wall[0] = new Plane(x, y + height, x + length,  y + height);
		wall[1] = new Plane(x, y, x,  y + height);
		wall[2] = new Plane(x + length,  y + height, x + length, y);
		wall[3] = new Plane(x + length, y, x, y);
		wall[4] = new Plane(x + cPocket, y, x, y + cPocket);
		wall[5] = new Plane(x + length, y + cPocket, x + length - cPocket, y);
		wall[6] = new Plane(x, y + height - cPocket, x + cPocket, y + height);
		wall[7] = new Plane(x + length - cPocket, y + height, x + length, y + height - cPocket);
		pool.start();
	}
	
	public void keyPressed(KeyEvent e) 
	{
		int key = e.getKeyCode();
		switch (key) 
		{
			case KeyEvent.VK_BACK_QUOTE :
				isPrint = !isPrint;
				System.out.println("Print: " + isPrint);
				break;
			case KeyEvent.VK_SPACE:
				isPause = !isPause;
				if (isPrint)
					System.out.println("Pause: " + isPause);
				break;
			case KeyEvent.VK_ENTER: 
				frame.setVisible(false);
				new Impulse();
				if (isPrint)
					System.out.println("Refresh");
				break;
			case KeyEvent.VK_F1: 
				isGravity = !isGravity;
				Atom.setGravity(isGravity);
				if (isPrint)
					System.out.println("Gravity: " + isGravity);
				break;
			case KeyEvent.VK_SHIFT: 
				isMagnet = !isMagnet;
				if (isPrint)
					System.out.println("Magnet: " + isMagnet);
				break;
			case KeyEvent.VK_EQUALS  : 
				if (xM > x && xM < x + length && yM > y && yM < y + height)
				{
					ballNum += 1;
					ball[ballNum - 1] = new Atom(xM, yM, radius, mass, cor, corC, 0, 0);
					if (isPrint)
						System.out.println("New ball");
				}
				break;
			case KeyEvent.VK_MINUS :
				if (ballNum > 1)
				{
					ballNum -= 1;
					ball[ballNum] = null;
					if (isPrint)
						System.out.println("Delete ball");
				}
				break;
			case KeyEvent.VK_UP :
				ball[0].applyForce ('u', keyForce);
				break;
			case KeyEvent.VK_DOWN :
				ball[0].applyForce ('d', keyForce);
				break;
			case KeyEvent.VK_LEFT :
				ball[0].applyForce ('l', keyForce);
				break;
			case KeyEvent.VK_RIGHT :
				ball[0].applyForce ('r', keyForce);
				break;
			case KeyEvent.VK_ESCAPE : 
				System.exit(0);
		}
		draw.repaint();
	}
	
	public void keyReleased(KeyEvent e) {} 
	
	public void keyTyped(KeyEvent e) {}
	
	public void  mouseClicked (MouseEvent e) {}
	
	public void  mouseEntered (MouseEvent e) {}
	
	public void  mouseExited (MouseEvent e) {}
	
	public void  mousePressed (MouseEvent e)
	{
		shoot = new Power();
		shoot.start();
		draw.repaint();
	}
	
	public void mouseReleased (MouseEvent e)
	{
		shoot.interrupt();
		ball[0].applyForce(xM, yM, mForce);
		mForce = 0;
		if (isPrint)
			System.out.println("Shoot");
		draw.repaint();
	}
	
	public void mouseDragged (MouseEvent e)
	{
		xM = e.getX();
		yM = e.getY();
		draw.repaint();
	}
	
	public void mouseMoved (MouseEvent e)
	{
		xM = e.getX();
		yM = e.getY();
		draw.repaint();
	}
	
	public static void main (String[] args)
	{
		new Impulse();
	}
	
	class Power extends Thread
	{
		public void run()
		{
			try
			{
				if (isTurn)
				{
					for (; mForce <= maxF; mForce += incF)
					{
						Thread.sleep(1000 / fps);
					}
				}
			}
			catch(InterruptedException e){};
		}
	}
	
	class Drawing extends JComponent
	{
		public Drawing ()
		{
			repaint();
		}
		
		public void paint (Graphics g)
		{
			g.setColor(Color.red);
			if (isTurn)
				g.drawLine(xM, yM, (int)ball[0].x, (int)ball[0].y);
			if (isMagnet)
				g.drawOval(xM - mRadius, yM - mRadius, mRadius * 2, mRadius * 2);
			for (int j = 0; j <= ball[0].radius; j++)
			{
				g.setColor(grayScale(j, ball[0].radius / 3));
				g.fillOval((int)ball[0].x - ball[0].radius + j, (int)ball[0].y - ball[0].radius + j, ball[0].radius * 2 - 2 * j, ball[0].radius * 2 - 2 * j);
			}
			for (int i = 1; i < ballNum; i++)
			{
				for (int j = 0; ball[i] != null && j <= ball[i].radius; j++)
				{
					g.setColor(grayScale(j, ball[i].radius * 2));
					g.fillOval((int)ball[i].x - ball[i].radius + j, (int)ball[i].y - ball[i].radius + j, ball[i].radius * 2 - 2 * j, ball[i].radius * 2 - 2 * j);
				}
			}
			g.setColor(Color.black);
			for (int i = 0; i < wallNum; i++)
			{
				if (i >= 4)
					g.setColor(Color.blue);
				//g.drawOval(wall[i].xMid, wall[i].yMid, 1, 1);
				g.drawLine(wall[i].xEnd1, wall[i].yEnd1, wall[i].xEnd2, wall[i].yEnd2);
			}
			g.setColor(Color.blue);
			g.drawLine(x + length / 2 + sPocket, y, x + length / 2 - sPocket, y);
			g.drawLine(x + length / 2 - sPocket, y + height, x + length / 2 + sPocket, y + height);
			
			g.setColor(Color.blue);
			g.drawRect(x + length / 2 - powerBarL / 2, y - 90, powerBarL, powerBarW);
			g.setColor(Color.red);
			g.fillRect(x + length / 2 - powerBarL / 2 + 1, y - 90 + 1, (powerBarL - 1) * mForce / maxF, powerBarW - 1);
			g.setColor(Color.black);
			g.drawString("Power: " + mForce, x + length / 2 - 20, y - 100);
			if (is1Turn)
			{
				g.setColor(Color.blue);
				g.drawString("Player-1's turn", x + length / 2 - 35, y - 30);
			}
			else
			{
				g.setColor(Color.red);
				g.drawString("Player-2's turn", x + length / 2 - 35, y - 30);
			}
			g.setColor(Color.blue);
			g.drawString("Player-1: " + player1 + " points", x, y - 30);
			g.setColor(Color.red);
			g.drawString("Player-2: " + player2 + " points", x + length - 95, y - 30);
			g.setColor(Color.black);
			g.drawString("enter : restart", x, y + height + 30);
			g.drawString("space : pause", x, y + height + 42);
			g.drawString("esc : quit", x, y + height + 54);
			g.drawString("shift : force field", x, y + height + 66);
			g.drawString("f1 : gravity", x, y + height + 78);
			g.drawString("- : delete a ball", x + 100, y + height + 30);
			g.drawString("= : add a ball", x + 100, y + height + 42);
			g.drawString("arrow keys : push on cue ball", x + 100, y + height + 54);
			g.drawString("` : system messages", x + 100, y + height + 66);
			g.setColor(Color.red);
			if (isPause)
				g.drawString("PAUSED", x + length / 2 - 10, y + height / 2 - 5);
		}
	}
	
	class Dynamics extends Thread
	{
		public void run ()
		{
			try
			{
				while (true)
				{
					for (int i = 0; i < ballNum; i++)
					{
						if (ball[i] != null && isMagnet)
							ball[i].applyMagnet(xM, yM, 10000, mRadius, true);
						for (int j = i + 1; j < ballNum; j++)
							if (ball[j] != null)
								Atom.collide(ball[i], ball[j]);
						if (ball[i] != null)
							ball[i].react(cof, cod);
						if (ball[i] != null && ball[i].isReflect(wall[3], x + length / 2 - sPocket, x + length / 2 + sPocket, y - 100, y + 100) && !isGravity)
							pocket(i);
						if (ball[i] != null && ball[i].isReflect(wall[0], x + length / 2 - sPocket, x + length / 2 + sPocket, y + height - 100, y + height + 100) && !isGravity)
							pocket(i);
						for (int j = 0; ball[i] != null && j < wallNum; j++)
						{
							if (j >= 4 && j <= 7 && ball[i].isReflect(wall[j]) && !isGravity)
								pocket(i);
							if (ball[i] != null)
								ball[i].reflect(wall[j]);
						}
						//if (ball[i] != null && i == 0 && isPrint)
							//System.out.println(ball[0].toString());
						if (ball[i] != null)
							ball[i].locate();
					}
					changeTurn();
					draw.repaint();
					Thread.sleep(1000 / fps);
					while (isPause)
						Thread.sleep(100);
				}
			}
			catch(InterruptedException e) {};
		}
	}
	
	public void changeTurn ()
	{
		final boolean TEMP = isTurn;
		setIsTurn();
		if (TEMP == false && isTurn == true)
		{
			if (isChangeTurn)
				is1Turn = !is1Turn;
			isChangeTurn = true;
		}
	}
	
	public void setIsTurn ()
	{
		isTurn = true;
		for (int i = 0; i < ballNum; i++)
		{
			if (!ball[i].isStationary())
				isTurn = false;
		}
	}
	
	public Color grayScale (int colNum, int scale)
	{
		if (colNum > scale)
			colNum = scale;
		int rgbNum = (int)(((double)colNum / scale) * 255.0);
		return new Color (rgbNum,rgbNum,rgbNum);
	}
	
	public void pocket (int i)
	{
		if (i == 0)
		{
			ball[0] = new Atom(x + length / 2, y + height / 2, radius, mass, cor, corC, 0, 0);
			if (is1Turn)
				player1 -= 2;
			else
				player2 -= 2;
			if (isPrint)
				System.out.println("Ball 0 pocketed");
		}
		else
		{
			ball[i] = new Atom(ball[ballNum - 1]);
			ball[ballNum - 1] = null;
			ballNum -= 1;
			isChangeTurn = false;
			if (is1Turn)
				player1 += 1;
			else
				player2 += 1;
			if (isPrint)
				System.out.println("Ball " + i + " pocketed");
		}
	}
}