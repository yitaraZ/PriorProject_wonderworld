package prior.solution.co.th.project.wonderland.controller;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.ExportPlayerService;

import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("api")
public class ExportPlayerRestController {
    private ExportPlayerService exportPlayerService;

    public ExportPlayerRestController(ExportPlayerService exportPlayerService) {
        this.exportPlayerService = exportPlayerService;
    }

    @GetMapping("player/pdf")
    public ResponseModel<Void> getPlayerPdf(HttpServletResponse response){
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PlayerData" + ".pdf";
        response.setHeader(headerKey, headerValue);

        return this.exportPlayerService.getPlayerPdf(response);
    }

    @GetMapping("player/csv")
    public ResponseModel<Void> getPlayerCsv(HttpServletResponse response){
        response.setContentType("application/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PlayerDataCsv" + ".csv";
        response.setHeader(headerKey, headerValue);

        return this.exportPlayerService.getPlayerCsv( response);
    }

    @GetMapping("player/csv2")
    public void getPlayerCsv2(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        //set file name and content type
        String filename = "Player-data.csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        //create a csv writer
        StatefulBeanToCsv<PlayerModel> writer = new StatefulBeanToCsvBuilder<PlayerModel>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
                .build();
        //write all employees data to csv file
        writer.write(this.exportPlayerService.getPlayerCsv2());
    }
}
