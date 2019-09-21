package VKBot.Bot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Vector;

import javax.swing.*;
import VKBot.Bot.Object;


public class App extends JPanel implements Runnable
{
	JFrame frame;

	private static final int DELAY = 17;
	private final int RECT_HEIGHT = 10;
	private final int RECT_WIDTH = 50;
	private final int ALIEN_H = 20;
	private final int ALIEN_W = 40;
	public double ALIEN_SPEED = 0.5;
	
	public  int RECT_X = 250;
	public  int RECT_Y = 500;

	public Vector<Shot> shots;
	public Player player;
	public Vector<Alien> aliens;
	public boolean ingame = true;
	public boolean isDefeated = false;
	public Thread animator;
	public JButton levels[];
	
    public App() {
    	
    	initApp();
    	gameInit();
    }
    
    public void initApp()
    {
    	frame = new JFrame("Space Invaders");
        frame.setSize(600, 600);
        frame.setTitle("Points");
        frame.addKeyListener(new MyKeyAdapter());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.add(this);

    }

//    public void startScreen()
//    {
//    	frame.setLayout(new FlowLayout());
//    	levels = new JButton[2];
//    
//   		levels[0] = new JButton("First");
//   		levels[0].addActionListener(this);
//        	
//    	levels[1] = new JButton("Second");
//    	levels[1].addActionListener(this);
//    	
//    }
    //add aliens here
    public void gameInit()
    {
    	shots = new Vector();
    	aliens = new Vector();
    	player = new Player(RECT_X, RECT_Y, RECT_WIDTH, RECT_HEIGHT);
    	
    	fillAliens();
    	animator = new Thread(this);
    	animator.start();
    }
    
    public void paintComponent(Graphics g)
    {
    	super.paintComponent(g);
    	
    	if(ingame)
    	{
    		drawPlayer(g);
    		drawAliens(g);
    		drawShot(g);
    		g.drawImage(player.getImage(), player.getX(), player.getY(), player.getWidth(), player.getHeight(), this);
    	}
    }
    
    public void fillAliens()
    {
    	
    	trapezeAliens();
    }
    
    //Forms of waves of aliens
    public void squareAliens()
    {
    	int x = 190, y = 50;
    	for(int i = 0; i < 3; i++)
		{
    		for(int j = 0; j < 4; j++)
    		{
    			aliens.add(new Alien(x, y));
    			x += 50;
    		}
    		x = 190;
    		y += 30;
		}
    }
    
    public void trapezeAliens()
    {
    	int x = 100, y = 50;
    	for(int i = 0; i < 7; i++)
    	{
    		aliens.add(new Alien(x, y));
    		x += 50;
    	}
    	//left side
    	x = 130;
    	y += 30;
    	aliens.add(new Alien(x, y));
    	
    	x = 160;
    	y += 30;
    	aliens.add(new Alien(x, y));
    	
    	
    	//right side
    	x = 370;
    	y = 80;
    	aliens.add(new Alien(x, y));
    	
    	x = 340;
    	y += 30;
    	aliens.add(new Alien(x, y));
    	
    	x = 200;
    	y += 30;
    	for(int i = 0; i < 3; i++)
    	{
    		aliens.add(new Alien(x, y));
    		x += 50;
    	}
    }
    
    public void drawShot(Graphics g)
    {
    	if(ingame)
    	{
    		for(int i = 0; i < shots.size(); i++)
    		{
    			g.drawRect(shots.elementAt(i).x + 15, shots.elementAt(i).y, 6, 10);
    		}
    	}
    }
    
    public void drawAliens(Graphics g)
    {
    	for(int i = 0; i < aliens.size(); i++)
    	{
    		g.drawRect((int)aliens.elementAt(i).x, (int)aliens.elementAt(i).y, ALIEN_W, ALIEN_H);
    	}
		
    }
    
