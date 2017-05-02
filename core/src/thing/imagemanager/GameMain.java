package thing.imagemanager;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import database.ImageDatabase;
import database.ImageInfo;
import gui.Gui;

import java.io.File;

public class GameMain extends ApplicationAdapter {

    private static ImageDatabase database;

    private Stage stage;


    public GameMain(String sourceFolderPath) {
        database = new ImageDatabase();

        File folder = new File(sourceFolderPath);
        for (File file : folder.listFiles()) {
            if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                database.addImage(new ImageInfo(file));
            }
        }
    }

    @Override
    public void create() {
        Gui.background_color = new Color(0.05f, 0.05f, 0.08f, 1f);
        Gui.frame_color = new Color(0.1f, 0.1f, 0.15f, 1f);
        Gui.trim_color = new Color(0.5f, 0.5f, 0.6f, 1f);

        //Initialize renderers
        Gui.setupRenderers();

        //Init stage
        stage = new Stage();
        BasicImageGrid grid = new BasicImageGrid(database.getAll(), 0.5f, 0, 0.5f, 1f);
        Gdx.input.setInputProcessor(grid);
        stage.addActor(grid);
    }

    public static ImageDatabase getDatabase() {
        return database;
    }

    @Override
    public void resize(int width, int height) {
        Gui.resizeRenderers(width, height);
    }

    public void exit() {
        //DO ALL DISPOSAL
        dispose();

        Gdx.app.exit();
        System.exit(0);
    }

    @Override
    public void dispose() {
        if (database != null) {
            database.dispose();
        }

        if (stage != null) {
            stage.dispose();
        }

        Gui.dispose();
    }

    private void update(float delta) {
        globalKeys();

        if (stage != null) {
            stage.act(delta);
        }
    }

    private void globalKeys() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            exit();
        }
    }

    @Override
    public void render() {
        update(Gdx.graphics.getDeltaTime());

        Gdx.gl.glClearColor(Gui.background_color.r, Gui.background_color.g, Gui.background_color.b, Gui.background_color.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw stage
        if (stage != null) {
            stage.draw();
        }

        Gui.endAll();
    }

}
