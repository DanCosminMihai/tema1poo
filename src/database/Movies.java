package database;

import java.util.ArrayList;
import java.util.HashMap;

public class Movies extends Videos {
    private static HashMap<String, Double> ratings;

    public Movies(String title, int year, ArrayList<String> cast, ArrayList<String> genres, int duration) {
        super(title, year, cast, genres, duration);
        this.ratings = new HashMap<>();
    }

    public static HashMap<String, Double> getRatings() {
        return ratings;
    }

    public static void setRatings(HashMap<String, Double> ratings) {
        Movies.ratings = ratings;
    }
}
