package execute_actions;

import action.command;
import fileio.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public final class executeActions {

    public static void run(Input input, JSONArray arrayResult,Writer writer) throws IOException{
        for (ActionInputData action : input.getCommands()) {
            switch (action.getActionType()) {
                case "command": {
                    arrayResult.add(command.execute(action, input,writer));
                    break;
                }
                case "query": {
                    break;
                }
                case "recommendation": {
                    break;
                }
            }
        }
    }
}
