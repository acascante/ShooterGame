package com.ksknt.shootergame;

public class CollisionManager {

	private AnimatedSprite spaceShipAnimated;
	private Enemy enemy;
	private ShotManager shotManager;

	
	public CollisionManager() {
		super();
	}

	public CollisionManager(AnimatedSprite spaceShipAnimated, Enemy enemy, ShotManager shotManager) {
		this.spaceShipAnimated = spaceShipAnimated;
		this.enemy = enemy;
		this.shotManager = shotManager;
	}

	public void handleCollisions() {
		handleEnemyShot();
		handlePlayerShot();
	}

	private void handlePlayerShot() {
		if(shotManager.enemyShotTouches(spaceShipAnimated.getBoundingBox())) {
			spaceShipAnimated.setDead(true);
		}
	}

	private void handleEnemyShot() {
		if(shotManager.playerShotTouches(enemy.getBoundingBox())) {
			enemy.hit();
		}
	}
}
