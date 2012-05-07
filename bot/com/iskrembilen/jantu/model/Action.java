package com.iskrembilen.jantu.model;


public class Action {
	public enum ActionType {
		Attack,
		Move,
		Build,
		Morph,
		Mine,
	}
	
	public int x = -1;
	public int y = -1;
	
	public ActionType type;
	
	public Unit unit;
	
	public int buildingType = -1;
	public int targetType = -1;
}
