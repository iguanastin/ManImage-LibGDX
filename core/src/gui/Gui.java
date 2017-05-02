package gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;

/**
 * Collection of GUI descriptors and utility functions.
 * 
 * @author austinbt
 */
public abstract class Gui {

    /**
     * Global border width for this GUI
     */
    public static int border = 5;
    /**
     * Does this GUI draw a border
     */
    public static boolean has_border = false;
    /**
     * Background color drawn first thing on the frame
     */
    public static Color background_color = Color.LIGHT_GRAY;
    /**
     * Frame color for GUI frames
     */
    public static Color frame_color = Color.WHITE;
    /**
     * Border and trim color for GUI frames
     */
    public static Color trim_color = Color.BLACK;
    /**
     * Text color for this GUI
     */
    public static Color text_color = Color.BLACK;
    /**
     * Alternate color for this GUI. No specific use.
     */
    public static Color alternate_color = Color.GRAY;
    
    /**
     * Current batch being used for rendering
     */
    public static SpriteBatch batch;
    /**
     * Current shape renderer being used for rendering
     */
    public static ShapeRenderer sr;
    /**
     * Current font being used for rendering
     */
    public static BitmapFont font;
    
    public static GlyphLayout GLYPHS;
    
    /**
     * Disposes and recreates renderers. Should be called when window is resized to apply effects of viewport changes to rendering.
     */
    public static void setupRenderers() {
        if (batch != null) {
            batch.dispose();
        }
        if (sr != null) {
            sr.dispose();
        }
        if (font != null) {
            font.dispose();
        }
        
        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        sr.setAutoShapeType(true);
        font = new BitmapFont();
        
        font.setColor(text_color);

        GLYPHS = new GlyphLayout();
    }

    public static void resizeRenderers(int width, int height) {
        Matrix4 matrix = new Matrix4();
        matrix.setToOrtho2D(0, 0, width, height);
        batch.setProjectionMatrix(matrix);
        sr.setProjectionMatrix(matrix);
    }
    
    /**
     * Finds the pixel width of a string as it would be drawn onto the screen
     * 
     * @param str String to check width of
     * @return Pixel width of the string
     */
    public static float getStringPixelWidth(BitmapFont font, String str) {
        GLYPHS.setText(font, str);
        return GLYPHS.width;
    }
    
    /**
     * Finds the pixel height of a string as it would be drawn onto the screen
     * 
     * @param str String to check height of
     * @return Pixel height of the string
     */
    public static float getStringPixelHeight(BitmapFont font, String str) {
        GLYPHS.setText(font, str);
        return GLYPHS.height;
    }
    
    /**
     * Draws a string centered at a given point
     * 
     * @param batch Batch to draw onto
     * @param text String to draw onto batch
     * @param x Center x coordinate
     * @param y Center y coordinate
     */
    public static void drawTextCenteredAt(Batch batch, BitmapFont font, String text, float x, float y) {
        float width = getStringPixelWidth(font, text);
        float height = getStringPixelHeight(font, text);
        
        font.draw(batch, text, x - width/2, y + height/2);
    }
    
    /**
     * Draws a string that does not exceed the given pixel width
     * 
     * @param batch Batch to be drawn onto
     * @param text String to draw onto batch
     * @param x X coordinate to draw text at
     * @param y Y coordinate to draw text at
     * @param width Maximum width to allow drawing in
     * @return Remaining text that was not able to be drawn
     */
    public static String drawTextInWidth(Batch batch, BitmapFont font, String text, float x, float y, float width) {
        String draw = text;
        String remaining = "";
        
        if (draw.contains("\n")) {
            remaining = draw.substring(draw.indexOf("\n") + 1);
            draw = draw.substring(0, draw.indexOf("\n"));
        }
        
        while (getStringPixelWidth(font, draw) > width) {
            if (draw.contains(" ")) {
                remaining = draw.substring(draw.lastIndexOf(" ")+1) + " " + remaining;
                draw = draw.substring(0, draw.lastIndexOf(" "));
            } else {
                remaining = draw.charAt(draw.length()-1) + remaining;
                draw = draw.substring(0, draw.length()-1);
            }
        }
        
        if (!draw.isEmpty()) {
            font.draw(batch, draw, x, y);
        }
        
        return remaining;
    }
    
