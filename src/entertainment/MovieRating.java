package entertainment;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

public class MovieRating {
    private static HashMap<String, ArrayList<Double>> ratings = null;

    public MovieRating() {
    }

    public static HashMap<String, ArrayList<Double>> getRatings() {
        if (ratings == null) {
            ratings = new HashMap<String, ArrayList<Double>>();
        }
        return ratings;
    }

}
