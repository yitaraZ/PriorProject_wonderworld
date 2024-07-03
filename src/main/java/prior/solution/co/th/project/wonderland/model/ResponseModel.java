package prior.solution.co.th.project.wonderland.model;

import lombok.Data;

@Data
public class ResponseModel<T> {

    private int status;
    private String description;
    private T data;
}
