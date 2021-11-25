package action;

import database.Actors;
import database.DB;
import database.Users;
import fileio.ActionInputData;
import fileio.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

public final class query {

    public static String userByNoRatings(ActionInputData action, DB db) {
        String msg;
        ArrayList<Users> l = new ArrayList<>(db.getUsers());
        l.removeIf((e) -> e.getNumberOfRatings() == 0);
        if (action.getSortType().equals("desc"))
            l.sort((o1, o2) -> o2.getNumberOfRatings() - o1.getNumberOfRatings());
        else
            l.sort((o2, o1) -> o2.getNumberOfRatings() - o1.getNumberOfRatings());

        msg = "Query result: " + (ArrayList<String>)
                l.stream().map(Users::getUsername).limit(action.getNumber()).collect(Collectors.toList());
        return msg;
    }

    public static String actors(ActionInputData action, DB db) {
        String msg = "";
        switch (action.getCriteria()) {
            case "average" -> {
                ArrayList<Actors> l = new ArrayList<>(db.getActors());
                l.removeIf((e) -> e.AvgRating(db) == 0);
                if (action.getSortType().equals("asc"))
                    l.sort((o1, o2) -> {
                        if (o2.AvgRating(db).compareTo(o1.AvgRating(db)) == 0)
                            return o1.getName().compareTo(o2.getName());
                        return -o2.AvgRating(db).compareTo(o1.AvgRating(db));
                    });
                else
                    db.getActors().sort((o1, o2) -> {
                        if (o2.AvgRating(db).compareTo(o1.AvgRating(db)) == 0)
                            return o2.getName().compareTo(o1.getName());
                        return -o2.AvgRating(db).compareTo(o1.AvgRating(db));
                    });

                msg = "Query result: " + (ArrayList<String>)
                        l.stream().map(Actors::getName).limit(action.getNumber()).collect(Collectors.toList());
            }
            case "awards" -> {
                ArrayList<Actors> l = new ArrayList<>(db.getActors());
                action.getFilters().get(3).forEach((award) -> {
                    l.removeIf((actor) -> !actor.getAwards().containsKey(award));
                });
                if (action.getSortType().equals("desc"))
                    l.sort((a1, a2) -> a1.getAwardsNumber() - a2.getAwardsNumber());
                else
                    l.sort((a2, a1) -> a1.getAwardsNumber() - a2.getAwardsNumber());
                msg = "Query result: " + (ArrayList<String>)
                        l.stream().map(Actors::getName).limit(action.getNumber()).collect(Collectors.toList());
            }
            case "filter_description" -> {
                ArrayList<Actors> l = new ArrayList<>(db.getActors());
                action.getFilters().get(2).forEach((word) -> {
                    l.removeIf((actor) -> !actor.getCareerDescription().contains(word));
                });
                if (action.getSortType().equals("asc"))
                    l.sort((a1, a2) -> a1.getName().compareTo(a2.getName()));
                else
                    l.sort((a2, a1) -> a1.getName().compareTo(a2.getName()));
                msg = "Query result: " + (ArrayList<String>)
                        l.stream().map(Actors::getName).limit(action.getNumber()).collect(Collectors.toList());
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
