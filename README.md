# TextAdventure
A text adventure game with a ligthweight GUI.


Section 1 – User Interaction
1.	Game Type / Description :  A traditional text adventure game, with the addition of a GUI.   
	•	Game locations will be will be defined as rooms   
	•	Player will interact with things within the room, and move between rooms, by typing commands into the interface.   
	•	The command area will simulate a command line environment.   
2.	GUI   
	•	The GUI will consist of three elements:   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	A link bar   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	An image   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	The command area   
	•	The link bar will contain buttons to do the following:   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Restart the game   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Save the game   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Load a saved game   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Open the Inventory window   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Open the Help window   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Exit the game   
	•	The image area will contain an ASCII art image   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	ASCII art images must be created using these parameters:   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;a)	The starting image must be 800 x 460 pixels   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;b)	Convert the image at a width of 157 characters   
	•	The command area will simulate a command line environment   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	There will be a prompt “>>”   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	Any text prior to the prompt will not be editable   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	The up and down arrow keys can be used to scroll through previously typed commands   
	•	The inventory window will list the player’s current inventory list   
	•	The Help window will contain a list of instructions for playing the game   
3.	Commands   
	•	Commands can be written freeform without structure, aside from one rule:  The verb should be the first word in the command   
	•	If the user is having trouble getting an inventory item to do something, focus can be forced to the inventory using the keywords "inventory" or "my", and the item.   


Section 2 – System
1.	GameState   
	•	This will be the central point of the system   
	•	It will store all progression variables, and provide methods to interact with those variables, which will include:   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	A hashmap of all Spaces   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	A hashmap of all Flags   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	A hashmap of inventory items   
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;o	The current Room   
	•	This will be the object used for saving and loading   
2.	Spaces   
	•	The system will be built around two types of objects: Rooms and Items   
	•	Both will be of type Space, with similar functionality   
	•	Rooms will represent locations that can be moved between   
	•	Items will represent items that can be added to inventory   
	•	Both will have descriptions, and may optionally have images   
	•	All Spaces are stored in GameSpace, and can be accessed from any Space   
3.	Flags   
	•	Progress will be controlled by Flags   
	•	When an action has been completed, the associated flag will be flipped, to indicate completion   
	•	Descriptions can be attached to flags, to alter output text based on the state of the flag   
	•	All Flags are stored in GameState, and can be accessed from any Space   
4.	Command Execution   
	•	Execution of player commands will be handled within each Space’s code   
	•	This means that the same command can do different things in different Spaces   
	•	Every command will return a String result to print, and optionally a String ASCII image to display   

# GUI Package:

![image](https://user-images.githubusercontent.com/48114601/120560241-fe763500-c3c7-11eb-9efe-c3fd06a5f19b.png)

# System Package:

![image](https://user-images.githubusercontent.com/48114601/120560283-0fbf4180-c3c8-11eb-9ec0-6cf0caacba15.png)

# Rooms Package:

![image](https://user-images.githubusercontent.com/48114601/120560323-1f3e8a80-c3c8-11eb-84cb-dc7d4986e736.png)

# Items Package:

![image](https://user-images.githubusercontent.com/48114601/120560350-29f91f80-c3c8-11eb-9479-0234e9af7b45.png)
