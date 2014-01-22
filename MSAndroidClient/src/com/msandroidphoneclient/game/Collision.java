package com.msandroidphoneclient.game;

public class Collision
{
	Player player;
	GameObject object;
	Vector3d collisionPoint;
	
	public Collision(Player player, GameObject object, Vector3d collisionPoint)
	{
		this.player = player;
		this.object = object;
		this.collisionPoint = collisionPoint;
	}
}
