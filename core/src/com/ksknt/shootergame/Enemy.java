package com.ksknt.shootergame;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Enemy {

	private static final int ENEMY_CHANGE_DIRECTION = 21;
	private static final int ENEMY_SHOT = 50;
	private static final float ENEMY_SPEED = 250;
	private Texture enemyTexture;
	private AnimatedSprite animatedSprite;
	private ShotManager shotManager;
	private float spawnTimeout;
	
	public Enemy() {
		super();
		spawnTimeout = 0f;
	}

	public Enemy(Texture enemyTexture, float screenWidth, float screenHeight, ShotManager shotManager) {
		this.enemyTexture = enemyTexture;
		this.shotManager = shotManager; 
		spawn(screenWidth, screenHeight);
		spawnTimeout = 0f;
	}

	private void spawn(float screenWidth, float screenHeight) {
		Sprite enemySprite = new Sprite(enemyTexture);
		animatedSprite = new AnimatedSprite(enemySprite);
		
		int xPosition = createRandomPosition((int)screenWidth, screenHeight);
		animatedSprite.setPosition(xPosition, screenHeight - animatedSprite.getHeight());
		
		animatedSprite.setVelocity(new Vector2(ENEMY_SPEED, 0));
		
		animatedSprite.setDead(false);
	}

	private int createRandomPosition(int screenWidth, float screenHeight) {
		Random random = new Random();
		int randomNumber = random.nextInt(screenWidth - animatedSprite.getWidth() + 1);
		return randomNumber + animatedSprite.getWidth() / 2;
	}

	public void draw(SpriteBatch batch) {
		if(!animatedSprite.isDead()) {
			animatedSprite.draw(batch);
		}
	}

	public void update(float screenWidth, float screenHeight) {
		if(animatedSprite.isDead()){
			spawnTimeout -= Gdx.graphics.getDeltaTime();
			if(spawnTimeout <= 0) {
				spawn(screenWidth, screenHeight);
			}
		} else {
			if(shouldChangeDirection()) {
				animatedSprite.changeDirection();
			}
			
			if(shouldFire()) {
				shotManager.fireEnemyShot(animatedSprite.getX());
			}
			animatedSprite.move(screenWidth, screenHeight);
		}
	}

	private boolean shouldFire() {
		Random random = new Random();
		return random.nextInt(ENEMY_SHOT) == 0;
	}

	private boolean shouldChangeDirection() {
		Random random = new Random();
		return random.nextInt(ENEMY_CHANGE_DIRECTION) == 0;
	}

	public Rectangle getBoundingBox() {
		return animatedSprite.getBoundingBox();
	}

	public void hit() {
		animatedSprite.setDead(true);
		spawnTimeout = 2f;
	}
}