package execute_actions;

import action.command;
import action.query;
import database.DB;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public final class executeActions {

    public static void run(Input input, JSONArray arrayResult, Writer writer) throws IOException {
        DB db = new DB(input);
        for (ActionInputData action : input.getCommands()) {
            switch (action.getActionType()) {
                case "command": {
                    arrayResult.add(command.execute(action, db, writer));
                    break;
                }
                case "query": {
                    arrayResult.add(query.execute(action, db, writer));
                    break;
                }
                case "recommendation": {
                    break;
                }
            }
        }
    }
}
