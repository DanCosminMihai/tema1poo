package action;

import common.Constants;
import entertainment.MovieRating;
import fileio.*;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class command {

    public static String favorite(Input input, ActionInputData action) {
        String msg = new String();
        for (UserInputData user : input.getUsers()) {
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

    public static String view(Input input, ActionInputData action) {
        String msg = new String();
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().containsKey(action.getTitle()))
                    user.getHistory().put(action.getTitle(), user.getHistory().get(action.getTitle() + 1));
                else
                    user.getHistory().put(action.getTitle(), 1);
                msg = "success -> " + action.getTitle() + " was viewed with total views of " + user.getHistory().get(action.getTitle());

            }
        }
        return msg;
    }

    public static String rating(Input input, ActionInputData action) {
        String msg = new String();
        MovieRating rating = null;
        for (UserInputData user : input.getUsers()) {
            if (user.getUsername().equals(action.getUsername())) {
                if (user.getHistory().containsKey(action.getTitle())) {
                    if (action.getSeasonNumber() != 0) {

                        for (SerialInputData show : input.getSerials())
                            if (show.getTitle().equals(action.getTitle())) {
                                show.getSeasons().get(action.getSeasonNumber() - 1).getRatings().add(action.getGrade());
                                msg = "success -> " + action.getTitle() + " was rated with " + action.getGrade() + " by " + action.getUsername();
                            }
                    } else {
                        rating.getRatings().putIfAbsent(action.getTitle(),new ArrayList<Double>());
                        ArrayList<Double> list = rating.getRatings().get(action.getTitle());
                        list.add(action.getGrade());
                        rating.getRatings().put(action.getTitle(), list);
                        msg = "success -> " + action.getTitle() + " was rated with " + action.getGrade() + " by " + action.getUsername();
                    }
                } else {
                    //error not seen
                    msg = "error -> " + action.getTitle() + " is not seen";
                }
            }
        }
        return msg;
    }

    public static Object execute(ActionInputData action, Input input, Writer writer) throws IOException {
        String msg = new String();
        switch (action.getType()) {
            case "favorite": {
                msg = favorite(input, action);
                break;
            }
            case "view": {
                msg = view(input, action);
                break;
            }
            case "rating": {
                msg = rating(input, action);
                break;
            }
        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
