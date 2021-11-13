package action;

import database.DB;
import database.Users;
import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

public final class query {

    public static String userByNoRatings(ActionInputData action, DB db) {
        String msg = new String();
        db.getUsers().sort(new Comparator<Users>() {
            @Override
            public int compare(Users o1, Users o2) {
                return o2.getNumberOfRatings() - o1.getNumberOfRatings() - 1;
            }
        });

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

    public static Object execute(ActionInputData action, DB db, Writer writer) throws IOException {
        String msg = new String();
        switch (action.getObjectType()) {
            case "actors": {
                break;
            }
            case "shows": {
                break;
            }
            case "movies": {
                break;
            }
            case "users": {
                msg = userByNoRatings(action, db);
                break;
            }

        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
