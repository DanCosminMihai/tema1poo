package action;

import com.fasterxml.jackson.core.JsonPointer;
import database.DB;
import database.Movies;
import database.Shows;
import database.Users;
import fileio.ActionInputData;
import fileio.Writer;

import java.io.IOException;
import java.util.ArrayList;

public final class recommendation {

    public static String standard(ActionInputData action, DB db) {
        Users user = db.getUser(action.getUsername());

        for(Movies movie : db.getMovies()) {
            if(!user.getHistory().containsKey(movie.getTitle()))
                return movie.getTitle();
        }
        for(Shows show: db.getShows()) {
            if(!user.getHistory().containsKey(show.getTitle()))
                return show.getTitle();
        }

        return "StandardRecommendation cannot be applied!";
    }

    public static String best_unseen(ActionInputData action, DB db) {
        Users user = db.getUser(action.getUsername());

        ArrayList<Movies> m = new ArrayList<>(db.getMovies());
        m.sort((m1,m2)->m1.getAverageRating().compareTo(m2.getAverageRating()));
        for(Movies movie : m) {
            if(!user.getHistory().containsKey(movie.getTitle()))
                return movie.getTitle();
        }

        ArrayList<Shows> s = new ArrayList<>(db.getShows());
        s.sort((s1,s2)->s1.getAverageRating().compareTo(s2.getAverageRating()));
        for(Shows show: s) {
            if(!user.getHistory().containsKey(show.getTitle()))
                return show.getTitle();
        }

        return "BestRatedUnseenRecommendation cannot be applied!";
    }

    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = "";
        switch (action.getType()) {
            case "standard" -> {
                msg = "StandardRecommendation result: " + standard(action, db);
            }
            case "best_unseen" -> {
                msg = "BestRatedUnseenRecommendation result: " + best_unseen(action,db);
            }
            case "popular" -> {
                if (!db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "PopularRecommendation cannot be applied!";
                else
                    msg = "";
            }
            case "favorite" -> {
                if (!db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "FavoriteRecommendation cannot be applied!";
                else
                    msg = "";
            }
            case "search" -> {
                if (!db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "SearchRecommendation cannot be applied!";
                else
                    msg = "";
            }

        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
