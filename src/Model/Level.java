/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Model.Items.Pickups.*;
import Model.Actors.Door;
import Model.Actors.Player;
import Model.Actors.Tile;
import Model.Actors.Enemies.*;
import Model.Items.*;
import Resources.ResourceLoader;
import java.awt.Point;
import java.io.InputStream;
import java.io.StringReader;
import java.util.Scanner;

/**
 *
 * @author Luminight
 */
public class Level {

	public enum Difficulty {
		Easy, Normal, Hard;

		public static Difficulty FromString(String string) {
			switch (string.toLowerCase()) {
				case "easy":
					return Difficulty.Easy;
				case "normal":
					return Difficulty.Normal;
				case "hard":
					return Difficulty.Hard;
				default:
					return Difficulty.Easy;
			}
		}
	}

	public String name;
	public Difficulty difficulty;
	public String ResourcePack = "Default";
	private String fileName;
	private static final String COMMENT = ";";
	public static final String MAPFOLDER = "/Resources/Maps/";

	Point roomSize;

	Point size;
	char[][] map;
	char[] equipment;

	int hp = -1;
	int maxHP = -1;
	int bombs = 0;
	int maxBombs = 0;
	int arrows = 0;
	int maxArrows = 0;
	int keys = 0;
	int time = 500;

	public Level(String fileName) {
		roomSize = new Point(Game.RoomSizeX, Game.RoomSizeY);
		this.fileName = fileName;
		String[] fileParts = fileName.split("\\.", 3);
		name = fileParts[0];
		difficulty = Difficulty.FromString(fileParts[1]);

	}

	public void LoadLevel() {
		InputStream stream = ResourceLoader.LoadResource(MAPFOLDER + fileName);
		try (Scanner sc = new Scanner(stream,"utf-8")) {
			String line = ReadLine(sc);
			boolean sizeRead = false;
			boolean mapRead = false;
			boolean EquipRead = false;
			while (!line.isEmpty()) {
				String[] sep = line.split("=", 2);
				switch (sep[0].toLowerCase()) {
					case "pack":
						ResourcePack = sep[1];
						break;
					case "equip":
						if (EquipRead) {
							System.err.println("WARNING: Equipment read again.");
						} else {
							ReadEquipment(sep[1]);
							EquipRead = true;
						}
						break;
					case "size":
						if (sizeRead) {
							System.err.println("WARNING: Size read again");
						} else {
							ReadSize(sep[1]);
							sizeRead = true;
						}
						break;
					case "map":
						if (!sizeRead) {
							System.err.println("ERROR! Size has to be read before map!");
							throw new Exception();
						}
						ReadMap(sc);
						mapRead = true;
						break;
					case "arrow":
						arrows = ReadStat(sep[1]);
						break;
					case "bomb":
						bombs = ReadStat(sep[1]);
						break;
					case "key":
						keys = ReadStat(sep[1]);
						break;
					case "time":
						time = ReadStat(sep[1]);
						break;
					case "hp":
						hp = ReadStat(sep[1]);
						if (maxHP == -1) {
							maxHP = hp;
						}
						break;
					case "maxhp":
						maxHP = ReadStat(sep[1]);
						if (hp == -1) {
							hp = maxHP;
						}
						break;
					case "maxarrow":
						maxArrows = ReadStat(sep[1]);
						break;
					case "maxbomb":
						maxBombs = ReadStat(sep[1]);
						break;
				}

				line = ReadLine(sc);
			}
			if (!mapRead) {
				System.err.println("Map wasn't read!");
				throw new Exception();
			}
			if (hp == -1) {
				hp = 6;
				maxHP = 6;
			}
		} catch (Exception e) {
			System.err.println("Level could not be loaded! (" + fileName + ")");
		}

	}

	int ReadStat(String stat) {
		return Integer.parseInt(stat);
	}

	void ReadEquipment(String equip) {
		equipment = new char[equip.length()];
		for (int i = 0; i < equip.length(); i++) {
			equipment[i] = equip.charAt(i);
		}
	}

	void ReadSize(String size) {
		Scanner sc = new Scanner(size);
		int x = sc.nextInt();
		int y = sc.nextInt();
		this.size = new Point(x, y);
		map = new char[this.roomSize.x * this.size.x][this.roomSize.x * this.size.y];
	}

	void ReadMap(Scanner sc) {
		for (int y = 0; y < size.y * roomSize.y; y++) {
			String line = sc.nextLine();
			for (int x = 0; x < size.x * roomSize.x; x++) {
				map[x][y] = (line.length() > x) ? line.charAt(x) : ' ';
			}
		}
	}

