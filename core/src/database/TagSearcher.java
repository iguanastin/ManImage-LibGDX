package database;

/**
 * Created by Austin on 1/17/2017.
 */
public class TagSearcher implements DatabaseSearcher {

    private ImageTag[] tags;


    public TagSearcher(ImageTag ... tags) {
        this.tags = tags;
    }

    @Override
    public boolean isValid(ImageInfo info) {
        if (info.hasTags(tags)) {
             return true;
        }

        return false;
    }

}
