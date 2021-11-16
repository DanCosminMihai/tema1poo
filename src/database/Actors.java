package database;

import actor.ActorsAwards;

import java.util.ArrayList;
import java.util.Map;

public final class Actors {
    /**
     * actor name
     */
    private String name;
    /**
     * description of the actor's career
     */
    private String careerDescription;
    /**
     * videos starring actor
     */
    private ArrayList<String> filmography;
    /**
     * awards won by the actor
     */
    private Map<ActorsAwards, Integer> awards;

    public Actors(final String name, final String careerDescription,
                  final ArrayList<String> filmography,
                  final Map<ActorsAwards, Integer> awards) {
        this.name = name;
        this.careerDescription = careerDescription;
        this.filmography = filmography;
        this.awards = awards;
    }

    public Double AvgRating(DB db) {
        Double sum = 0.0;
        int n = 0;
        for (String video : this.filmography) {
            if (db.isMovie(video) && db.getMovie(video).getAverageRating() != 0) {
                sum = sum + db.getMovie(video).getAverageRating();
                n++;
            }
            if (db.getShow(video) != null && db.getShow(video).getAverageRating() != 0) {
                sum = sum + db.getShow(video).getAverageRating();
                n++;
            }
        }
        if (n == 0)
            return 0.0;
        return sum / n;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public void setFilmography(final ArrayList<String> filmography) {
        this.filmography = filmography;
    }

    public Map<ActorsAwards, Integer> getAwards() {
        return awards;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public void setCareerDescription(final String careerDescription) {
        this.careerDescription = careerDescription;
    }

    public void setAwards(Map<ActorsAwards, Integer> awards) {
        this.awards = awards;
    }

    @Override
    public String toString() {
        return "ActorInputData{"
                + "name='" + name + '\''
                + ", careerDescription='"
                + careerDescription + '\''
                + ", filmography=" + filmography + '}';
    }
}
