package VKBot.Bot;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


abstract class Object 
{
	private double x;
	private double y;
	public BufferedImage image;
	private int WIDTH;
	private int HEIGHT;
	
	public Object(double x, double y, int width, int height)
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
	public double getX()
	{
		return x;
	}
	
	public double getY()
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
	
	public void setX(double x)
	{
		this.x = x;
	}
	
	public void addToX(double x)
	{
		this.x += x;
	}
	
	public void setY(double y)
	{
		this.y = y;
	}
	
	public void addToY(double y)
	{
		this.y += y;
	}
	
}
