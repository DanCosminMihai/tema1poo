package action;

import fileio.ActionInputData;
import fileio.Input;
import fileio.Writer;

import java.io.IOException;

public final class query {


    public static Object query(ActionInputData action, Input input, Writer writer) throws IOException{
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
                break;
            }

        }
        return writer.writeFile(action.getActionId(), "", msg);

    }
}
