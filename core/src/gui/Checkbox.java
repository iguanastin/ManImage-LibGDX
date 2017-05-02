package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * @author austinbt
 */
public class Checkbox extends Actor {

    public static final int BOX_WIDTH = 20;
    private boolean selected = false;
    private String text = "Default";

    private CheckboxListener listener;

    public Checkbox(int x, int y, int width, int height, boolean selected, String text) {
        setPosition(x, y);
        setSize(width, height);

        this.text = text;
        this.selected = selected;
    }

    public void setListener(CheckboxListener listener) {
        this.listener = listener;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean b) {
        selected = b;
    }

    @Override
    public void act(float delta) {
        if (isMouseJustDownOnThis()) {
            //Invert selected value
            setSelected(!isSelected());

            //Fire listener event
            if (listener != null) {
                listener.checkboxClicked(this, isSelected());
            }
        }
    }

    /**
     * Determines if the mouse was just clicked and is currently within this object's bounds.
     *
     * @return true if the mouse was just clicked and is within this object's bounds, false otherwise
     */
    private boolean isMouseJustDownOnThis() {
        int x = Gdx.input.getX();
        int y = Gdx.graphics.getHeight() - Gdx.input.getY();
        return Gdx.input.justTouched() && x > getX() && x < getX() + getWidth() && y > getY() && y < getY() + getHeight();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Gui.end(batch);
        Gui.begin(Gui.sr, ShapeRenderer.ShapeType.Filled, Gui.frame_color);

        //Draw frame background
        Gui.sr.rect(getX(), getY(), getWidth(), getHeight());

        //Set shapetype to filled if selected
        if (isSelected()) {
            Gui.begin(Gui.sr, ShapeRenderer.ShapeType.Filled, Gui.trim_color);
        } else {
            Gui.begin(Gui.sr, ShapeRenderer.ShapeType.Line, Gui.trim_color);
        }

        //Draw box
        Gui.sr.rect(getX() + Gui.border, getY() + Gui.border, BOX_WIDTH, BOX_WIDTH);

        Gui.end(Gui.sr);
        Gui.begin(batch);

        //Draw text
        Gui.font.draw(batch, text, getX() + Gui.border*2 + BOX_WIDTH, getY() + Gui.border);
    }
}
