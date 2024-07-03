package prior.solution.co.th.project.wonderland.repository;

import prior.solution.co.th.project.wonderland.model.MarketModel;
import java.util.List;
import java.util.Map;

public interface MarketNativeRepository {

    public List<MarketModel> findAllList();

    public int insertList(MarketModel marketModel);

    public int updateList(MarketModel marketModel);

    public void deleteList(MarketModel marketModel);

}
