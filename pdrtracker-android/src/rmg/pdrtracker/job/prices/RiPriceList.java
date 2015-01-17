package rmg.pdrtracker.job.prices;

import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.RiItemName;
import rmg.pdrtracker.job.constants.RiItemType;
import rmg.pdrtracker.job.model.RiItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RiPriceList {

    private RiItemModel[] priceList = {

            new RiItemModel(CarArea.HOOD, RiItemType.LABOR, RiItemName.ASSEMBLY, null, 0.0F),
            new RiItemModel(CarArea.HOOD, RiItemType.LABOR, RiItemName.LINER, null, 0.0F),

            new RiItemModel(CarArea.ROOF, RiItemType.LABOR, RiItemName.HEADLINER_WITH_SUNROOF, null, 0.0F),
            new RiItemModel(CarArea.ROOF, RiItemType.LABOR, RiItemName.HEADLINER_WITHOUT_SUNROOF, null, 0.0F),
            new RiItemModel(CarArea.ROOF, RiItemType.LABOR, RiItemName.ROOF_RACK, null, 0.0F),
            new RiItemModel(CarArea.ROOF, RiItemType.LABOR, RiItemName.REAR_HATCH, null, 0.0F),
            new RiItemModel(CarArea.ROOF, RiItemType.PART, RiItemName.LEFT_DRIP_MOLDING, null, null),
            new RiItemModel(CarArea.ROOF, RiItemType.PART, RiItemName.RIGHT_DRIP_MOLDING, null, null),
            new RiItemModel(CarArea.ROOF, RiItemType.PART, RiItemName.DVD, null, null),
            new RiItemModel(CarArea.ROOF, RiItemType.PART, RiItemName.THIRD_BRAKE_LIGHT, null, null),
            new RiItemModel(CarArea.ROOF, RiItemType.PART, RiItemName.WINDSHIELD, null, null),
            new RiItemModel(CarArea.ROOF, RiItemType.PART, RiItemName.CENTER_CONSOLE, null, null),

            new RiItemModel(CarArea.DECK_LID, RiItemType.LABOR, RiItemName.ASSEMBLY, null, 0.0F),
            new RiItemModel(CarArea.DECK_LID, RiItemType.LABOR, RiItemName.LINER, null, 0.0F),

            new RiItemModel(CarArea.L_FENDER, RiItemType.LABOR, RiItemName.ASSEMBLY, null, 0.4F),
            new RiItemModel(CarArea.L_FENDER, RiItemType.LABOR, RiItemName.LINER, null, 0.0F),

            new RiItemModel(CarArea.R_FENDER, RiItemType.LABOR, RiItemName.ASSEMBLY, null, 0.0F),
            new RiItemModel(CarArea.R_FENDER, RiItemType.LABOR, RiItemName.LINER, null, 0.0F),

            new RiItemModel(CarArea.LF_DOOR, RiItemType.LABOR, RiItemName.DOOR_PANEL, null, 0.0F),
            new RiItemModel(CarArea.LF_DOOR, RiItemType.PART, RiItemName.UPPER_MOLDING, null, null),
            new RiItemModel(CarArea.LF_DOOR, RiItemType.PART, RiItemName.LOWER_BELT_MOLDING, null, null),
            new RiItemModel(CarArea.LF_DOOR, RiItemType.PART, RiItemName.APPLIQUE, null, null),

            new RiItemModel(CarArea.RF_DOOR, RiItemType.LABOR, RiItemName.DOOR_PANEL, null, 0.0F),
            new RiItemModel(CarArea.RF_DOOR, RiItemType.PART, RiItemName.UPPER_MOLDING, null, null),
            new RiItemModel(CarArea.RF_DOOR, RiItemType.PART, RiItemName.LOWER_BELT_MOLDING, null, null),
            new RiItemModel(CarArea.RF_DOOR, RiItemType.PART, RiItemName.APPLIQUE, null, null),

            new RiItemModel(CarArea.LR_DOOR, RiItemType.LABOR, RiItemName.DOOR_PANEL, null, 0.0F),
            new RiItemModel(CarArea.LR_DOOR, RiItemType.PART, RiItemName.UPPER_MOLDING, null, null),
            new RiItemModel(CarArea.LR_DOOR, RiItemType.PART, RiItemName.LOWER_BELT_MOLDING, null, null),
            new RiItemModel(CarArea.LR_DOOR, RiItemType.PART, RiItemName.APPLIQUE, null, null),

            new RiItemModel(CarArea.RR_DOOR, RiItemType.LABOR, RiItemName.DOOR_PANEL, null, 0.0F),
            new RiItemModel(CarArea.RR_DOOR, RiItemType.PART, RiItemName.UPPER_MOLDING, null, null),
            new RiItemModel(CarArea.RR_DOOR, RiItemType.PART, RiItemName.LOWER_BELT_MOLDING, null, null),
            new RiItemModel(CarArea.RR_DOOR, RiItemType.PART, RiItemName.APPLIQUE, null, null),

            new RiItemModel(CarArea.L_QUARTER, RiItemType.LABOR, RiItemName.TAILLIGHT, null, 0.0F),
            new RiItemModel(CarArea.L_QUARTER, RiItemType.PART, RiItemName.LINER, null, null),

            new RiItemModel(CarArea.R_QUARTER, RiItemType.LABOR, RiItemName.TAILLIGHT, null, 0.0F),
            new RiItemModel(CarArea.R_QUARTER, RiItemType.LABOR, RiItemName.LINER, null, 0.0F),

            new RiItemModel(CarArea.METAL_SUNROOF, RiItemType.LABOR, RiItemName.SUNROOF_ASSEMBLY, null, 0.0F)

    };

    private Map<CarArea, List<RiItemModel>> riPriceListByCarAreaMap = new HashMap<CarArea, List<RiItemModel>>(priceList.length);

    public RiPriceList() {

        for(RiItemModel price : priceList) {
            CarArea carArea = price.getCarArea();
            List<RiItemModel> carAreaPriceList = riPriceListByCarAreaMap.get(carArea);
            if (carAreaPriceList == null) {
                carAreaPriceList = new ArrayList<RiItemModel>(10);
                riPriceListByCarAreaMap.put(carArea, carAreaPriceList);
            }
            carAreaPriceList.add(price);
        }

    }

    public List<RiItemModel> getRiPriceListForCarArea(CarArea carArea) {
    return riPriceListByCarAreaMap.get(carArea);
    }

}
