package states;

import java.awt.Graphics;

import entities.creatures.Player;
import gfx.Assets;
import main.Game;
import main.Handler;
import worlds.World;

public class GameState extends State {
	
	private Player player;
	private World world;
	
	public GameState(Handler handler) {
		super(handler);
		world = new World(handler, "res/worlds/world1.txt");
		handler.setWorld(world);
		player = new Player(handler, 100, 100);
	}
	
	public void update() {
		world.update();
		player.update();
	}
	
	public void render(Graphics g) {
		world.render(g);
		player.render(g);
	}

}
