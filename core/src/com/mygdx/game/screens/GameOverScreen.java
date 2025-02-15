package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.AstroBlaster;

public class GameOverScreen extends Screens{

    // Textures for game over screen
    private Texture background;
    private Texture gameOver;
    private Texture replayButton;
    private Texture homeButton;
    private Texture quitButton;
    private String finalScore;
    private BitmapFont scoreFont;


    public GameOverScreen(ScreenManager sm, int score){

        super(sm);

        camera.setToOrtho(false, AstroBlaster.WIDTH, AstroBlaster.HEIGHT);

        // Initialize textures
        background = new Texture("background.png");
        gameOver = new Texture("gameover.png");
        replayButton = new Texture("play.png");
        homeButton = new Texture("home.png");
        quitButton = new Texture("Quit Button.png");
        finalScore = "FINAL SCORE: " + score;
        scoreFont = new BitmapFont(Gdx.files.internal("myFont.fnt"), Gdx.files.internal("myFont.png"), false);
        scoreFont.getData().setScale(0.7f,0.7f);

        // Get and update scores
        loadScores();
        addScore(score);
        saveScores();

    }

    // Input handler
    @Override
    protected void handleInput() {

        if(Gdx.input.justTouched()){

            input.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(input);

            Rectangle playButtonBounds = new Rectangle(AstroBlaster.WIDTH / 2 - replayButton.getWidth() / 2, 230, replayButton.getWidth(), replayButton.getHeight());
            Rectangle homeButtonBounds = new Rectangle(AstroBlaster.WIDTH / 2 - homeButton.getWidth() / 2, 140, homeButton.getWidth(), homeButton.getHeight());
            Rectangle quitButtonBounds = new Rectangle(AstroBlaster.WIDTH / 2 - quitButton.getWidth() / 2, 50, quitButton.getWidth(), quitButton.getHeight());

            // This will start a new game
            if (playButtonBounds.contains(input.x, input.y)){

                buttonSound.play(0.5f);
                sm.setScreen(new GameScreen(sm));

            }

            // This will go back to the menu
            if(homeButtonBounds.contains(input.x, input.y)){

                buttonSound.play(0.5f);
                sm.setScreen(new MenuScreen(sm));

            }

            // This will make the app exit
            if(quitButtonBounds.contains(input.x, input.y)){

                buttonSound.play(0.5f);
                disposeSound();
                dispose();
                Gdx.app.exit();
            }
        }
    }

    @Override
    public void update(float delta) {

        camera.update();
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(camera.combined);

        // Draw textures
        sb.begin();
        sb.draw(background, 0, 0, AstroBlaster.WIDTH, AstroBlaster.HEIGHT);
        sb.draw(gameOver, (AstroBlaster.WIDTH/2) - (gameOver.getWidth() / 2), 380);
        sb.draw(replayButton, (AstroBlaster.WIDTH/2) - (replayButton.getWidth()/2), 220);
        sb.draw(homeButton, (AstroBlaster.WIDTH/2) - (homeButton.getWidth()/2), 130);
        sb.draw(quitButton, (AstroBlaster.WIDTH/2) - (quitButton.getWidth()/2), 40);

        // How to get middle coordinates of string with custom font
        GlyphLayout gl = new GlyphLayout();
        gl.setText(scoreFont, finalScore);
        float temp = gl.width;
        scoreFont.draw(sb, finalScore, (AstroBlaster.WIDTH / 2) - (temp / 2), 335);

        sb.end();

    }

    // Update score
    public void addScore(int newScore){

        // Loop to check if new score is higher than past high scores
        for(int i = 0; i < highScores.size(); i++){

            int temp = highScores.get(i);

            if(newScore > temp){

                highScores.add(i, newScore);

                // Remove the extra score in list, we only want 5
                if(highScores.size() > 5){

                    highScores.remove(5);
                }

                // Break when a high score is replaced
                break;

            }
        }
    }

    public void loadScores(){

        // Get old high scores, 0 is default when there's none
        highScores.add(myPrefs.getInteger("score_1", 0));
        highScores.add(myPrefs.getInteger("score_2", 0));
        highScores.add(myPrefs.getInteger("score_3", 0));
        highScores.add(myPrefs.getInteger("score_4", 0));
        highScores.add(myPrefs.getInteger("score_5", 0));

    }

    public void saveScores(){

        // Update preferences with new scores from ArrayList
        myPrefs.putInteger("score_1", highScores.get(0));
        myPrefs.putInteger("score_2", highScores.get(1));
        myPrefs.putInteger("score_3", highScores.get(2));
        myPrefs.putInteger("score_4", highScores.get(3));
        myPrefs.putInteger("score_5", highScores.get(4));
        myPrefs.flush();

    }

    @Override
    public void dispose() {

        // Dispose all textures
        background.dispose();
        gameOver.dispose();
        scoreFont.dispose();
        replayButton.dispose();
        homeButton.dispose();
        quitButton.dispose();

    }
}
