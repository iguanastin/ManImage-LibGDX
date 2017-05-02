package database;

import java.util.Comparator;

/**
 * Created by Austin on 1/17/2017.
 */
public class FilenameComparator implements Comparator<ImageInfo> {

    private boolean descending = false;


    public FilenameComparator(boolean descending) {
        this.descending = descending;
    }

    @Override
    public int compare(ImageInfo o1, ImageInfo o2) {
        if (descending) {
            return o1.getSource().getName().compareTo(o2.getSource().getName());
        } else {
            return -o1.getSource().getName().compareTo(o2.getSource().getName());
        }
    }

}
