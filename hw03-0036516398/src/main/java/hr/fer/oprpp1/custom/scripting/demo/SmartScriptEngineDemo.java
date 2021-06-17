package hr.fer.oprpp1.custom.scripting.demo;

import hr.fer.oprpp1.custom.scripting.exec.SmartScriptEngine;
import hr.fer.oprpp1.custom.scripting.nodes.DocumentNode;
import hr.fer.oprpp1.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo test class.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 29/03/2021
 */
public class SmartScriptEngineDemo {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Required 1 argument filepath.");
            return;
        }
        Path fileName = Path.of(args[0]);

        String documentBody = readFromDisk(fileName);
        Map<String, String> parameters = new HashMap<String, String>();
        Map<String, String> persistentParameters = new HashMap<String, String>();
        List<RequestContext.RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
// put some parameter into parameters map
// create engine and execute it
        RequestContext rc = new RequestContext(System.out, parameters, persistentParameters, cookies);
        new SmartScriptEngine(
                (DocumentNode) new SmartScriptParser(documentBody).getHeadNode(),
                rc
        ).execute();
    }

    private static String readFromDisk(Path fileName) {
        try {
            return Files.readString(fileName);
        } catch (IOException e) {
            return "";
        }
    }
}
