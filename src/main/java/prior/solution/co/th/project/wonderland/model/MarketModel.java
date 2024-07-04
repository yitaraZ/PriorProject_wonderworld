package prior.solution.co.th.project.wonderland.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MarketModel {

    private int listId;
    private int pItemId;
    private int qty;
    private double price;
    private String status;
}
