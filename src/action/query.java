package action;

import database.Actors;
import database.DB;
import fileio.ActionInputData;
import fileio.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public final class query {

    public static String userByNoRatings(ActionInputData action, DB db) {
        String msg;
        db.getUsers().sort((o1, o2) -> o2.getNumberOfRatings() - o1.getNumberOfRatings() - 1);

        int i = db.getUsers().size() - 1;
        int nr = action.getNumber();
        while (db.getUsers().get(i).getNumberOfRatings() == 0 && i > 0)
            i--;
        ArrayList<String> l = new ArrayList<>();
        while (nr > 0 && i >= 0) {
            l.add(db.getUsers().get(i).getUsername());
            i--;
            nr--;
        }

        msg = "Query result: " + l;
        return msg;
    }

    public static String actors(ActionInputData action, DB db) {
        String msg = "";
        switch (action.getCriteria()) {
            case "average" -> {
                db.getActors().sort(new Comparator<Actors>() {
                    @Override
                    public int compare(Actors o1, Actors o2) {
                        if (o2.AvgRating(db).compareTo(o1.AvgRating(db)) == 0)
                            return o1.getName().compareTo(o2.getName());
                        return -o2.AvgRating(db).compareTo(o1.AvgRating(db));
                    }
                });
                int i = 0;
                int nr = action.getNumber();
                while (db.getActors().get(i).AvgRating(db) == 0 && i < db.getActors().size())
                    i++;
                ArrayList<String> l = new ArrayList<>();
                while (nr > 0 && i < db.getActors().size()) {
                    l.add(db.getActors().get(i).getName());
                    i++;
                    nr--;
                }

                msg = "Query result: " + l;
            }
            case "awards" -> {

            }
            case "filter_description" -> {

            }
        }
        return msg;
    }

    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = "";
        switch (action.getObjectType()) {
            case "actors" -> {
                msg = actors(action, db);
            }
            case "shows" -> {
            }
            case "movies" -> {
            }
            case "users" -> {
                msg = userByNoRatings(action, db);
            }
        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
