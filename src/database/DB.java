package database;

import fileio.*;

import java.util.ArrayList;
import java.util.List;

public class DB {
    private final List<Actors> actors;
    private final List<Movies> movies;
    private final List<Shows> shows;
    private final List<Users> users;

    public DB() {
        this.actors = null;
        this.users = null;
        this.movies = null;
        this.shows = null;
    }

    public DB(Input input) {
        //Actors
        this.actors = new ArrayList<>();
        for (ActorInputData actor : input.getActors()) {
            Actors newActor = new Actors(actor.getName(), actor.getCareerDescription(), actor.getFilmography(), actor.getAwards());
            this.actors.add(newActor);
        }

        //Users
        this.users = new ArrayList<>();
        for (UserInputData user : input.getUsers()) {
            Users newUser = new Users(user.getUsername(), user.getSubscriptionType(), user.getHistory(), user.getFavoriteMovies(), 0);
            this.users.add(newUser);
        }

        //Movies
        this.movies = new ArrayList<>();
        for (MovieInputData movie : input.getMovies()) {
            Movies newMovie = new Movies(movie.getTitle(), movie.getYear(), movie.getCast(), movie.getGenres(), movie.getDuration());
            this.movies.add(newMovie);
        }

        //Shows
        this.shows = new ArrayList<>();
        for (SerialInputData show : input.getSerials()) {
            int duration = 0;
            ArrayList<Seasons> ses = new ArrayList<>();
            for (int i = 0; i < show.getNumberSeason(); i++) {
                ses.add(new Seasons(i + 1, show.getSeasons().get(i).getDuration()));
                duration = duration + show.getSeasons().get(i).getDuration();
            }

            Shows newShow = new Shows(show.getTitle(), show.getYear(), show.getCast(), show.getGenres(), duration, show.getNumberSeason(), ses);
            this.shows.add(newShow);
        }

    }

    public List<Actors> getActors() {
        return actors;
    }

    public List<Movies> getMovies() {
        return movies;
    }

    public List<Shows> getShows() {
        return shows;
    }

    public List<Users> getUsers() {
        return users;
    }

    public boolean isMovie(String name) {
        for (Movies movie : this.movies)
            if (movie.getTitle().equals(name))
                return true;
        return false;
    }

    public Movies getMovie(String name) {
        for (Movies movie : this.movies)
            if (movie.getTitle().equals(name))
                return movie;
        return null;
    }

    public Shows getShow(String name) {
        for (Shows show : this.shows)
            if (show.getTitle().equals(name))
                return show;
        return null;
    }
}
