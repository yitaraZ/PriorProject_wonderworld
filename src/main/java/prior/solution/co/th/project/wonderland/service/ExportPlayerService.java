package prior.solution.co.th.project.wonderland.service;

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

    public ResponseModel<Void> getPlayerCsv(HttpServletResponse response) {
        ResponseModel<Void> result = new ResponseModel<>();
        log.info("getCsv");

        result.setStatus(200);
        result.setDescription("getCsv");
        try{


        }catch (Exception e){
            log.info("getCsv error {}",e.getMessage());

            result.setStatus(500);
            result.setDescription("getCsv error "+e.getMessage());
        }
        return result;
    }

}
