package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Slider extends Actor {

    private static final int SLIDER_BAR_WIDTH = 10;

    private int min = 0, max = 100, current = 50;
    private int step = 1;
    private String text;

    private boolean dragging = false;

    private SliderListener listener;

    public Slider(int x, int y, int width, int height) {
        setSize(width, height);
        setPosition(x, y);
    }

    /**
     * Sets values for this slider
     *
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @param current Current value
     * @param step Step radius
     * @return This slider
     */
    public Slider setValues(int min, int max, int current, int step, String text) {
        if (max == min) {
            max = min+1;
        } else if (max < min) {
            int temp = min;
            min = max;
            max = temp;
        }

        this.min = min;
        this.max = max;
        this.current = current;
        this.step = step;
        this.text = text;

        return this;
    }

    public void setCurrent(int val) {
        this.current = val;
    }

    /**
     *
     * @return The current value this slider is at
     */
    public int getValue() {
        return current;
    }

    /**
     *
     * @return Minimum possible value of this slider
     */
    public int getMin() {
        return min;
    }

    /**
     *
     * @return Maximum possible value of this slider
     */
    public int getMax() {
        return max;
    }

    /**
     *
     * @return Step radius of this slider
     */
    public int getStep() {
        return step;
    }

    /**
     * Sets this drag/drop listener for this slider
     *
     * @param listener Listener to be set
     * @return This slider
     */
    public Slider setSliderListener(SliderListener listener) {
        this.listener = listener;

        return this;
    }

    @Override
    public void act(float delta) {
        if (isMouseJustDownOnThis()) {
            //Start dragging
            dragging = true;
        } else if (dragging) {
            if (!Gdx.input.isTouched()) {
                //Stop dragging
                dragging = false;
                //Fire dropped listener
                if (listener != null) {
                    listener.sliderDropped(this, current);
                }
            } else {
                int newValue;

                //Find newValue
                float relativeX = Gdx.input.getX() - getX();
                float pixelWidth = getWidth() - SLIDER_BAR_WIDTH;
                float scalar = relativeX/pixelWidth;
                newValue = min + (int)((max-min)*scalar);

                //Ensure newValue is acceptable
                if (newValue % step != 0) {
                    float work = (float)newValue/step;
                    newValue = (int)(work+0.5f)*step;
                }
                if (newValue < min) {
                    newValue = min;
                } else if (newValue > max) {
                    newValue = max;
                }

                //If value has changed, fire dragged listener
                if (newValue != current && listener != null) {
                    listener.sliderDragged(this, newValue);
                }

                //Set new value
                current = newValue;
            }
        }
    }

    /**
     * Determines if the mouse was just clicked and is currently over within this object's bounds.
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
        //End batch, start shape rendering
        Gui.end(batch);
        Gui.begin(Gui.sr, ShapeRenderer.ShapeType.Filled, Gui.frame_color);

        //Render frame background
        Gui.sr.rect(getX(), getY(), getWidth(), getHeight());

        //Render slider bar
        int offset = (int)((((float)current-min)/((float)max-min))*(getWidth() - SLIDER_BAR_WIDTH));
        if (dragging) {
            Gui.sr.setColor(Gui.alternate_color);
        } else {
            Gui.sr.setColor(Gui.trim_color);
        }
        Gui.sr.rect(getX() + offset, getY(), SLIDER_BAR_WIDTH, getHeight());

        //Render frame border
        if (Gui.has_border) {
            Gui.begin(Gui.sr, ShapeRenderer.ShapeType.Line, Gui.trim_color);
            Gui.sr.rect(getX(), getY(), getWidth(), getHeight());
        }

        //End shape rendering
        Gui.end(Gui.sr);

        //Begin batch
        Gui.begin(batch);

        //Draw text
        Gui.font.draw(batch, text, getX() - Gui.getStringPixelWidth(Gui.font, text) - 5, getY() + getHeight()/2 + Gui.getStringPixelHeight(Gui.font, text)/2);
        String valueText = "" + getValue();
        Gui.font.draw(batch, valueText, getX() + getWidth() + 5, getY() + getHeight()/2 + Gui.getStringPixelHeight(Gui.font, valueText)/2);
    }
}
