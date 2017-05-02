package thing.imagemanager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import database.FilenameComparator;
import database.ImageInfo;
import gui.Gui;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Austin on 1/17/2017.
 */
public class BasicImageGrid extends Actor implements InputProcessor {

    private int scrollBarWidth = 20;
    private int border = 10;
    private int thumbnailSize = 200;

    private float targetScroll = 0;
    private float actualScroll = 0;

    private ArrayList<ImageInfo> contents;


    public BasicImageGrid(ArrayList<ImageInfo> images, float x, float y, float width, float height) {
        this.contents = images;
        if (contents != null && !contents.isEmpty()) Collections.sort(contents, new FilenameComparator(true));

        setPosition(x, y);
        setSize(width, height);
    }

    private int getMaxScrollHeight() {
        return (int) (getHeight() * Gdx.graphics.getHeight() - border * 2 - 6 - scrollBarWidth * 2);
    }

    private int getMaxThumbsAcross() {
        return (int) Math.floor((getWidth() * Gdx.graphics.getWidth() - 2*border) / (thumbnailSize + border));
    }

    private int getVerticalScrollArea() {
        return contents.size()/getMaxThumbsAcross() * (thumbnailSize + border);
    }

    private int getThumbnailX(int thumbIndex) {
        return (thumbIndex % getMaxThumbsAcross()) * (thumbnailSize + border);
    }

    private int getThumbnailY(int thumbIndex) {
        return (thumbIndex / getMaxThumbsAcross()) * (thumbnailSize + border) - (int) (actualScroll*getVerticalScrollArea());
    }

    private boolean isIndexVisible(int thumbIndex) {
        return getThumbnailY(thumbIndex) + thumbnailSize >= 0 && getThumbnailY(thumbIndex) - thumbnailSize <= getHeight() * Gdx.graphics.getHeight();
    }

    @Override
    public void act(float delta) {
        if (actualScroll != targetScroll) {
            actualScroll += (targetScroll - actualScroll) / 4;
        }
        if (Math.abs(actualScroll - targetScroll) <= 0.001) {
            actualScroll = targetScroll;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gui.end(batch);
        Gui.endAll();

        //Begin draw frame ---------------------------------------------------------------------------------------------
        Gui.begin(Gui.sr, ShapeRenderer.ShapeType.Filled, Gui.frame_color);

        //Frame
        Gui.sr.rect(border + getX() * Gdx.graphics.getWidth(), border + getY() * Gdx.graphics.getHeight(), -2 * border + getWidth() * Gdx.graphics.getWidth(), -2 * border + getHeight() * Gdx.graphics.getHeight());

        Gui.sr.setColor(Gui.trim_color);
        Gui.sr.set(ShapeRenderer.ShapeType.Line);

        //Scroll bar divider
        Gui.sr.line(-border + getX() * Gdx.graphics.getWidth() + getWidth() * Gdx.graphics.getWidth() - scrollBarWidth, 2 * border + getY() * Gdx.graphics.getHeight(), -border + getX() * Gdx.graphics.getWidth() + getWidth() * Gdx.graphics.getWidth() - scrollBarWidth, -2 * border + getY() * Gdx.graphics.getHeight() + getHeight() * Gdx.graphics.getHeight());

        Gui.sr.set(ShapeRenderer.ShapeType.Filled);

        //Draw actualScroll bar
        Gui.sr.rect(-border + getX() * Gdx.graphics.getWidth() + getWidth() * Gdx.graphics.getWidth() - scrollBarWidth + 3, -border + getY() * Gdx.graphics.getHeight() + getHeight() * Gdx.graphics.getHeight() - 2 * scrollBarWidth - 3 - getMaxScrollHeight() * actualScroll, scrollBarWidth - 6, scrollBarWidth * 2);

        Gui.sr.flush();
        Gui.endAll();
        //End draw frame -----------------------------------------------------------------------------------------------

        //Begin draw contents ------------------------------------------------------------------------------------------
        Gui.begin(Gui.batch);
        Gui.enableScissor((int) (getX() * Gdx.graphics.getWidth() + 2*border), (int) (getY() * Gdx.graphics.getHeight() + 2*border), (int) (getWidth() * Gdx.graphics.getWidth() - 4*border), (int) (getHeight() * Gdx.graphics.getHeight() - 4*border));

        if (contents != null && !contents.isEmpty()) {
            for (int i = 0; i < contents.size(); i++) {
                if (isIndexVisible(i)) {
                    ImageInfo info = contents.get(i);
//                    float scale = thumbnailSize/info.getTexture().getWidth();
//                    if (info.getTexture().getHeight() > info.getTexture().getWidth()) scale = thumbnailSize/info.getTexture().getHeight();
//                    int w = (int) (info.getTexture().getWidth() * scale);
//                    int h = (int) (info.getTexture().getHeight() * scale);
                    int w = thumbnailSize;
                    int h = thumbnailSize;

                    Gui.batch.draw(info.getTexture(), getX() * Gdx.graphics.getWidth() + 2*border + getThumbnailX(i) + thumbnailSize/2 - w/2, (getY() + getHeight()) * Gdx.graphics.getHeight() + 2*border - getThumbnailY(i) + thumbnailSize/2 - h/2, w, h);
                }
            }
        }

        Gui.batch.flush();
        Gui.disableScissor();
        Gui.endAll();
        //End draw contents --------------------------------------------------------------------------------------------

        Gui.begin(batch);
    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (targetScroll < 1 && amount > 0) {
            targetScroll += amount * 0.005f;
        } else if (targetScroll > 0 && amount < 0) {
            targetScroll += amount * 0.005f;
        }

        if (targetScroll > 1) targetScroll = 1;
        if (targetScroll < 0) targetScroll = 0;

        return true;
    }

}
