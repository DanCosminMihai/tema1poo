package action;

import com.fasterxml.jackson.core.JsonPointer;
import database.DB;
import database.Movies;
import database.Shows;
import database.Users;
import entertainment.Genre;
import fileio.ActionInputData;
import fileio.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public final class recommendation {

    public static String standard(ActionInputData action, DB db) {
        Users user = db.getUser(action.getUsername());

        for (Movies movie : db.getMovies()) {
            if (!user.getHistory().containsKey(movie.getTitle()))
                return "StandardRecommendation result: " + movie.getTitle();
        }
        for (Shows show : db.getShows()) {
            if (!user.getHistory().containsKey(show.getTitle()))
                return "StandardRecommendation result: " + show.getTitle();
        }

        return "StandardRecommendation cannot be applied!";
    }

    public static String best_unseen(ActionInputData action, DB db) {
        Users user = db.getUser(action.getUsername());
        Movies best_m = null;
        Shows best_s = null;

        ArrayList<Movies> m = new ArrayList<>(db.getMovies());
        m.sort((m2, m1) -> m1.getAverageRating().compareTo(m2.getAverageRating()));
        for (Movies movie : m) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                best_m = movie;
                break;

            }
        }

        ArrayList<Shows> s = new ArrayList<>(db.getShows());
        s.sort((s2, s1) -> s1.getAverageRating().compareTo(s2.getAverageRating()));
        for (Shows show : s) {
            if (!user.getHistory().containsKey(show.getTitle())) {
                best_s = show;
                break;
            }
        }
        if (best_m != null && best_s != null)
            if (best_m.getAverageRating() >= best_s.getAverageRating())
                return "BestRatedUnseenRecommendation result: " + best_m.getTitle();
            else
                return "BestRatedUnseenRecommendation result: " + best_s.getTitle();
        if (best_s == null && best_m != null)
            return "BestRatedUnseenRecommendation result: " + best_m.getTitle();
        if (best_m == null && best_s != null)
            return "BestRatedUnseenRecommendation result: " + best_s.getTitle();

        return "BestRatedUnseenRecommendation cannot be applied!";
    }


    public static String popular(ActionInputData action, DB db) {
        Set<String> g = new LinkedHashSet<>();
        for (Movies movie : db.getMovies())
            g.addAll(movie.getGenres());
        for (Shows show : db.getShows())
            g.addAll(show.getGenres());
        ArrayList<String> genres = new ArrayList<>(g);
        int views[];
        int n = genres.size();
        views = new int[n];
        for (int i = 0; i < n; i++)
            views[i] = 0;

        for (int i = 0; i < n; i++) {
            for (Movies movie : db.getMovies())
                if (movie.getGenres().contains(genres.get(i)))
                    views[i] += movie.getViews(db);
            for (Shows show : db.getShows())
                if (show.getGenres().contains(genres.get(i)))
                    views[i] += show.getViews(db);
        }

        for (int i = 0; i < n - 1; i++)
            for (int j = i + 1; j < n; j++)
                if (views[i] < views[j]) {
                    Collections.swap(genres, i, j);
                    int aux = views[i];
                    views[i] = views[j];
                    views[j] = aux;
                }

        Users user = db.getUser(action.getUsername());
        for (int i = 0; i < n; i++) {
            for (Movies movie : db.getMovies()) {
                if (!user.getHistory().containsKey(movie.getTitle()) && movie.getGenres().contains(genres.get(i)))
                    return "PopularRecommendation result: " + movie.getTitle();
            }
            for (Shows show : db.getShows()) {
                if (!user.getHistory().containsKey(show.getTitle()) && show.getGenres().contains(genres.get(i)))
                    return "PopularRecommendation result: " + show.getTitle();
            }
        }

        return "PopularRecommendation cannot be applied!";
    }

    public static String favorite(ActionInputData action, DB db) {
        Users user = db.getUser(action.getUsername());
        Movies best_m = null;
        Shows best_s = null;
        ArrayList<Movies> m = new ArrayList<>(db.getMovies());
        m.removeIf((m1) -> m1.getNumberOfFavorites(db) == 0);
        m.sort((m1, m2) -> m2.getNumberOfFavorites(db) - m1.getNumberOfFavorites(db));
        for (Movies movie : m) {
            if (!user.getHistory().containsKey(movie.getTitle())) {
                best_m = movie;
                break;
            }
        }

        ArrayList<Shows> s = new ArrayList<>(db.getShows());
        s.removeIf((s1) -> s1.getNumberOfFavorites(db) == 0);
        s.sort((s1, s2) -> s2.getNumberOfFavorites(db) - s1.getNumberOfFavorites(db));
        for (Shows show : s) {
            if (!user.getHistory().containsKey(show.getTitle())) {
                best_s = show;
                break;
            }
        }

        if (best_m != null && best_s != null)
            if (best_m.getNumberOfFavorites(db) >= best_s.getNumberOfFavorites(db))
                return "FavoriteRecommendation result: " + best_m.getTitle();
            else
                return "FavoriteRecommendation result: " + best_s.getTitle();
        if (best_s == null && best_m != null)
            return "FavoriteRecommendation result: " + best_m.getTitle();
        if (best_m == null && best_s != null)
            return "FavoriteRecommendation result: " + best_s.getTitle();


        return "FavoriteRecommendation cannot be applied!";
    }

    public static String search(ActionInputData action, DB db) {
        Users user = db.getUser(action.getUsername());

        ArrayList<Movies> m = new ArrayList<>(db.getMovies());
        m.removeIf((m1) -> !m1.getGenres().contains(action.getGenre()));
        m.removeIf((m1) -> user.getHistory().containsKey(m1.getTitle()));
        m.sort((m1, m2) -> {
            if ((m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db)) == 0)
                return m1.getTitle().compareTo(m2.getTitle());
            return m1.getNumberOfFavorites(db) - m2.getNumberOfFavorites(db);
        });

        ArrayList<Shows> s = new ArrayList<>(db.getShows());
        s.removeIf((s1) -> !s1.getGenres().contains(action.getGenre()));
        s.removeIf((s1) -> user.getHistory().containsKey(s1.getTitle()));
        s.sort((s1, s2) -> {
            if ((s1.getNumberOfFavorites(db) - s2.getNumberOfFavorites(db)) == 0)
                return s1.getTitle().compareTo(s2.getTitle());
            return s1.getNumberOfFavorites(db) - s2.getNumberOfFavorites(db);
        });
        ArrayList<String> results = new
                ArrayList<String>(m.stream().map(Movies::getTitle).collect(Collectors.toList()));
        results.addAll(new
                ArrayList<String>(s.stream().map(Shows::getTitle).collect(Collectors.toList())));
        results.sort(((o1, o2) -> o1.compareTo(o2)));
        if (!results.isEmpty())
            return "SearchRecommendation result: " + results;

        return "SearchRecommendation cannot be applied!";
    }

    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = "";
        switch (action.getType()) {
            case "standard" -> {
                msg = standard(action, db);
            }
            case "best_unseen" -> {
                msg = best_unseen(action, db);
            }
            case "popular" -> {
                if (!db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "PopularRecommendation cannot be applied!";
                else
                    msg = popular(action, db);
            }
            case "favorite" -> {
                if (!db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "FavoriteRecommendation cannot be applied!";
                else
                    msg = favorite(action, db);
            }
            case "search" -> {
                if (!db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "SearchRecommendation cannot be applied!";
                else
                    msg = search(action, db);
            }

        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
