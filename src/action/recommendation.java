package action;

import com.fasterxml.jackson.core.JsonPointer;
import database.DB;
import fileio.ActionInputData;
import fileio.Writer;

import java.io.IOException;

public final class recommendation {

    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = "";
        switch (action.getType()) {
            case "standard" -> {

            }
            case "best_unseen" -> {

            }
            case "popular" -> {
                if (db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "PopularRecommendation cannot be applied!";
                else
                    msg = "";
            }
            case "favorite" -> {
                if (db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "FavoriteRecommendation cannot be applied!";
                else
                    msg = "";
            }
            case "search" -> {
                if (db.getUser(action.getUsername()).getSubscriptionType().equals("PREMIUM"))
                    msg = "SearchRecommendation cannot be applied!";
                else
                    msg = "";
            }

        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
