package gfx;

import java.awt.image.BufferedImage;

public class Assets {

	private static final int width = 16, height = 16;
	
	public static BufferedImage grass, wall, rock;
	public static BufferedImage[] player_down, player_up, player_right, player_left;
	
	public static void init() {
		SpriteSheet sheet = new SpriteSheet(ImageLoader.loadImage("/textures/town_tiles.png"));
		
		player_down = new BufferedImage[3];
		
		player_down[0] = sheet.crop(0, 0, width, height);
		
		grass = sheet.crop(0, 16, 16, 16);
		rock = sheet.crop(0, 0, 16, 16);
	}
	
}
