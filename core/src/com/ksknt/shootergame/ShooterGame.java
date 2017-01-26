package com.ksknt.shootergame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;

public class ShooterGame extends ApplicationAdapter {
	
	private static final String GAME_MUSIC = "back.wav";
	private static final String ANIMATED_SHIP = "anim.png";
	private static final String ANIMATED_ENEMY_SHIP = "enemy_animated.png";
	private static final String BACKGROUND = "bg3.png";
	private static final String SHOT = "shot2.png";
	private static final String ENEMY_SHOT = "shot_enemy1.png";
	
	private OrthographicCamera	camera;
	private SpriteBatch 		batch;
	private Texture 			background;
	private AnimatedSprite		spaceShipAnimated;
	private float				screenWidth;
	private float				screenHeight;
	private ShotManager			shotManager;
	private CollisionManager	collisionManager;
	private Enemy 				enemy;
	private Music 				gameMusic;
	private boolean 			isGameOver;
	
	public ShooterGame() {
		super();
		this.screenWidth = 800;
		this.screenHeight = 480;		
		this.isGameOver = false;
	}
	
	public ShooterGame(float screenWidth, float screenHeight) {
		super();
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.isGameOver = false;
	}

	@Override
	public void create () {		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, screenWidth, screenHeight);
		
		batch = new SpriteBatch();
		
		background = new Texture(Gdx.files.internal(BACKGROUND));
		
		Texture spaceShipTexture = new Texture(Gdx.files.internal(ANIMATED_SHIP));
		Sprite spaceShipSprite = new Sprite(spaceShipTexture);
		spaceShipAnimated = new AnimatedSprite(spaceShipSprite);
		spaceShipAnimated.setPosition(screenWidth / 2 , 0);
		
		Texture shotTexture = new Texture(Gdx.files.internal(SHOT));
		Texture enemyShotTexture = new Texture(Gdx.files.internal(ENEMY_SHOT));
		shotManager = new ShotManager(shotTexture, enemyShotTexture);
		
		Texture enemyTexture = new Texture(Gdx.files.internal(ANIMATED_ENEMY_SHIP));
		enemy = new Enemy(enemyTexture, screenWidth, screenHeight, shotManager);
		
		collisionManager = new CollisionManager(spaceShipAnimated, enemy, shotManager);
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal(GAME_MUSIC));
		gameMusic.setVolume(.25f);
		gameMusic.setLooping(true);
		gameMusic.play();
	}

	private void handleInput() {
		if (Gdx.input.isTouched()) {
			if(isGameOver) {
				spaceShipAnimated.setDead(false);
				isGameOver = false;
			}
			Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
			camera.unproject(touchPosition);
			
			int xTouch = Gdx.input.getX();
			if (xTouch > spaceShipAnimated.getX()) {
				spaceShipAnimated.moveRight();
			} else {
				spaceShipAnimated.moveLeft();
			}
			
			shotManager.firePlayerShot(spaceShipAnimated.getX());
		}
	}
	
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(background, 0, 0);
		
		if(isGameOver) {
			BitmapFont font = new BitmapFont();
			font.draw(batch, "PLAYER HIT", 200, 200);
		}
		spaceShipAnimated.draw(batch);
		shotManager.draw(batch);
		enemy.draw(batch);
		batch.end();
		
		handleInput();
		if(!isGameOver) {
			spaceShipAnimated.move(screenWidth, screenHeight);
			enemy.update(screenWidth, screenHeight);
			shotManager.update(screenWidth, screenHeight);
			collisionManager.handleCollisions();
		}
		if(spaceShipAnimated.isDead()) {
			isGameOver = true;
		}
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}