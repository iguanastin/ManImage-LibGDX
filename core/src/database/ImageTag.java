package database;

import org.json.JSONObject;

/**
 * Created by Austin on 1/17/2017.
 */
public class ImageTag {

    public static final int DESCRIPTOR_TYPE = 0;
    public static final int ARTIST_TYPE = 1;
    public static final int ORIGIN_TYPE = 2;
    public static final int CHARACTER_TYPE = 3;

    private String name;
    private int type;


    public ImageTag(String name, int type) {
        if (type > 3) {
            throw new IllegalArgumentException("Invalid tag type: " + type);
        } else {
            this.name = name;
            this.type = type;
        }
    }

    public ImageTag(String name) {
        this(name, DESCRIPTOR_TYPE);
    }

    public ImageTag(JSONObject json) {
        //TODO Implement loading from json
    }

    public String getName() {
        return name;
    }

    public boolean isName(String name) {
        return this.name.equals(name);
    }

    public int getType() {
        return type;
    }

    public boolean isType(int type) {
        return this.type == type;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImageTag) {
            ImageTag tag = (ImageTag) obj;

            if (tag.isType(getType()) && tag.isName(getName())) {
                return true;
            }
        }

        return false;
    }

}
