package prior.solution.co.th.project.wonderland.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.ExportPlayerNativeRepository;
import prior.solution.co.th.project.wonderland.repository.PlayerNativeRepository;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.*;

@Service
@Slf4j
public class ExportPlayerService {

    @Value("${app.jasperFile}")
    private String jasperFile;

    @Value("${app.jasperFolder}")
    private String jasperFolder;

    private ExportPlayerNativeRepository exportPlayerNativeRepository;

    private PlayerNativeRepository playerNativeRepository;

    public ExportPlayerService(ExportPlayerNativeRepository exportPlayerNativeRepository, PlayerNativeRepository playerNativeRepository) {
        this.exportPlayerNativeRepository = exportPlayerNativeRepository;
        this.playerNativeRepository = playerNativeRepository;
    }

    public ResponseModel<Void> getPlayerPdf(HttpServletResponse response){
        ResponseModel<Void> result = new ResponseModel<>();
        response.setContentType("application/pdf");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PlayerData" + ".pdf";
        response.setHeader(headerKey, headerValue);
        log.info("getPdf");

        result.setStatus(200);
        result.setDescription("getPdf");
        try{

            String jasperFilePath = this.jasperFolder+this.jasperFile;
            List<PlayerModel> playerModels = this.playerNativeRepository.findAllPlayer();

            //Get file and compile it
            File file = ResourceUtils.getFile(jasperFilePath);
            JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(playerModels);

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cherryImage", "C:\\Users\\ASUS\\Downloads\\Cherry_season_(48216568227).jpg");
            parameters.put("createdBy", "Simplifying Tech");
            //Fill Jasper report
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
            //Export report
            JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());;


        } catch (Exception e) {
            log.info("getPdf error {}",e.getMessage());

            result.setStatus(500);
            result.setDescription("getPdf error "+e.getMessage());
        }
        return result;
    }

    public ResponseModel<Void> getPlayerCsv( HttpServletResponse response) {
        ResponseModel<Void> result = new ResponseModel<>();
        response.setContentType("application/csv");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=PlayerDataCsv" + ".csv";
        response.setHeader(headerKey, headerValue);

        log.info("getCsv");

        result.setStatus(200);
        result.setDescription("getCsv");
        try{
            OutputStream outputStream = response.getOutputStream();
            List<PlayerModel> playerModels = this.playerNativeRepository.findAllPlayer();
            this.generateCustomerReportCsv(playerModels, outputStream);
        }catch (Exception e){
            log.info("getCsv error {}",e.getMessage());

            result.setStatus(500);
            result.setDescription("getCsv error "+e.getMessage());
        }
        return result;
    }

    private void generateCustomerReportCsv(List<PlayerModel> playerModels, OutputStream outputStream) throws IOException {

        String header = String.format("%s,%s,%s,%s","id","name","attack", "balance")+"\r\n";
        outputStream.write(header.getBytes());

        for (int i = 0; i < playerModels.size(); i++) {
            int rownum = i+1;

            String data = String.format("%d,%s,%d,%.2f"
                    , playerModels.get(i).getPid()
                    , playerModels.get(i).getPname()
                    , playerModels.get(i).getAtk()
                    , playerModels.get(i).getBalance())+"\r\n";
            outputStream.write(data.getBytes());
            if(i % 20 == 0) {
                outputStream.flush();
            }
        }
        outputStream.flush();

    }

    public void getPlayerCsv2(HttpServletResponse response) throws IOException, CsvDataTypeMismatchException, CsvRequiredFieldEmptyException {
        String filename = "Player-data.csv";

        response.setContentType("application/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");

        try (PrintWriter writer = response.getWriter()) {
            StatefulBeanToCsv<PlayerModel> csvWriter = new StatefulBeanToCsvBuilder<PlayerModel>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withOrderedResults(true)
                    .build();

            // Write all players data to csv file
            csvWriter.write(this.playerNativeRepository.findAllPlayer());
        } catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
            log.error("Error occurred while exporting players data: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error occurred while exporting players data: " + e.getMessage());
        }
    }

    public void getPlayerExcel(HttpServletResponse response) throws IOException {
        String filename = "Player-excel.xlsx";

        response.setContentType("application/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        try{
            OutputStream outputStream = response.getOutputStream();

            Workbook wb = this.generatePlayerReportExcel();
            wb.write(outputStream);
            outputStream.flush();
        }catch (Exception e ){
            log.error("Error occurred while exporting players data: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error occurred while exporting players data: " + e.getMessage());
        }

    }

    private Workbook generatePlayerReportExcel(){

        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("sheet1");
        int row  = 0;
        Row headerRow = sheet1.createRow(row++);
        int headerCol = 0;
        Cell h1 = headerRow.createCell(headerCol++);
        h1.setCellValue("ID");

        Cell h2 = headerRow.createCell(headerCol++);
        h2.setCellValue("NAME");

        Cell h3 = headerRow.createCell(headerCol++);
        h3.setCellValue("ATTACK");

        Cell h4 = headerRow.createCell(headerCol++);
        h4.setCellValue("BALANCE");

        List<PlayerModel> datas = this.playerNativeRepository.findAllPlayer();
        for (int i = 0; i < datas.size(); i++) {
            int dataCol = 0;
            Row dataRow = sheet1.createRow(row++);
            Cell pId = dataRow.createCell(dataCol++);
            pId.setCellValue(datas.get(i).getPid());

            Cell pName = dataRow.createCell(dataCol++);
            pName.setCellValue(datas.get(i).getPname());

            Cell atk = dataRow.createCell(dataCol++);
            atk.setCellValue(datas.get(i).getAtk());

            Cell balance = dataRow.createCell(dataCol++);
            balance.setCellValue(datas.get(i).getBalance());
        }

        return wb;
    }


}
