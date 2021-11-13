package database;

import database.Seasons;

import java.util.ArrayList;

public class Shows extends Videos {
    /**
     * Number of seasons
     */
    private final int numberOfSeasons;
    /**
     * Season list
     */
    private final ArrayList<Seasons> seasons;

    public Shows(String title, int year, ArrayList<String> cast, ArrayList<String> genres, int duration, int numberOfSeasons, ArrayList<Seasons> seasons) {
        super(title, year, cast, genres, duration);
        this.numberOfSeasons = numberOfSeasons;
        this.seasons = seasons;
    }

    public int getNumberOfSeasons() {
        return numberOfSeasons;
    }

    public ArrayList<Seasons> getSeasons() {
        return seasons;
    }
}
