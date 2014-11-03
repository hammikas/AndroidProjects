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

    public CollisionManager(Context ctx, AnimatedSprite spaceshipAnimated, Enemy enemy,
                            ShotManager shotManager) {
        this.spaceshipAnimated = spaceshipAnimated;
        this.enemy = enemy;
        this.shotManager = shotManager;
        this.context = ctx;
    }

    public void handleCollisions() {
        handleEnemyShot();
        handlePlayerShot();
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
        // Vibrate for 500 milliseconds
        v.vibrate(500);
    }

    private void handleEnemyShot() {
        if(shotManager.playerShotTouches(enemy.getBoundingBox()))
            enemy.hit();
    }

}

