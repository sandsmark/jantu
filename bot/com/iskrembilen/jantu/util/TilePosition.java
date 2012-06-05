package com.iskrembilen.jantu.util;


public class TilePosition
{
	private int x;
	private int y;
	public TilePosition(int xin, int yin)
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
	public TilePosition add(TilePosition p)
	{
		return new TilePosition(x() + p.x(), y + p.y());
	}
	public TilePosition add(int x2, int y2)
	{
		return new TilePosition(x() + x2, y + y2);
	}
	public Position toPosition()
	{
		return new Position(x * 32, y * 32);
	}
	public static final TilePosition None = new TilePosition(-1,-1);
	public static final TilePosition Unknown = new TilePosition(-2,-2);
	public static final TilePosition Invalid = new TilePosition(-3,-3);

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
		TilePosition other = (TilePosition) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}
}
