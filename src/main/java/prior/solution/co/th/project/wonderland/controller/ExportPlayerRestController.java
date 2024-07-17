package prior.solution.co.th.project.wonderland.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.ExportPlayerService;

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
}
