package com.ksknt.shootergame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ShotManager {

	private static final FileHandle SHOT_SOUND = Gdx.files.internal("shot.wav");
	private static final int SHOT_SPEED = 300;
	private static final int SHOT_Y_OFFSET = 90;
	private static final int ENEMY_SHOT_Y_OFFSET = 400;
	private static final float MIN_TIME_BETWEEN_SHOTS = .5f;
	
	private Texture shotTexture;
	private Texture enemyShotTexture;
	private List<AnimatedSprite> shots;
	private List<AnimatedSprite> enemyShots;
	private Sound laser = Gdx.audio.newSound(SHOT_SOUND);

	private float timeSinceLastShot;
	
	public ShotManager() {
		super();
		shots = new ArrayList<AnimatedSprite>();
		enemyShots = new ArrayList<AnimatedSprite>();
		timeSinceLastShot = 0;
	}

	public ShotManager(Texture shotTexture, Texture enemyShotTexture) {
		this.shotTexture = shotTexture;
		this.enemyShotTexture = enemyShotTexture;
		shots = new ArrayList<AnimatedSprite>();
		enemyShots = new ArrayList<AnimatedSprite>();
		timeSinceLastShot = 0;
	}

	public void firePlayerShot(int shipCenterXLocation) {
		if(canFireShot()) {
			Sprite newShot = new Sprite(shotTexture);
			AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
			newShotAnimated.setPosition(shipCenterXLocation, SHOT_Y_OFFSET);
			newShotAnimated.setVelocity(new Vector2(0, SHOT_SPEED));
			shots.add(newShotAnimated);
			timeSinceLastShot = 0f;
			laser.play();
		}
	}

	private boolean canFireShot() {
		return timeSinceLastShot > MIN_TIME_BETWEEN_SHOTS;
	}

	public void update(float screenWidth, float screenHeight) {
		Iterator<AnimatedSprite> i = shots.iterator();
		while(i.hasNext()) {
			AnimatedSprite shot = i.next();
			shot.move(screenWidth, screenHeight);
			if(shot.getY() > screenHeight) {
				i.remove();
			}
		}
		
		Iterator<AnimatedSprite> j = enemyShots.iterator();
		while(j.hasNext()) {
			AnimatedSprite shot = j.next();
			shot.move(screenWidth, screenHeight);
			if(shot.getY() < 0) {
				j.remove();
			}
		}
		timeSinceLastShot += Gdx.graphics.getDeltaTime();
	}

	public void draw(SpriteBatch batch) {
		for(AnimatedSprite shot : shots) {
			shot.draw(batch);
		}
		
		for(AnimatedSprite shot : enemyShots) {
			shot.draw(batch);
		}
	}

	public void fireEnemyShot(int enemyCenterXLocation) {
		Sprite newShot = new Sprite(enemyShotTexture);
		AnimatedSprite newShotAnimated = new AnimatedSprite(newShot);
		newShotAnimated.setPosition(enemyCenterXLocation, ENEMY_SHOT_Y_OFFSET);
		newShotAnimated.setVelocity(new Vector2(0, -SHOT_SPEED));
		enemyShots.add(newShotAnimated);
	}

	public boolean playerShotTouches(Rectangle boundingBox) {
		return shotTouches(shots, boundingBox);
	}

	public boolean enemyShotTouches(Rectangle boundingBox) {
		return shotTouches(enemyShots, boundingBox);
	}

	private boolean shotTouches(List<AnimatedSprite> shots, Rectangle boundingBox) {
		Iterator<AnimatedSprite> i = shots.iterator();
		while(i.hasNext()) {
			AnimatedSprite shot = i.next();
			if(Intersector.intersectRectangles(shot.getBoundingBox(), boundingBox, new Rectangle())) {
				i.remove();
				return true;
			}
		}
		return false;
	}
}
