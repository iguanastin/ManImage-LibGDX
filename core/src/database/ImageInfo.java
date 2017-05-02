package database;

import com.badlogic.gdx.graphics.Texture;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class ImageInfo {

    private Texture image;
    private ImageHistogram histogram;
    private File source;
    private final ArrayList<ImageTag> tags = new ArrayList<ImageTag>();
    private String md5;

    public ImageInfo(String filepath) {
        this(new File(filepath));
    }

    public ImageInfo(File file) {
        this.source = file;
        if (!file.exists()) {
            throw new IllegalArgumentException("File " + file.getAbsolutePath() + " does not exist");
        }
    }

    public ImageInfo(JSONObject json) {
        if (json.has("md5")) {
            md5 = json.getString("md5");
        }

        source = new File(json.getString("path"));

        if (json.has("tags")) {
            JSONArray tagArray = json.getJSONArray("tags");
            for (int i = 0; i < tagArray.length(); i++) {
                tags.add(new ImageTag(tagArray.getJSONObject(i)));
            }
        }

        if (json.has("histogram")) {
            histogram = new ImageHistogram(json.getJSONObject("histogram"));
        }
    }

    public boolean addTag(ImageTag tag) {
        if (tags.contains(tag)) {
            return false;
        } else {
            tags.add(tag);
            return true;
        }
    }

    public boolean removeTag(ImageTag tag) {
        return tags.remove(tag);
    }

    public boolean hasTags(ImageTag[] tagList) {
        boolean result = true;
        for (ImageTag tag : tagList) {
            if (!tags.contains(tag)) {
                result = false;
                break;
            }
        }

        return result;
    }

    public String getFilepath() {
        return source.getAbsolutePath();
    }
    
    public long getFileSizeKB() {
        return source.length()/1024;
    }

    public String getFolderPath() {
        return source.getParentFile().getAbsolutePath();
    }

    public String getFilename() {
        return source.getName();
    }

    public ArrayList<ImageTag> getTags() {
        return tags;
    }

    public Texture getTexture() {
        if (isLoaded()) {
            return image;
        } else {
            return image = new Texture(source.getAbsolutePath());
        }
    }

    public ImageHistogram getHistogram() {
        if (histogram == null) {
            return histogram = new ImageHistogram(getTexture());
        } else {
            return histogram;
        }
    }

    public String getMD5() {
//        if (md5 == null || md5.isEmpty()) {
//            return md5 = Utils.getImageMD5(getTexture());
//        } else {
//            return md5;
//        }

        //TODO: Implement md5

        return null;
    }

    public File getSource() {
        return source;
    }

    public boolean isLoaded() {
        return image != null;
    }

    public void dispose() {
        if (image != null) {
            image.dispose();
            image = null;
        }
        
        histogram = null;
        md5 = null;
        source = null;
        tags.clear();
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ImageInfo) {
            ImageInfo taggedImg = (ImageInfo) other;

            return taggedImg.getSource().equals(getSource());
        }

        return false;
    }

    @Override
    public String toString() {
        String result = source.getAbsolutePath();
        if (histogram != null) {
            result += "-[H]";
        }
        if (image != null) {
            result += "-[L]";
        }
        return result;
    }

    public JSONObject asJSONObject() {
        JSONObject json = new JSONObject();

        json.put("path", source.getAbsolutePath());

        if (md5 != null && !md5.isEmpty()) {
            json.put("md5", md5);
        }

        if (tags != null) {
            json.put("tags", tags);
        }

        if (histogram != null) {
            json.put("histogram", histogram.asJSONObject());
        }

        return json;
    }

}
