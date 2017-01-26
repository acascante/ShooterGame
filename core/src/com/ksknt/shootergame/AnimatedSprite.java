package com.ksknt.shootergame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class AnimatedSprite {

	private static final int SHIP_SPEED = 300;
	private static final int FRAMES_COL = 2;
	private static final int FRAMES_ROW = 2;

	private Sprite sprite;
	private Animation<TextureRegion> animation;
	private TextureRegion[] frames;
	private TextureRegion currentFrame;
	private Vector2 velocity;

	private float stateTime;
	private boolean isDead;

	public AnimatedSprite(Sprite sprite) {
		this.sprite = sprite;
		isDead = false;
		
		velocity = new Vector2();
		Texture texture = sprite.getTexture();
		TextureRegion[][] temp  = TextureRegion.split(texture, (int) getSpriteWidth(), texture.getHeight() / FRAMES_ROW);
		frames = new TextureRegion[FRAMES_COL * FRAMES_ROW];
		int index = 0;
		for (int i = 0; i < FRAMES_ROW; i++) {
			for (int j = 0; j < FRAMES_COL; j++) {
				frames[index++] = temp[i][j];
			}
		}
		animation = new Animation<TextureRegion>(0.1f, frames);
		stateTime = 0f;
	}
	
	public void draw(SpriteBatch spriteBatch) {
		stateTime += Gdx.graphics.getDeltaTime();
		currentFrame = animation.getKeyFrame(stateTime, true);
		spriteBatch.draw(currentFrame, sprite.getX(), sprite.getY());
	}
	
	public void setPosition(float x, float y) {
		sprite.setPosition(x - getSpriteCenterOffset(), y);
	}

	public float getSpriteCenterOffset() {
		return getSpriteWidth() / 2;
	}
	
	private float getSpriteWidth() {
		return sprite.getWidth() / FRAMES_COL;
	}

	public void moveRight() {
		velocity = new Vector2(SHIP_SPEED, 0);
	}
	
	public void moveLeft() {
		velocity = new Vector2(-SHIP_SPEED, 0);
	}
	public int getX() {
		// Positicion of the center of the ship
		return (int) (sprite.getX() + getSpriteCenterOffset());
	}

	public void move(float screenWidth, float screenHeight) {
		int xMovement = (int) (velocity.x * Gdx.graphics.getDeltaTime());
		int yMovement = (int) (velocity.y * Gdx.graphics.getDeltaTime());
		sprite.setPosition(sprite.getX() + xMovement, sprite.getY() + yMovement);
		
		if (sprite.getX() < 0) {
			sprite.setX(0);
		}

		if (sprite.getX() + getSpriteWidth() > screenWidth) {
			sprite.setX(screenWidth - getSpriteWidth());
		}
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getY() {
		return sprite.getY();
	}

	public int getWidth() {
		return (int) getSpriteWidth();
	}

	public int getHeight() {
		return (int) sprite.getHeight() / FRAMES_ROW;
	}

	public void changeDirection() {
		velocity.x = -velocity.x;
	}

	public Rectangle getBoundingBox() {
		return new Rectangle(sprite.getX(), sprite.getY(), getWidth(), getHeight());
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;		
	}

	public boolean isDead() {
		return this.isDead;
	}
}