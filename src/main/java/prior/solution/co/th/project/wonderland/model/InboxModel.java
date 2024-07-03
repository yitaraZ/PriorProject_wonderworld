package prior.solution.co.th.project.wonderland.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class InboxModel {
    private int inboxId;
    private int pId;
    private String message;
    private Timestamp date;
}
