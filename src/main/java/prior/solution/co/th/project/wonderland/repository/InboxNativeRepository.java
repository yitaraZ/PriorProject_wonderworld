package prior.solution.co.th.project.wonderland.repository;

import prior.solution.co.th.project.wonderland.model.InboxModel;
import java.util.List;

public interface InboxNativeRepository {

    public List<InboxModel> findAllInbox();

    public int insertInbox(InboxModel inboxModels);

    public int updateInbox(InboxModel inboxModel);

    public void deleteInbox(InboxModel inboxModel);
}