    /**
     * Draws (potentially multi-line) text that will not exceed the given region
     * 
     * @param batch Batch to draw onto
     * @param text String to draw to batch
     * @param x Leftmost x value
     * @param y Highest y value
     * @param width Width of the region
     * @param height Height of the region
     * @return Remaining text that had no room to be drawn on the screen
     */
    public static String drawTextInArea(Batch batch, BitmapFont font, String text, float x, float y, float width, float height) {
        String remaining = text;
        
        for (int i = 0; i < Math.floor(height/font.getLineHeight()) && !remaining.isEmpty(); i++) {
            remaining = drawTextInWidth(batch, font, remaining, x, y - font.getLineHeight()*i, width);
        }
        
        return remaining;
    }
    
    /**
     * Enables a graphics scissor so that anything drawn will only modify pixels within the given dimensions.
     * 
     * @see #disableScissor()
     * 
     * @param x Leftmost x value of scissor region
     * @param y Lowest y value of scissor region
     * @param width Width of scissor region
     * @param height Height of scissor region
     */
    public static void enableScissor(int x, int y, int width, int height) {
        Gdx.gl.glScissor(x, y, width, height);
        Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
    }
    
    /**
     * Disables the scissor modifier
     * 
     * @see #enableScissor(int, int, int, int)
     */
    public static void disableScissor() {
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);
    }
    
    /**
     * Calculates the multiplier needed to scale an image to ensure that it is within the dimensions. Image will never be scaled up. If image has dimensions smaller than maxWidth and maxHeight, function will return 1f.
     * 
     * @param texture Image that needs to be fit within dimensions
     * @param maxWidth Maximum allowed width of the image
     * @param maxHeight Maximum allowed height of the image
     * @return A scalar float (0f, 1f]
     */
    public static float getImageScalarToFit(Texture texture, int maxWidth, int maxHeight) {
        float scale = 1;
        
        if (texture.getWidth() <= maxWidth && texture.getHeight() <= maxHeight) {
            return 1;
        }
        
        if (texture.getWidth() > maxWidth) {
            scale = maxWidth / texture.getWidth();
        }
        if (texture.getHeight() * scale > maxHeight) {
            scale = maxHeight / texture.getHeight();
        }
        
        return scale;
    }
    
    /**
     * Ensures that a given Batch has began
     * 
     * @param batch Batch to begin
     */
    public static void begin(Batch batch) {
        if (!batch.isDrawing()) {
            endAll();

            batch.begin();
        }
    }
    
    /**
     * Ensures that a given Batch has began with a given Color
     * 
     * @param batch Batch to begin
     * @param color Color to apply to Batch
     */
    public static void begin(Batch batch, Color color) {
        begin(batch);
        batch.setColor(color);
    }
    
    /**
     * Ensures that a given ShapeRenderer has began with a given ShapeType
     * 
     * @param sr ShapeRenderer to begin
     * @param type ShapeType to apply to given ShapeRenderer
     */
    public static void begin(ShapeRenderer sr, ShapeRenderer.ShapeType type) {
        begin(sr);
        if (sr.getCurrentType() != type) {
            sr.set(type);
        }
    }
    
    /**
     * Ensures that a given ShapeRenderer has began
     * 
     * @param sr ShapeRenderer to begin
     */
    public static void begin(ShapeRenderer sr) {
        if (!sr.isDrawing()) {
            endAll();

            sr.begin();
        }
    }
    
    /**
     * Ensures that the given ShapeRenderer has began with a given ShapeType and Color
     * 
     * @param sr ShapeRenderer to begin
     * @param type ShapeType to apply to given ShapeRenderer
     * @param color Color to apply to given ShapeRenderer
     */
    public static void begin(ShapeRenderer sr, ShapeRenderer.ShapeType type, Color color) {
        begin(sr, type);
        sr.setColor(color);
    }
    
    /**
     * Ensures that a given ShapeRenderer has began with a given color
     * 
     * @param sr ShapeRenderer to ensure started
     * @param color Color to apply to given ShapeRenderer
     */
    public static void begin(ShapeRenderer sr, Color color) {
        begin(sr);
        sr.setColor(color);
    }
    
    /**
     * Ensures that a Batch has ended
     * 
     * @param batch Batch to end
     */
    public static void end(Batch batch) {
        if (batch.isDrawing()) {
            batch.end();
        }
    }
    
    /**
     * Ensures that a ShapeRenderer has ended
     * 
     * @param sr ShapeRenderer to end
     */
    public static void end(ShapeRenderer sr) {
        if (sr.isDrawing()) {
            sr.end();
        }
    }

    public static void endAll() {
        end(sr);
        end(batch);
    }
    
    /**
     * Disposes all batches and renderers
     */
    public static void dispose() {
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (sr != null) {
            sr.dispose();
            sr = null;
        }
        if (font != null) {
            font.dispose();
            font = null;
        }
    }
}
