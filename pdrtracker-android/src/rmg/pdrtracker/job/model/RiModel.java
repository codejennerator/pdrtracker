package rmg.pdrtracker.job.model;

import rmg.pdrtracker.job.constants.CarArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiModel implements Serializable {

    Map<CarArea, List<RiItemModel>> riItemModelMap = new HashMap<CarArea, List<RiItemModel>>(CarArea.values().length);

    public void addRiItem(RiItemModel riItemModel) {
        CarArea carArea = riItemModel.getCarArea();
        List<RiItemModel> riItemModelList = riItemModelMap.get(carArea);
        if (riItemModelList == null) {
            riItemModelList = new ArrayList<RiItemModel>(10);
            riItemModelMap.put(carArea, riItemModelList);
        }
        riItemModelList.add(riItemModel);
    }

    public RiItemModel getRiItem(RiItemModel riItem) {
        List<RiItemModel> riItemModels = riItemModelMap.get(riItem.getCarArea());

        if (riItemModels == null) {
            return null;
        }

        int itemIndex;
        if ((itemIndex = riItemModels.indexOf(riItem)) == -1) {
            return null;
        }

        return riItemModels.get(itemIndex);
    }
}
