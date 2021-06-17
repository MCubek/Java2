package hr.fer.oprpp2.hw4.servlets.voting;

import hr.fer.oprpp2.hw4.utils.voting.Band;
import hr.fer.oprpp2.hw4.utils.voting.VoteFileManager;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

/**
 * Servlet used for generating xml spreadsheet containing bands
 * and their votes in order of most votes.
 * Servlet sends xml document as response.
 *
 * @author MatejCubek
 * @project hw04-0036516398
 * @created 09/04/2021
 */
@WebServlet(name = "VotingSpreadsheet", urlPatterns = "/glasanje-xls")
public class VotingSpreadsheet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String bandsFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-definicija.txt");
        String votesFile = req.getServletContext().getRealPath("/WEB-INF/glasanje-rezultati.txt");
        Path bandsPath = Path.of(bandsFile);
        Path votesPath = Path.of(votesFile);

        var bandVotes = VoteFileManager.getSortedVotes(bandsPath, votesPath);
        HSSFWorkbook workbook = generateWorkbook(bandVotes);

        resp.setContentType("application/vnd.ms-excel");
        resp.setHeader("Content-Disposition", "attachment; filename=\"tablica.xls\"");

        //Writing spreadsheet to response outputstream.
        workbook.write(resp.getOutputStream());
    }

    /**
     * Helper method for generating {@link HSSFWorkbook} spreadsheet class
     * from map of bands and votes.
     *
     * @param bandVotes map containing bands and their votes.
     * @return {@link HSSFWorkbook} containing spreadsheet.
     */
    private HSSFWorkbook generateWorkbook(Map<Band, Integer> bandVotes) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet sheet = workbook.createSheet("Band votes");
        HSSFRow head = sheet.createRow(0);
        head.createCell(0).setCellValue("Band Name");
        head.createCell(1).setCellValue("Votes");
        head.createCell(2).setCellValue("Song Url");

        int rowCounter = 1;
        for (var entry : bandVotes.entrySet()) {
            HSSFRow row = sheet.createRow(rowCounter++);
            row.createCell(0).setCellValue(entry.getKey().getName());
            row.createCell(1).setCellValue(entry.getValue());
            row.createCell(2).setCellValue(entry.getKey().getUrl());
        }

        return workbook;
    }
}
