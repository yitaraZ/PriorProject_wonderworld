package prior.solution.co.th.project.wonderland.controller;

import org.springframework.web.bind.annotation.*;
import prior.solution.co.th.project.wonderland.model.InboxModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.service.InboxService;

import java.util.List;

@RestController
@RequestMapping("api")
public class InboxRestController {

    private InboxService inboxService;

    public InboxRestController(InboxService inboxService) {
        this.inboxService = inboxService;
    }

    @GetMapping("/find/all/inboxes")
    public ResponseModel<List<InboxModel>> getAllInbox(){
        return this.inboxService.getAllInboxByNativeSql();
    }


}
