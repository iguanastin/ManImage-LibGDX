package database;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImageDatabase {

    private final ArrayList<ImageInfo> images = new ArrayList<ImageInfo>();

    public ImageDatabase() {
    }
    
    public ImageDatabase(File file) {
        loadFromFile(file);
    }

    public ArrayList<ImageInfo> find(DatabaseSearcher searcher) {
        ArrayList<ImageInfo> results = new ArrayList<ImageInfo>();

        for (ImageInfo img : getAll()) {
            if (searcher.isValid(img)) {
                results.add(img);
            }
        }

        return results;
    }
    
    public ArrayList<ImageInfo> getAll() {
        return images;
    }
    
    public boolean addImage(ImageInfo img) {
        if (!images.contains(img)) {
            images.add(img);
            return true;
        }
        
        return false;
    }
    
    public boolean remove(ImageInfo img) {
        if (images.contains(img)) {
            images.remove(img);
            images.get(images.indexOf(img)).dispose();
            return true;
        }
        
        return false;
    }
    
    public void writeToFile(File file) {
        if (file.exists()) {
            File moveTo = new File(file.getAbsolutePath() + ".old");
            file.getAbsoluteFile().renameTo(moveTo);
        }
        
        JSONObject json = new JSONObject();
        
        for (ImageInfo img : images) {
            json.append("images", img.asJSONObject());
        }
        
        try {
            PrintWriter writer = new PrintWriter(file);
            
            writer.println(json.toString());
            
            writer.close();
        } catch (IOException ex) {
            Gdx.app.error("Writing Database", "Failed to write database to file: " + file.getAbsolutePath(), ex);
        }
    }

    public boolean loadFromFile(File file) {
        try {
            Scanner scan = new Scanner(file);
            
            StringBuilder jsonString = new StringBuilder();
            while (scan.hasNextLine()) {
                jsonString.append(scan.nextLine());
            }
            
            loadFromJSON(new JSONObject(jsonString.toString()));
            
            scan.close();
            return true;
        } catch (IOException ex) {
            Gdx.app.error("Loading Database", "Failed to load database from file: " + file.getAbsolutePath(), ex);
            return false;
        }
    }
    
    private void loadFromJSON(JSONObject json) {
        JSONArray imgArray = json.getJSONArray("images");
        
        for (int i = 0; i < imgArray.length(); i++) {
            images.add(new ImageInfo(imgArray.getJSONObject(i)));
        }
    }
    
    public void dispose() {
        for (ImageInfo img : images) {
            img.dispose();
        }
    }
    
    public String toString() {
        StringBuilder result = new StringBuilder("Image Database (" + images.size() + " images): ");
        
        for (ImageInfo img : images) {
            result.append("\n  ").append(img);
        }
        
        return result.toString();
    }

}
