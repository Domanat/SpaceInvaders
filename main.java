package VKBot.Bot;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
	
	
	public  int RECT_X = 250;
	public  int RECT_Y = 500;

	public Vector<Shot> shots;
	public Player player;
	public Vector<Alien> aliens;
	
	public boolean ingame = true;
	public boolean isDefeated = false;
	public boolean isWin = false;
	public Thread animator;
	BufferedImage image;
	BufferedImage bgImage;
	
    public App() 
    {
    	gameInit();
    	initApp();
    	
    }
    
    public void initApp()
    {
    	frame = new JFrame("Space Invaders");
    	try
    	{
    		bgImage = ImageIO.read(new File("C:\\Users\\maxim\\Desktop\\bgImage.jpg"));
    	}catch(IOException e)
    	{
    		System.out.println("Can't download background image");
    	}
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
    		g.drawImage(bgImage, 0, 0, 600, 600, this);

    		drawPlayer(g);
    		
    		drawAliens(g);
    		drawShot(g);
    		//g.drawImage(player.getImage(), (int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight(), this);
    	}
    	else if(isDefeated)
    	{
    		defeat();
    		
    		//draw loser screen or image 
    		try 
    		{
    			 image = ImageIO.read(new File("C:\\Users\\maxim\\Desktop\\loser.jpg"));
    			g.drawImage(image, 0, 0, 600, 600, this);
    		}catch(IOException ex)
    		{
    			
    		}
    	}
    	else if(isWin)
    	{
    		victory();
    		
    		try
    		{
    			image = ImageIO.read(new File("C:\\Users\\maxim\\Desktop\\winner.jpg")); 
    			g.drawImage(image, 0, 0, 600, 600, this);
    		}catch(IOException ex)
    		{
    			
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
    			shots.elementAt(i).addToY(-10);
    		
    		
    		for(int j = 0; j < aliens.size(); j++)
    		{
    			if(aliens.elementAt(j).getY() >= player.getY() - 2*RECT_HEIGHT)
    			{
    				isDefeated = true;
    				ingame = false;
    				break;
    			}
    			aliens.elementAt(j).addToY(aliens.elementAt(j).getSpeed()); //+= ALIEN_SPEED;
    			collide();
    		}
    		
    		if(aliens.size() == 0)
    		{
    			ingame = false;
    			isWin = true;
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
    			aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
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
    		aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
    		x += 50;
    	}
    	//left side
    	x = 130;
    	y += 30;
    	aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
    	
    	x = 160;
    	y += 30;
    	aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
    	
    	
    	//right side
    	x = 370;
    	y = 80;
    	aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
    	
    	x = 340;
    	y += 30;
    	aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
    	
    	x = 200;
    	y += 30;
    	for(int i = 0; i < 3; i++)
    	{
    		aliens.add(new Alien(x, y, ALIEN_W, ALIEN_H));
    		x += 50;
    	}
    }
    
    public void drawShot(Graphics g)
    {
    	g.setColor(Color.ORANGE);
    	if(ingame)
    	{
    		for(int i = 0; i < shots.size(); i++)
    		{
    			g.fillRect((int)shots.elementAt(i).getX()+ 15, (int)shots.elementAt(i).getY(), 6, 10);
    		}
    	}
    }
    
    public void drawAliens(Graphics g)
    {
    	g.setColor(Color.GREEN);
    	for(int i = 0; i < aliens.size(); i++)
    	{
    		g.fillRect((int)aliens.elementAt(i).getX(), (int)aliens.elementAt(i).getY(), ALIEN_W, ALIEN_H);
    	}
		
    }
    
    public void drawPlayer(Graphics g)
    {
    	g.setColor(Color.WHITE);
    	g.fillRect((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
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
    		
    		if(shots.elementAt(i).getY() < 0)
    		{
    			shots.remove(i);
    		}
   
    		for(int j = 0; j < aliens.size(); j++)
    		{
    			if(shots.size() == 0)
    				break;
    			
    			//shots.elementAt(i).y >= aliens.elementAt(j).y mistake somewhere here
    			if(shots.elementAt(i).getX() < aliens.elementAt(j).getX() + ALIEN_W/2 && shots.elementAt(i).getX() > aliens.elementAt(j).getX() - ALIEN_W/2
    					&& shots.elementAt(i).getY() >= aliens.elementAt(j).getY() && shots.elementAt(i).getY() <= aliens.elementAt(j).getY() + ALIEN_H)
    			{
    				shots.remove(i);
    				aliens.remove(j);
    			}
    		}

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

class Alien extends Object
{
	//change all members to private
	private double ALIEN_SPEED = 0.5;
	
	Alien(int x ,int y, int w, int h)
	{
		super(x, y, w, h);
	}
	
	public double getSpeed()
	{
		return ALIEN_SPEED;
	}
}

class Shot 
{
	//change all members to private
	private static int S_WIDTH = 6;
	private static int S_HEIGHT = 10;
	private double x;
	private double y;
	
	public Shot(double x, double y)
	{
		this.x = x + S_WIDTH;
		this.y = y - S_HEIGHT;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public void addToY(double y)
	{
		this.y += y;
	}
}

class Player extends Object
{
	Player(int x, int y, int w, int h) 
	{
		super(x, y, w, h);
	}
}

