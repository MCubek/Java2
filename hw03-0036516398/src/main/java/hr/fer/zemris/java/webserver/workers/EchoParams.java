package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * Class that implements {@link IWebWorker} and prints all passed parameters
 * as html table-
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 30/03/2021
 */
@SuppressWarnings("unused")
public class EchoParams implements IWebWorker {
    @Override
    public void processRequest(RequestContext context) throws Exception {
        context.setMimeType("text/html");

        StringBuilder stringBuilder = new StringBuilder();

        for (String param : context.getParameterNames()) {
            stringBuilder.append(String.format("""
                    <tr>
                        <td>
                            %s
                        </td>
                        <td>
                            %s
                        </td>
                    </tr>
                    """, param, context.getParameter(param)));
        }

        //language=HTML
        String html = String.format("""
                <html lang="en">
                <head>
                <style>
                    table{
                    font-family: arial, sans-serif;
                    border-collapse: collapse;
                    }
                    td, th {
                      border: 1px solid #dddddd;
                      text-align: left;
                      padding: 8px;
                    }
                </style><title>My Table</title>
                </head>
                    <body>
                        <table>
                            <thead>
                              <tr>
                                <th>Key</th>
                                <th>Value</th>
                                </tr>
                            </thead>
                           %s
                        </table>
                    </body>
                </html>
                """, stringBuilder);

        context.write(html);
    }
}
