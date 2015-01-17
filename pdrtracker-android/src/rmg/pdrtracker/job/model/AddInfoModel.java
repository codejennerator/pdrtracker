package rmg.pdrtracker.job.model;

import rmg.pdrtracker.job.constants.CarArea;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddInfoModel implements Serializable {

    Map<CarArea, List<AddInfoItemModel>> addInfoItemModelMap = new HashMap<CarArea, List<AddInfoItemModel>>(CarArea.values().length);

    public void addAddInfoItem(AddInfoItemModel addInfoItemModel) {
        CarArea carArea = addInfoItemModel.getCarArea();
        List<AddInfoItemModel> addInfoItemModelList = addInfoItemModelMap.get(carArea);
        if (addInfoItemModelList == null) {
            addInfoItemModelList = new ArrayList<AddInfoItemModel>(10);
            addInfoItemModelMap.put(carArea, addInfoItemModelList);
        }
        addInfoItemModelList.add(addInfoItemModel);
    }


    public AddInfoItemModel getAddInfoItem(AddInfoItemModel addInfoItem) {
        List<AddInfoItemModel> addInfoItemModels = addInfoItemModelMap.get(addInfoItem.getCarArea());

        if (addInfoItemModels == null) {
            return null;
        }

        int itemIndex;
        if ((itemIndex = addInfoItemModels.indexOf(addInfoItem)) == -1) {
            return null;
        }

        return addInfoItemModels.get(itemIndex);
    }
}
