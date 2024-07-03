package prior.solution.co.th.project.wonderland.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MarketModel {

    private int ListId;
    private int sellerId;
    private int itemId;
    private int qty;
    private double price;
    private String status;
}
