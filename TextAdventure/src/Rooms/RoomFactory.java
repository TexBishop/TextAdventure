/**
 * Group Project II: a Text Adventure
 * @author Tex Bishop
 * @version 1.0
 * @date 05-12-2019
 */

package Rooms;

import Structure.GameState;

import Rooms.CustomRooms.FarmhousePorch;
import Rooms.CustomRooms.OldFarmhouse;
import Rooms.CustomRooms.BackArea.*;
import Rooms.CustomRooms.Forest.*;
import Rooms.CustomRooms.House.*;

public class RoomFactory 
{
	private GameState gameState;
	
	public RoomFactory(GameState gameState)
	{
		this.gameState = gameState;
	}
	
	public void makeRoom(String room)
	{
		switch(room)
		{
		case "Old Farmhouse": new OldFarmhouse(this.gameState);
			break;
		case "Farmhouse Porch": new FarmhousePorch(this.gameState);
			break;
		case "Edge of Forest": new EdgeOfForest(this.gameState);
			break;
		case "Forest Path": new ForestPath(this.gameState);
			break;
		case "Forest Clearing": new ForestClearing(this.gameState);
			break;
		case "Forest Cliff": new ForestCliff(this.gameState);
			break;
		case "Forest Crossroads": new ForestCrossroads(this.gameState);
			break;
		case "Cave Entrance": new CaveEntrance(this.gameState);
			break;
		case "Cave Interior": new CaveInterior(this.gameState);
			break;
		case "Forest Exit": new ForestExit(this.gameState);
			break;
		case "Back of Cornfield": new BackOfCornfield(this.gameState);
			break;
		case "Backyard": new Backyard(this.gameState);
			break;
		case "ToolShed": new ToolShed(this.gameState);
			break;
		case "Shed Basement": new ShedBasement(this.gameState);
			break;
		case "Barn": new Barn(this.gameState);
			break;
		case "Barn Storage": new BarnStorage(this.gameState);
			break;
		case "Cornfield Entrance": new CornfieldEntrance(this.gameState);
			break;
		case "Cornfield Center": new CornfieldCenter(this.gameState);
			break;
		case "Entryway": new Entryway(this.gameState);
			break;
		case "Dining Room": new DiningRoom(this.gameState);
			break;
		case "Kitchen": new Kitchen(this.gameState);
			break;
		case "Living Room": new LivingRoom(this.gameState);
			break;
		case "Upstairs Hallway": new UpstairsHallway(this.gameState);
			break;
		case "Guest Room": new GuestRoom(this.gameState);
			break;
		case "Master Bedroom": new MasterBedroom(this.gameState);
			break;
		
		default:
			System.out.println("Room creation failed, default reached, due to no room match. ");
		}
	}
}
