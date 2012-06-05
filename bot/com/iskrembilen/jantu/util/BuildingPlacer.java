package com.iskrembilen.jantu.util;
import com.iskrembilen.jantu.util.BWColor;
import com.iskrembilen.jantu.util.TilePosition;

import com.iskrembilen.jantu.types.UnitType;
import com.iskrembilen.jantu.types.UnitType.UnitTypes;
import com.iskrembilen.jantu.model.Unit;
import com.iskrembilen.jantu.util.RectangleArray;
import java.util.Set;

import com.iskrembilen.jantu.JNIBWAPI;

public class BuildingPlacer 
{
	private JNIBWAPI bwapi;
	
	private RectangleArray reserveMap = new RectangleArray();
	private int buildDistance;
	
	public BuildingPlacer(JNIBWAPI jniBwapi)
	{
		bwapi = jniBwapi;
		reserveMap.resize(bwapi.getMap().getWidth(), bwapi.getMap().getHeight());
		reserveMap.setTo(false);
		System.out.println("Width:" + bwapi.getMap().getWidth());
		System.out.println("Height:" + bwapi.getMap().getHeight());
		this.buildDistance = 1;
	}
	
	/** returns true if we can build this type of unit here. Take into account reserved tiles. */
	public boolean canBuildHere(TilePosition pos, UnitType typ)
	{
		try
		{
			//if(typ.getRaceID() )
			/*
			if(typ.isRequiresCreep() && !bwapi.hasCreep(pos.x(), pos.y()))
			{
				return false;
			}
			*/
			if(!bwapi.canBuildHere(-1, pos.x(), pos.y(), typ.getID(), false))
			{
				return false;
			}
			for(int x = pos.x(); x < pos.x() + typ.getTileWidth(); x++)
			{
				for(int y = pos.y(); y < pos.y() + typ.getTileHeight(); y++)
				{
					if(reserveMap.getItem(x, y))
					{
						return false;
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace(System.out);
		}
		return true;
	}
	
	public boolean canBuildHereWithSpace(TilePosition pos, UnitType typ)
	{
		return canBuildHereWithSpace(pos, typ, this.buildDistance);
	}
	
	/** returns true if we can build this type of unit here with the specified amount of space.
	 *  space value is stored in this.buildDistance
	 */
	public boolean canBuildHereWithSpace(TilePosition pos, UnitType typ, int buildDist)
	{
		//if we can't build here, we of course can't build here with space.
		if(!this.canBuildHere(pos, typ))
		{
			//bwapi.drawCircle(pos.toPosition().x(), pos.toPosition().y(), 5, BWColor.RED, true, false);
			return false;
		}
		int width = typ.getTileWidth();
		int height = typ.getTileHeight();
		
		//make sure we leave space for add-ons. These types of units can have addons:
		if( typ.getID()==UnitTypes.Terran_Command_Center.ordinal() ||
			typ.getID()==UnitTypes.Terran_Factory.ordinal() ||
			typ.getID()==UnitTypes.Terran_Starport.ordinal() ||
			typ.getID()==UnitTypes.Terran_Science_Facility.ordinal())
		{
			width += 2;
		}
		int startx = pos.x() - buildDist;
		if(startx<0) return false;
		int starty = pos.y() - buildDist;
		if(starty<0) return false;
		int endx = pos.x() + width + buildDist;
		if(endx>bwapi.getMap().getWidth()) return false;
		int endy = pos.y() + height + buildDist;
		if(endy>bwapi.getMap().getHeight()) return false;
		
		

		if( !typ.isRefinery())
		{
			for(int x = startx; x < endx; x++)
			{
				for(int y = starty; y < endy; y++)
				{
					if(!buildable(x,y) || reserveMap.getItem(x, y))
					{
						return false;
					}
				}
			}
		}
		
		
		
//		if (pos.x() > 3)
//		{
//			int startx2 = startx - 2;
//			if(startx2 < 0) startx2 = 0;
//			for(int x = startx2; x < startx; x++)
//			{
//				for(int y = starty; y < endy; y++)
//				{
//					for(Unit u : bwapi.getMap().getUnitsOnTile(new TilePosition(x, y)))
//					{
//
//						if(!u.isLifted())
//						{
//							int t2 = u.getTypeID();
//							if (t2 == UnitTypes.Terran_Command_Center.ordinal() ||
//								t2 == UnitTypes.Terran_Factory.ordinal() ||
//								t2 == UnitTypes.Terran_Starport.ordinal() ||
//								t2 == UnitTypes.Terran_Science_Facility.ordinal())
//							{
//								return false;
//							}
//						}
//						
//					}
//				}
//			}
//		}

		return true;
	}
	
	public TilePosition getBuildLocation(UnitType typ)
	{
		//returns a valid build location if one exists, scans the map left to right
		for(int x = 0; x < bwapi.getMap().getWidth(); x++)
		{
			for(int y = 0; y < bwapi.getMap().getHeight(); y++)
			{
				if(this.canBuildHere(new TilePosition(x,y), typ))
				{
					return new TilePosition(x,y);
				}
			}
		}
		return TilePosition.None;
	}
	
	public TilePosition getBuildLocationNear(TilePosition position, UnitType typ)
	{
		return getBuildLocationNear(position, typ, this.buildDistance);
	}
	
	//Returns a valid build location near the specified tile position.
	//Searches outward in a spiral.
	public TilePosition getBuildLocationNear(TilePosition position, UnitType typ, int buildDist)
	{
		System.out.print(typ.getName());
		int x = position.x();
		int y = position.y();
		int length = 1;
		int j = 0;
		boolean first = true;
		int dx = 0;
		int dy = 1;
		while(length < bwapi.getMap().getWidth()) //We'll ride the sprial to the end
		{
			//if we can build here, return this tile position
			if(x >= 0 && x < bwapi.getMap().getWidth() && y >= 0 && y < bwapi.getMap().getHeight())
			{
				if(canBuildHereWithSpace(new TilePosition(x,y), typ, buildDist))
				{
					return new TilePosition(x,y);
				}
			}
		
			//otherwise move to another position
			x = x + dx;
			y = y + dy;
			//count how many steps we take in this direction
			j++;
			if(j == length) //if we've reached the end, it's time to turn
			{
				//reset step counter;
				j = 0;
				
				//Spiral out. Keep going.
				if(!first)
					length++; //increment step counter if needed
				
				//first=true for every other turn so we spiral out at the right rate
				first = !first;
				
				//turn counter clockwise 90 degrees:
				if(dx == 0)
				{
					dx = dy;
					dy = 0;
				}
				else
				{
					dy = -dx;
					dx = 0;
				}
			}
			//Spiral out. Keep going.
		}
		
		return TilePosition.None;
	}
	
	public boolean buildable(int x, int y)
	{	
		if(!bwapi.getMap().isBuildable(x, y)) return false;
//		for(Unit u : bwapi.getMap().getUnitsOnTile(new TilePosition(x,y)))
//		{
//			if(bwapi.getUnitType(u.getTypeID()) != null)
//			{
//				if(bwapi.getUnitType(u.getTypeID()).isBuilding() && u.isLifted() == false)
//				{
//					return false;
//				}
//			}
//		}
		return true;
		
	}
	
	public void reserveTiles(TilePosition position, int width, int height)
	{
		for(int x = position.x(); x < position.x() + width && x < (int)reserveMap.getWidth(); x++)
		{
			for(int y = position.y(); y < position.y() + height && y < (int)reserveMap.getHeight(); y++)
				reserveMap.setItem(x, y, true);
		}
	}
	
	public void freeTiles(TilePosition position, int width, int height)
	{
		for(int x = position.x(); x < position.x() + width && x < (int)reserveMap.getWidth(); x++)
		{
			for(int y = position.y(); y < position.y() + height && y < (int)reserveMap.getHeight(); y++)
				reserveMap.setItem(x, y, false);
		}
	}
	
	public void setBuildDistance(int distance)
	{
		this.buildDistance = distance;
	}
	
	public int getBuildDistance()
	{
		return this.buildDistance;
	}
	
	public boolean isReserved(int x, int y)
	{
		return reserveMap.getItem(x, y);
	}
	
	
}
