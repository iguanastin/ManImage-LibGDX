package database;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import java.util.Arrays;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImageHistogram {

    private int[] histogramRed;
    private int[] histogramGreen;
    private int[] histogramBlue;
    private int pixelCount;
    
    private static final int BINS = 64;
    private static final int MAX_DIFF = 6;
    
    public ImageHistogram(Texture tex) {
        if (tex == null) {
            throw new NullPointerException("");
        }
        
        initializeHistogram(tex);
    }
    
    public ImageHistogram(JSONObject json) {
        initializeFromJSON(json);
    }

    private void initializeFromJSON(JSONObject json) {
        histogramRed = new int[BINS];
        histogramGreen = new int[BINS];
        histogramBlue = new int[BINS];
        
        JSONArray redArray = json.getJSONArray("red");
        for (int i = 0; i < redArray.length(); i++) {
            histogramRed[i] = redArray.getInt(i);
        }
        
        JSONArray greenArray = json.getJSONArray("green");
        for (int i = 0; i < greenArray.length(); i++) {
            histogramGreen[i] = greenArray.getInt(i);
        }
        
        JSONArray blueArray = json.getJSONArray("blue");
        for (int i = 0; i < blueArray.length(); i++) {
            histogramBlue[i] = blueArray.getInt(i);
        }
        
        pixelCount = json.getInt("pixels");
    }
    
    private void initializeHistogram(Texture texture) {
        histogramRed = new int[BINS];
        histogramGreen = new int[BINS];
        histogramBlue = new int[BINS];
        
        pixelCount = texture.getWidth() * texture.getHeight();
        
        if (!texture.getTextureData().isPrepared()) {
            texture.getTextureData().prepare();
        }
        
        Pixmap pixmap = texture.getTextureData().consumePixmap();
        for (int y = 0; y < texture.getHeight(); y++) {
            for (int x = 0; x < texture.getWidth(); x++) {
                Color color = new Color(pixmap.getPixel(x, y));
                float red = color.r;
                float green = color.g;
                float blue = color.b;
                
                int redIndex = (int)Math.floor(red*BINS);
                if (redIndex >= BINS) redIndex--;
                histogramRed[redIndex]++;
                int greenIndex = (int)Math.floor(green*BINS);
                if (greenIndex >= BINS) greenIndex--;
                histogramGreen[greenIndex]++;
                int blueIndex = (int)Math.floor(blue*BINS);
                if (blueIndex >= BINS) blueIndex--;
                histogramBlue[blueIndex]++;
            }
        }
    }
    
    public float getSimilarityTo(ImageHistogram other) {
        float similarity = 0;
        
        for (int i = 0; i < BINS; i++) {
            similarity += Math.abs((float)other.histogramRed[i]/other.getPixelCount() - (float)histogramRed[i]/getPixelCount());
        }
        for (int i = 0; i < BINS; i++) {
            similarity += Math.abs((float)other.histogramGreen[i]/other.getPixelCount() - (float)histogramGreen[i]/getPixelCount());
        }
        for (int i = 0; i < BINS; i++) {
            similarity += Math.abs((float)other.histogramBlue[i]/other.getPixelCount() - (float)histogramBlue[i]/getPixelCount());
        }
        
        return 1 - similarity/MAX_DIFF;
    }
    
    public static float getSimilarity(Texture tex1, Texture tex2) {
        ImageHistogram texHist1 = new ImageHistogram(tex1);
        ImageHistogram texHist2 = new ImageHistogram(tex2);
        
        return texHist1.getSimilarityTo(texHist2);
    }

    public int getPixelCount() {
        return pixelCount;
    }
    
    @Override
    public String toString() {
        String result = "";
        
        result += "Red Histogram: " + Arrays.toString(histogramRed) + "\n";
        result += "Green Histogram: " + Arrays.toString(histogramGreen) + "\n";
        result += "Blue Histogram: " + Arrays.toString(histogramBlue) + "\n";
        
        return result;
    }
    
    public JSONObject asJSONObject() {
        JSONObject json = new JSONObject();
        
        json.put("red", histogramRed);
        json.put("green", histogramGreen);
        json.put("blue", histogramBlue);
        json.put("pixels", getPixelCount());
        
        return json;
    }

}