    public void drawPlayer(Graphics g)
    {
    	g.drawRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
    
    public void defeat()
    {
    	System.out.println("You lose");
    }
    
    public void victory()
    {
    	System.out.println("You win!");
    }
    
    //check collision
    public void collide()
    {
    	for(int i = 0; i < shots.size(); i++)
    	{
    		
    		if(shots.elementAt(i).y < 0)
    		{
    			shots.remove(i);
    		}
   
    		for(int j = 0; j < aliens.size(); j++)
    		{
    			if(shots.size() == 0)
    				break;
    			
    			//shots.elementAt(i).y >= aliens.elementAt(j).y mistake somewhere here
    			if(shots.elementAt(i).x < aliens.elementAt(j).x + ALIEN_W/2 && shots.elementAt(i).x > aliens.elementAt(j).x - ALIEN_W/2
    					&& shots.elementAt(i).y >= aliens.elementAt(j).y && shots.elementAt(i).y <= aliens.elementAt(j).y + ALIEN_H)
    			{
    				shots.remove(i);
    				aliens.remove(j);
    			}
    		}

    	}
    }
    
    
    public void run()
    {
    	long beforeTime, timeDiff, sleep;
    	
    	beforeTime = System.currentTimeMillis();
    	
    	while(ingame)
    	{
    		
    		repaint();
    		//animation();
    		
    		for(int i = 0; i < shots.size(); i++)
    			shots.elementAt(i).y -= 10;
    		
    		
    		for(int j = 0; j < aliens.size(); j++)
    		{
    			if(aliens.elementAt(j).y >= player.getY() - 2*RECT_HEIGHT)
    			{
    				isDefeated = true;
    				break;
    			}
    			aliens.elementAt(j).y += ALIEN_SPEED;
    			collide();
    		}
    		
    		if(aliens.size() == 0)
    		{
    			ingame = false;
    			victory();
    		}

    		if(isDefeated)
    		{
    			ingame = false;
    			defeat();		
    		}
    		
    		timeDiff = System.currentTimeMillis() - beforeTime;
    		sleep = DELAY - timeDiff;
    		
    		if(sleep < 0)
    		{
    			sleep = 4;
    		}
    		
    		try
    		{
    			Thread.sleep(sleep);
    		}catch(InterruptedException e)
    		{
    			System.out.println("Interrupted");
    		}
    		
    		beforeTime = System.currentTimeMillis();
    	}
    }
    
    

    public static void main(String[] args) {

        App a = new App();
    }
    
    
    class MyKeyAdapter extends KeyAdapter
    {
    	
    	public void keyPressed(KeyEvent ke)
    	{
    		if(ke.getKeyCode() == KeyEvent.VK_RIGHT)
    		{
    			if((player.getX() + RECT_WIDTH) < getSize().width - 1)
    			{
    				player.addToX(20);
    				repaint();
    			}
    		}
    		else if(ke.getKeyCode() == KeyEvent.VK_LEFT) 
    		{
    			if(player.getX() > 0)
    			{
    				player.addToX(-20);
    				repaint();
    			}
    		}
    		else if(ke.getKeyCode() == KeyEvent.VK_SPACE)
    		{
    			shots.add(new Shot(player.getX(), player.getY()));
				
    		}
    	}
    }
}

//TODO: with abstract class contains(coords, state, image)

class Alien
{
	//change all members to private
	public double x;
	public double y;
	public boolean isVisible = true;
	
	Alien(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	//TODO: getters and setters
}

class Shot  
{
	//change all members to private
	public static int S_WIDTH = 6;
	public static int S_HEIGHT = 10;
	public boolean isVisible = true;
	public int x;
	public int y;
	public Shot()
	{
		
	}
	
	public Shot(int x, int y)
	{
		this.x = x + S_WIDTH;
		this.y = y - S_HEIGHT;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
}

class Player extends Object
{
	Player(int x, int y, int w, int h)
	{
		super(x, y, w, h);
	}
}

