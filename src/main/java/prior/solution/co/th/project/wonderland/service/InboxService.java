package prior.solution.co.th.project.wonderland.service;

import org.springframework.stereotype.Service;
import prior.solution.co.th.project.wonderland.model.InboxModel;
import prior.solution.co.th.project.wonderland.model.ResponseModel;
import prior.solution.co.th.project.wonderland.repository.InboxNativeRepository;

import java.util.List;

@Service
public class InboxService {

    private InboxNativeRepository inboxNativeRepository;

    public InboxService(InboxNativeRepository inboxNativeRepository) {
        this.inboxNativeRepository = inboxNativeRepository;
    }

    public ResponseModel<List<InboxModel>> getAllInboxByNativeSql(){
        ResponseModel<List<InboxModel>> result = new ResponseModel<>();

        result.setStatus(200);
        result.setDescription("find all inboxes");
        try{
            List<InboxModel> transfromedData = inboxNativeRepository.findAllInbox();
            result.setData(transfromedData);
        }catch (Exception e){
            result.setStatus(500);
            result.setDescription(e.getMessage());
        }

        return result;
    }


}
