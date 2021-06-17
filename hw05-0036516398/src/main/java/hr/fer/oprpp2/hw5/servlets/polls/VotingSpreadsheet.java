package hr.fer.oprpp2.hw5.servlets.polls;

import hr.fer.oprpp2.hw5.dao.DAOProvider;
import hr.fer.oprpp2.hw5.model.PollOption;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static hr.fer.oprpp2.hw5.utils.Common.sendErrorMessage;

/**
 * Servlet used for generating xml spreadsheet containing candidates
 * and their votes in order of most votes.
 * Servlet sends xml document as response.
 *
 * @author MatejCubek
 * @project hw05-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "VotingSpreadsheet", urlPatterns = "/servleti/glasanje-xls")
public class VotingSpreadsheet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        long pollID;
        try {
            pollID = Long.parseLong(req.getParameter("pollID"));
        } catch (NumberFormatException exception) {
            sendErrorMessage(req, resp, "Invalid poll selected!");
            return;
        }

        List<PollOption> candidates = DAOProvider.getDao().getPollOptionsForPoll(pollID).stream()
                .sorted(Comparator.comparing(PollOption::getVotesCount).reversed())
                .collect(Collectors.toList());

        HSSFWorkbook workbook = generateWorkbook(candidates);

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");

        //Writing spreadsheet to response outputstream.
        workbook.write(resp.getOutputStream());
    }

    /**
     * Helper method for generating {@link HSSFWorkbook} spreadsheet class
     * from map of candidates and votes.
     *
     * @param candidates map containing candidates and their votes.
     * @return {@link HSSFWorkbook} containing spreadsheet.
     */
    private HSSFWorkbook generateWorkbook(List<PollOption> candidates) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("Poll Votes");
        HSSFRow head = sheet.createRow(0);
        head.createCell(0).setCellValue("Candidate Name");
        head.createCell(1).setCellValue("Votes");
        head.createCell(2).setCellValue("Url");

        int rowCounter = 1;
        for (var entry : candidates) {
            HSSFRow row = sheet.createRow(rowCounter++);
            row.createCell(0).setCellValue(entry.getOptionTitle());
            row.createCell(1).setCellValue(entry.getVotesCount());
            row.createCell(2).setCellValue(entry.getOptionLink());
        }

        return workbook;
    }
}
