package database;

import java.util.ArrayList;
import java.util.HashMap;

public class Movies extends Videos {
    private HashMap<String, Double> ratings;

    public Movies(String title, int year, ArrayList<String> cast, ArrayList<String> genres, int duration) {
        super(title, year, cast, genres, duration);
        this.ratings = new HashMap<>();
    }

    public Double getAverageRating() {
        if (ratings.isEmpty())
            return 0.0;
        Double sum = 0.0;
        for (Double rating : ratings.values())
            sum = sum + rating;
        return sum / ratings.size();
    }
    public HashMap<String, Double> getRatings() {
        return ratings;
    }

    public void setRatings(HashMap<String, Double> ratings) {
        this.ratings = ratings;
    }
}
