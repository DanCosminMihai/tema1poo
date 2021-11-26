package action;

import database.DB;
import database.Users;
import entertainment.MovieRating;
import fileio.*;

import java.io.IOException;
import java.util.ArrayList;

public final class command {

    public static String favorite(DB db, ActionInputData action) {
        String msg = "";
        for (Users user : db.getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().containsKey(action.getTitle())) {
                    if (!user.getFavoriteMovies().contains(action.getTitle())) {
                        user.getFavoriteMovies().add(action.getTitle());
                        //success
                        msg = "success -> " + action.getTitle() + " was added as favourite";
                    } else {
                        //error duplicate
                        msg = "error -> " + action.getTitle() + " is already in favourite list";
                    }
                } else {
                    //error not seen
                    msg = "error -> " + action.getTitle() + " is not seen";
                }
            }
        }
        return msg;

    }

    public static String view(DB db, ActionInputData action) {
        String msg = "";
        for (Users user : db.getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().containsKey(action.getTitle()))
                    user.getHistory().put(action.getTitle(), user.getHistory().get(action.getTitle()) + 1);
                else
                    user.getHistory().put(action.getTitle(), 1);
                msg = "success -> " + action.getTitle() + " was viewed with total views of " + user.getHistory().get(action.getTitle());
            }
        }
        return msg;
    }

    public static String rating(DB db, ActionInputData action) {
        String msg = "";

        for (Users user : db.getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().containsKey(action.getTitle())) {
                    if (db.isMovie(action.getTitle())) {
                        if (!db.getMovie(action.getTitle()).getRatings().containsKey(action.getUsername())) {
                            msg = "success -> " + action.getTitle() + " was rated with " + action.getGrade() + " by " + action.getUsername();
                            db.getMovie(action.getTitle()).getRatings().put(action.getUsername(), action.getGrade());
                            user.setNumberOfRatings(user.getNumberOfRatings() + 1);
                        } else
                            msg = "error -> " + action.getTitle() + " has been already rated";
                    } else {
                        if (!db.getShow(action.getTitle()).getSeasons().get(action.getSeasonNumber() - 1).getRatings().containsKey(action.getUsername())) {
                            msg = "success -> " + action.getTitle() + " was rated with " + action.getGrade() + " by " + action.getUsername();
                            user.setNumberOfRatings(user.getNumberOfRatings() + 1);
                            db.getShow(action.getTitle()).getSeasons().get(action.getSeasonNumber() - 1).getRatings().put(action.getUsername(), action.getGrade());
                        } else
                            msg = "error -> " + action.getTitle() + " has been already rated";
                    }
                } else {
                    msg = "error -> " + action.getTitle() + " is not seen";
                }
            }
        }
        return msg;

    }


    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = "";
        switch (action.getType()) {
            case "favorite" -> {
                msg = favorite(db, action);
            }
            case "view" -> {
                msg = view(db, action);
            }
            case "rating" -> {
                msg = rating(db, action);;
            }
        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
