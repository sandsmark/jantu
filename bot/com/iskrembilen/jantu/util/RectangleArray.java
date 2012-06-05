package com.iskrembilen.jantu.util;

public class RectangleArray {
	
	private int width;
	private int height;
	
	private boolean[] data;
	
	public RectangleArray()
	{
		width = 1;
		height = 1;
		resize(width,height);
	}
	
	public RectangleArray(int win, int hin)
	{
		width = win;
		height = hin;
		resize(width, height);
	}
	
	public int getWidth()
	{
		return width;
	}
	public int getHeight()
	{
		return height;
	}
	public boolean getItem(int x, int y)
	{
		return data[x + y * getWidth()];
	}
	
	
	public void setItem(int x, int y, boolean newItem)
	{
		data[x + y * getWidth()] = newItem;
	}
	
	
	public void setTo(boolean newItem)
	{
		for(int iy = 0; iy < getHeight(); iy++)
		{
			for(int ix = 0; ix < getWidth(); ix++)
			{
				data[ix + iy * getWidth()] = newItem;
			}
		}
	}
	
	public void resize(int win, int hin)
	{
		data = new boolean[win * hin];
	}

}
