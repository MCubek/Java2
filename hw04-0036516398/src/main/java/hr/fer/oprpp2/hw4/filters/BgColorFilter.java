package hr.fer.oprpp2.hw4.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Class injecting a variable used for background color.
 * Sets a temporary variable used by jsp pages to render html.
 * Color is obtained from the session or if session doesn't contain
 * the variable it sets the variable to white color.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 08/04/2021
 */
@WebFilter(filterName = "ColorFilter", urlPatterns = "/*")
public class BgColorFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        String color = null;
        if (session != null)
            color = (String) session.getAttribute("pickedBgCol");

        if (color == null) color = "#FFFFFF";

        req.setAttribute("bgCol", color);

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}


