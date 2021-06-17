package hr.fer.oprpp2.hw6.blog.web.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Servlet filter that sets current user data if it is known for servlets
 * so they don't have to.
 * Filter checks session and if data exists in session which means user is logged in
 * it sets the data into request attributes.
 * <p>
 * If there is no session and user isn't logged in filter will only set currentID attribute
 * to value <code>-1</code>.
 *
 * @author MatejCubek
 * @project hw06-0036516398
 * @created 02/06/2021
 */
@WebFilter(filterName = "CommonData", urlPatterns = "/servleti/*")
public class CommonDataSetter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpSession session = req.getSession(false);
        Long id = null;
        String nickname = null;
        String firstName = null;
        String lastName = null;
        if (session != null) {
            id = (Long) session.getAttribute("current.user.id");
            firstName = (String) session.getAttribute("current.user.fn");
            lastName = (String) session.getAttribute("current.user.ln");
            nickname = (String) session.getAttribute("current.user.nick");
        }
        if (id == null) id = - 1L;
        if (firstName == null) firstName = "";
        if (lastName == null) lastName = "";
        if (nickname == null) nickname = "";

        req.setAttribute("currentID", id);
        req.setAttribute("currentFN", firstName);
        req.setAttribute("currentLN", lastName);
        req.setAttribute("currentNick", nickname);

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
