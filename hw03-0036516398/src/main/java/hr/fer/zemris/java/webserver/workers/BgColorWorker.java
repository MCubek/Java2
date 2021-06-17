package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Worker implementing {@link IWebWorker} that changes background color of
 * index2.html page.
 * Worker requests one parameter in the request called <code>bgcolor<code>
 * that has to be a six digit hex value and represents the color of the index page.
 * That color parsed is put into session context of client.
 * Worker draws a html page with a message if the color has been set and a link to
 * homepage.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 31/03/2021
 */
public class BgColorWorker implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        String color = context.getParameter("bgcolor");
        boolean valid = false;
        if (color.matches("[0-9a-fA-F]{6}")) {
            valid = true;
            context.setPersistentParameter("bgcolor", color);
        }

        context.setStatusCode(200);
        context.setStatusText("OK");
        context.setMimeType("text/html");
        //noinspection HtmlUnknownTarget
        context.write(String.format("""
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <title>BgColorChange</title>
                </head>
                <body>
                                
                <a href="index2.html">Index2</a>
                <br/>
                Color is%s updated!
                                
                </body>
                </html>
                """, valid ? "" : " not"));
    }
}
