package main;

import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import display.Display;
import gfx.Assets;
import gfx.GameCamera;
import gfx.ImageLoader;
import gfx.SpriteSheet;
import input.KeyManager;
import states.GameState;
import states.GameStateManager;
import states.State;

public class Game implements Runnable {

	private Display display;
	
	private int width, height;
	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	private State gameState;
	
	private KeyManager keyManager;
	
	private GameCamera gameCamera;
	
	private Handler handler;
	
	public Game(String title, int width, int height) {
		this.width = width;
		this.height = height;
		this.title = title;
		keyManager = new KeyManager();
	}

	private void init() {
		display = new Display(title, width, height);
		display.getFrame().addKeyListener(keyManager);
		Assets.init();
		
		handler = new Handler(this);
		gameCamera = new GameCamera(handler, 0, 0);
		
		gameState = new GameState(handler);
		GameStateManager.setState(gameState);
	}
	
	private void update() {
		keyManager.update();
		
		if(GameStateManager.getState() != null)
			 GameStateManager.getState().update();
	}
	
	private void render(double interpolation) {		
		bs = display.getCanvas().getBufferStrategy(); 
		if(bs == null) {
			display.getCanvas().createBufferStrategy(3); return; 
		} 
		g = bs.getDrawGraphics(); 
		//Clear Screen
		g.clearRect(0, 0, width, height);
		
		//Draw Here
		 if(GameStateManager.getState() != null)
			 GameStateManager.getState().render(g);
		  
		
		//End Drawing
		bs.show(); 
		g.dispose();
	}
	
	@Override
	public void run() {
		init();
		
		// Initial values
		int updatesPerSec = 64;               // How many updates you do per second.
		double timePerUpdate = 1000000000 / updatesPerSec;   // The number of nanoseconds between updates.
		double nextUpdate = System.nanoTime();               // When next update should occur.
		long currentTime = System.nanoTime();              // The current time.
		long lastTime = System.nanoTime();
		long timer = 0;
		int ticks = 0;
		int renders = 0;
		
		// Game loop
		while(running) {

		    // Update current time
		    currentTime = System.nanoTime();
		    timer += currentTime - lastTime;
		    lastTime = currentTime;

		    // While we are behind in updates, do updates! Keep doing them till we catch up.
		    while(nextUpdate < currentTime) {
		        update();
		        nextUpdate += timePerUpdate;
		        ticks++;
		    }

		    // Calculate how far in between updates we are:
		    // Near 0.0 values: update just occurred.
		    // Near 1.0 values: update about to happen.
		    // Read this equation till it makes sense what's happening here:
		    double interpolation = (currentTime + timePerUpdate - nextUpdate) / timePerUpdate;
		    render(interpolation);
		    renders++;
		    
		    if (timer >= 1000000000) {
		    	System.out.printf("UPS: %d\n", ticks);
		    	System.out.printf("FPS: %d\n", renders);
		    	ticks = 0;
		    	renders = 0;
		    	timer = 0;
		    }
		}
		
		stop();
	}
	
	public KeyManager getKeyManager() {
		return keyManager;
	}
	
	public GameCamera getGameCamera() {
		return gameCamera;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public synchronized void start() {
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop() {
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
