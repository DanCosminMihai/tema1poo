package database;

import java.util.ArrayList;

public class Videos {
    /**
     * Show's title
     */
    private final String title;
    /**
     * The year the show was released
     */
    private final int year;
    /**
     * Show casting
     */
    private final ArrayList<String> cast;
    /**
     * Show genres
     */
    private final ArrayList<String> genres;

    private int duration;

    public Videos(final String title, final int year,
                  final ArrayList<String> cast, final ArrayList<String> genres,
                  int duration) {
        this.title = title;
        this.year = year;
        this.cast = cast;
        this.genres = genres;
        this.duration = duration;
    }

    public int getNumberOfFavorites(DB db) {
        int nr = 0;
        for (Users user : db.getUsers())
            if (user.getFavoriteMovies().contains(this.title))
                nr++;
        return nr;
    }

    public int getViews(DB db) {
        int nr = 0;
        for (Users user : db.getUsers())
            if (user.getHistory().containsKey(this.title))
                if(user.getHistory().get(this.title) != null)
                nr += user.getHistory().get(this.title);
        return nr;
    }

    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }


    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
