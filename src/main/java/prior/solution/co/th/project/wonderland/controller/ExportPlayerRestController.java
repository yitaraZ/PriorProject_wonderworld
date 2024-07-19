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
        return this.exportPlayerService.getPlayerPdf(response);
    }

    @GetMapping("player/csv")
    public ResponseModel<Void> getPlayerCsv(HttpServletResponse response){
        return this.exportPlayerService.getPlayerCsv( response);
    }

    @GetMapping("player/csv2")
    public void getPlayerCsv2(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {
        this.exportPlayerService.getPlayerCsv2(response);
    }

    @GetMapping("player/excel")
    public void getPlayerExcel(HttpServletResponse response) throws IOException {
        this.exportPlayerService.getPlayerExcel(response);
    }
}
