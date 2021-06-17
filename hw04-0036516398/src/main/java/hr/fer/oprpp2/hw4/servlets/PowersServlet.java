package hr.fer.oprpp2.hw4.servlets;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for displaying dynamic xml spreadsheet with powers table from arguments.
 * Servlet accepts arguments a,b and n.
 * A represents starting number, B ending number and n number of sheets.
 * A and B need to be in interval [-100,100] and n [1,5].
 * Servlet checks arguments from get method and generates xml spreadsheet
 * with powers of a to b to the power of current sheet.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "Powers", urlPatterns = "/powers")
public class PowersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer a = null;
        Integer b = null;
        Integer n = null;
        try {
            a = Integer.parseInt(req.getParameter("a"));
        } catch (NumberFormatException ignored) {
        }
        try {
            b = Integer.parseInt(req.getParameter("b"));
        } catch (NumberFormatException ignored) {
        }
        try {
            n = Integer.parseInt(req.getParameter("n"));
        } catch (NumberFormatException ignored) {
        }

        if (a == null || a < - 100 || a > 100) {
            sendErrorMessage(req, resp, "Parameter a is invalid.");
            return;
        }
        if (b == null || b < - 100 || b > 100) {
            sendErrorMessage(req, resp, "Parameter b is invalid.");
            return;
        }
        if (n == null || n < 1 || n > 5) {
            sendErrorMessage(req, resp, "Parameter n is invalid.");
            return;
        }

        HSSFWorkbook workbook = generateWorkbook(a, b, n);
        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");

        workbook.write(resp.getOutputStream());
    }

    private void sendErrorMessage(HttpServletRequest req, HttpServletResponse resp, String message) throws
            ServletException, IOException {
        req.setAttribute("MESSAGE", message);
        req.getRequestDispatcher("/WEB-INF/pages/errorMessage.jsp").forward(req, resp);
    }

    /**
     * Helper method for generating spreadsheet workbook.
     *
     * @param a starting number
     * @param b ending number
     * @param n number of sheets
     * @return spreadsheet workbook
     */
    private HSSFWorkbook generateWorkbook(Integer a, Integer b, Integer n) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        for (int i = 1; i <= n; i++) {
            HSSFSheet sheet = workbook.createSheet("Sheet " + i);
            for (int j = 0; j + a <= b; j++) {
                HSSFRow row = sheet.createRow(j);
                row.createCell(0).setCellValue(a + j);
                row.createCell(1).setCellValue(Math.pow(a + j, i));
            }
        }

        return workbook;
    }
}
