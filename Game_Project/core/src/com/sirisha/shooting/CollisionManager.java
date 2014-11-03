package com.sirisha.shooting;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by Sirisha on 11/1/2014.
 */
public class CollisionManager {

    private final AnimatedSprite spaceshipAnimated;
    private final Enemy enemy;
    private final ShotManager shotManager;
    private final Context context;
    private final GoldCoins goldCoins;
    public int goldCoinCount = 0;

    public CollisionManager(Context ctx, AnimatedSprite spaceshipAnimated, Enemy enemy,
                            GoldCoins goldCoins, ShotManager shotManager) {
        this.spaceshipAnimated = spaceshipAnimated;
        this.enemy = enemy;
        this.shotManager = shotManager;
        this.context = ctx;
        this.goldCoins = goldCoins;
    }

    public void handleCollisions() {
        handleEnemyShot();
        handlePlayerShot();
        handleGoldCoinShot();
    }

    private void handlePlayerShot() {
        if(shotManager.enemyShotTouches(spaceshipAnimated.getBoundingBox()))
        {
            spaceshipAnimated.setDead(true);
            vibrateDevice();
        }
    }

    private void vibrateDevice() {
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 1000 milliseconds
        v.vibrate(1000);
    }

    private void handleEnemyShot() {
        if(shotManager.playerShotTouches(enemy.getBoundingBox()))
            enemy.hit();
    }

    private void handleGoldCoinShot() {
        if (shotManager.goldCoinTouches(spaceshipAnimated.getBoundingBox()))
            goldCoinCount++;
    }
}