	String ReadLine(Scanner sc) {
		String line = "";
		while (sc.hasNextLine() && (line.trim().isEmpty() || line.startsWith(COMMENT))) {
			line = sc.nextLine();
		}
		return line;
	}

	public void BuildLevel() {
		Game g = Game.instance;
		g.actors.clear();
		Player p = new Player();
		g.player = p;
		p.maxHealth = maxHP;
		p.health = hp;
		p.maxBombCount = maxBombs;
		p.bombCount = bombs;
		p.maxArrowCount = maxArrows;
		p.arrowCount = arrows;
		p.keyCount = keys;
		g.Time = time;

		Tile.Tiles = new Tile[roomSize.x * size.x][roomSize.y * size.y];
		//Equipment
		for (int i = 0; i < equipment.length; i++) {
			AddEquipment(equipment[i], p);
		}
		//Map
		for (int x = 0; x < size.x * roomSize.x; x++) {
			for (int y = 0; y < size.y * roomSize.y; y++) {
				BuildTile(x, y, map[x][y], g);
			}
		}

	}

	void AddEquipment(char equip, Player p) {
		switch (equip) {
			case '⛣':
				p.items.add(new Bomb());
				break;
			case '⇈':
				p.items.add(new Bow());
				break;
			case '⛴':
				p.items.add(new Boat());
				break;
			case '⛏':
				p.items.add(new Hammer());
				break;
			default:
				System.err.println("Equipment '" + equip + "' not found!");
				break;
		}
	}

	void BuildTile(int x, int y, char tile, Game g) {
		boolean putTile = true;
		Point worldPoint = new Point(x * Game.TileSize + Game.TileSize / 2, y * Game.TileSize + Game.TileSize / 2);
		switch (tile) {
			case '⛇':
				g.player.SetPosition(new Point(x * Game.TileSize + Game.TileSize / 2, y * Game.TileSize + Game.TileSize / 2));
				break;
			case '⛿':
				new PickupFinish(worldPoint.x, worldPoint.y);
				break;
			case '⛫':
				AddDoor(x, y, null);
				putTile = false;
				break;
			case '⛯':
				AddDoor(x, y, Door.DoorType.LOCKED);
				putTile = false;
				break;
			case '⛬':
				AddDoor(x, y, Door.DoorType.CRACKED);
				putTile = false;
				break;
			case '⛼':
				AddDoor(x, y, Door.DoorType.STUCK);
				putTile = false;
				break;
			case '█':
				AddWall(x, y);
				putTile = false;
				break;
			case '⛆':
				new Tile(x, y, Tile.TileType.WATER, true);
				putTile = false;
				break;
			case '▲':
				new Tile(x, y, Tile.TileType.PEG, true);
				break;
			case '⚶':
				new Fire(x, y);
				break;
			case '⛣':
				new PickupBomb(worldPoint.x, worldPoint.y, 0);
				break;
			case '⛏':
				new PickupHammer(worldPoint.x, worldPoint.y);
				break;
			case '⇈':
				new PickupBow(worldPoint.x, worldPoint.y);
				break;
			case '⛴':
				new PickupBoat(worldPoint.x, worldPoint.y);
				break;
			case '⚿':
				new PickupKey(worldPoint.x, worldPoint.y);
				break;
			case '⛋':
				new PickupTreasure(worldPoint.x, worldPoint.y, 0);
				break;
			case '♙':
				new GreenSlime(x, y);
				break;
			case '♘':
				new BlueSlime(x, y);
				break;
			case '♗':
				new PinkSlime(x, y);
				break;
			default:
				break;
		}
		if (putTile) {
			new Tile(x, y, Tile.TileType.NONE, false);
		}
	}

	void AddDoor(int x, int y, Door.DoorType doorType) {
		int rX = x % roomSize.x;
		int rY = y % roomSize.y;

		Tile.TileType tileType = Tile.TileType.FromPoint(rX, rY, roomSize.x - 1, roomSize.y - 1);
		tileType = Tile.TileType.WallToDoor(tileType);
		new Tile(x, y, tileType, false);
		if (doorType != null) {
			new Door(x, y, tileType, doorType);
		}
	}

	void AddWall(int x, int y) {
		int roomX = x % roomSize.x;
		int roomY = y % roomSize.y;
		Tile.TileType wall = Tile.TileType.FromPoint(roomX, roomY, roomSize.x - 1, roomSize.y - 1);
		new Tile(x, y, wall, true);
	}

}
