package prior.solution.co.th.project.wonderland.service;

import com.opencsv.bean.StatefulBeanToCsv;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import prior.solution.co.th.project.wonderland.model.PlayerModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.ExportPlayerNativeRepository;
import prior.solution.co.th.project.wonderland.repository.PlayerNativeRepository;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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

    public void generateCustomerReportCsv(List<PlayerModel> playerModels, OutputStream outputStream) throws IOException {

        String header = String.format("%s|%s|%s|%s","id","name","attack", "balance")+"\r\n";
        outputStream.write(header.getBytes());

        for (int i = 0; i < playerModels.size(); i++) {
            int rownum = i+1;

            String data = String.format("%d|%s|%d|%.2f"
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

    public List<PlayerModel> getPlayerCsv2(){
        return this.playerNativeRepository.findAllPlayer();
    }

}
