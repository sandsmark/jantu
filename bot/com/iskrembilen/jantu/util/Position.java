package com.iskrembilen.jantu.util;

import java.awt.Point;

public class Position 
{
	private int x;
	private int y;
	public Position(int xin, int yin)
	{
		x = xin;
		y = yin;
	}
	public int x()
	{
		return x;
	}
	public int y()
	{
		return y;
	}
	public TilePosition toTilePosition()
	{
		return new TilePosition(x / 32, y / 32);
	}
	public double getDistanceSquared(Position otherPoint)
	{
		return (x - otherPoint.x()) * (x - otherPoint.x()) + (y - otherPoint.y()) * (y - otherPoint.y());
	}
	public Point getDxDy(Position otherPoint)
	{
		return new Point(otherPoint.x() - x(), otherPoint.y() - y());
	}
	public double getDistance(Position otherPoint)
	{
		return java.lang.Math.sqrt(getDistanceSquared(otherPoint));
	}
	public Point toPoint()
	{
		return new Point(x(), y());
	}
	
	public static final Position None = new Position(-1,-1);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + x;
		result = prime * result + y;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
