package VKBot.Bot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

abstract class Object 
{
	private int x;
	private int y;
	public BufferedImage image;
	private int WIDTH;
	private int HEIGHT;
	
	public Object(int x, int y, int width, int height)
	{
		this.x = x;
		this.y = y;
		this.HEIGHT = height;
		this.WIDTH = width;
		try 
		{
			image = ImageIO.read(new File("C:\\Users\\maxim\\Desktop\\image.jpg"));
		}catch(IOException ex)
		{
			
		}
	}
	
	public BufferedImage getImage()
	{
		return image;
	}
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getHeight()
	{
		return HEIGHT;
	}
	
	public int getWidth()
	{
		return WIDTH;
	}
	
	public void setX(int x)
	{
		this.x = x;
	}
	
	public void addToX(int x)
	{
		this.x += x;
	}
	
	public void setY(int y)
	{
		this.y = y;
	}
	
	public void addToY(int y)
	{
		this.y += y;
	}
}
