package database;

public class TagSearcher implements DatabaseSearcher {

    private final ImageTag[] tags;


    public TagSearcher(ImageTag ... tags) {
        this.tags = tags;
    }

    @Override
    public boolean isValid(ImageInfo info) {
        return info.hasTags(tags);
    }

}
