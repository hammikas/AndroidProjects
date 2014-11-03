package com.sirisha.shooting;

/**
 * Created by Sirisha on 11/1/2014.
 */
        import android.content.Context;
        import com.badlogic.gdx.ApplicationListener;
        import com.badlogic.gdx.Gdx;
        import com.badlogic.gdx.audio.Music;
        import com.badlogic.gdx.graphics.GL20;
        import com.badlogic.gdx.graphics.OrthographicCamera;
        import com.badlogic.gdx.graphics.Texture;
        import com.badlogic.gdx.graphics.g2d.BitmapFont;
        import com.badlogic.gdx.graphics.g2d.Sprite;
        import com.badlogic.gdx.graphics.g2d.SpriteBatch;
        import com.badlogic.gdx.math.Vector3;

public class ShooterGame implements ApplicationListener {
    public static final int SCREEN_HEIGHT = 480;
    public static final int SCREEN_WIDTH = 800;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Texture background;
    private AnimatedSprite spaceshipAnimated;
    private ShotManager shotManager;
    private Music gameMusic;
    private Enemy enemy;
    private GoldCoins goldCoins;
    private CollisionManager collisionManager;
    private boolean isGameOver = false;
    private Context context;

    public ShooterGame(Context ctx) {
        this.context = ctx;
    }
    public int livesCount = 0;

    @Override
    public void create() {

        //Texture.setEnforcePotImages(false);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        batch = new SpriteBatch();

        background = new Texture(Gdx.files.internal("backgroundmm.png"));
        Texture spaceshipTexture = new Texture(Gdx.files.internal("mario200x200.png"));
        Sprite spaceshipSprite = new Sprite(spaceshipTexture);
        spaceshipAnimated = new AnimatedSprite(spaceshipSprite);
        spaceshipAnimated.setPosition(800 / 2, 0);

        Texture shotTexture = new Texture(Gdx.files.internal("firebomb44x40final.png"));
        Texture enemyShotTexture = new Texture(Gdx.files.internal("enemy-shot-spritesheet.png"));
        Texture goldCoinTexture = new Texture(Gdx.files.internal("goldcoins.png"));
        Texture goldCoinShipTexture = new Texture(Gdx.files.internal("blanksprite.png"));

        shotManager = new ShotManager(shotTexture, enemyShotTexture, goldCoinTexture);
        Texture enemyTexture = new Texture(Gdx.files.internal("manie240x240.png"));
        enemy = new Enemy(enemyTexture, shotManager);
        goldCoins = new GoldCoins(goldCoinShipTexture, shotManager);

        collisionManager = new CollisionManager(context, spaceshipAnimated, enemy, goldCoins, shotManager);

        gameMusic = Gdx.audio.newMusic(Gdx.files.internal("game-music.mp3"));
        gameMusic.setVolume(.25f);
        gameMusic.setLooping(true);
        gameMusic.play();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0);

        BitmapFont goldCoinFont = new BitmapFont();
        goldCoinFont.setScale(1);
        goldCoinFont.draw(batch, "Coins : " + collisionManager.goldCoinCount, 720, 470);

        BitmapFont livesFont = new BitmapFont();
        livesFont.setScale(1);
        livesFont.draw(batch, "Lives : " + livesCount, 10, 470);

        if(isGameOver)
        {
            BitmapFont font = new BitmapFont();
            font.setScale(5);
            font.draw(batch, "PLAYER HIT", 200, 200);

            if (livesCount > 3)
                System.exit(0);
        }

        spaceshipAnimated.draw(batch);
        enemy.draw(batch);
        shotManager.draw(batch);
        goldCoins.draw(batch);
        batch.end();

        handleInput();

        if(!isGameOver)
        {
            spaceshipAnimated.move();
            enemy.update();
            goldCoins.update();
            shotManager.update();

            collisionManager.handleCollisions();
        }

        if(spaceshipAnimated.isDead())
        {
            isGameOver = true;
        }
    }

    private void handleInput() {
        if(Gdx.input.isTouched())
        {
            if(isGameOver)
            {
                livesCount++;

                spaceshipAnimated.setDead(false);
                isGameOver = false;
            }

            Vector3 touchPosition = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);

            if(touchPosition.x > spaceshipAnimated.getX())
            {
                spaceshipAnimated.moveRight();
            }
            else
            {
                spaceshipAnimated.moveLeft();
            }

            shotManager.firePlayerShot(spaceshipAnimated.getX());
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }
}
