package hr.fer.oprpp2.hw5.filters;

import hr.fer.oprpp2.hw5.dao.sql.SQLConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.sql.DataSource;

@WebFilter(filterName = "dbConnectionFilter", urlPatterns = {"/servleti/*"})
public class ConnectionSetterFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        //noinspection SpellCheckingInspection
        DataSource ds = (DataSource) request.getServletContext().getAttribute("hr.fer.zemris.dbpool");
        Connection con;
        try {
            con = ds.getConnection();
        } catch (SQLException e) {
            throw new IOException("Database unavailable.", e);
        }
        SQLConnectionProvider.setConnection(con);
        try {
            chain.doFilter(request, response);
        } finally {
            SQLConnectionProvider.setConnection(null);
            try {
                con.close();
            } catch (SQLException ignored) {
            }
        }
    }

}