package rmg.pdrtracker.job.prices;

import rmg.pdrtracker.job.constants.AddInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Matrix indicating how much each type of dent costs to fix.
 */
public class AddOnSurcharge {

    private Map<AddInfo, Float> addOnSurcharge = new HashMap<AddInfo, Float>(AddInfo.values().length);

    public AddOnSurcharge(){

        for(AddInfo addInfo : AddInfo.values()){
            if(addInfo.equals(AddInfo.ALUMINUM_PANEL)){

                addOnSurcharge.put(addInfo, .25F);
            }
        }
        for(AddInfo addInfo : AddInfo.values()){
            if(addInfo.equals(AddInfo.OVERSIZED_DENT)){

                addOnSurcharge.put(addInfo, 40.0F);
            }
        }
        for(AddInfo addInfo : AddInfo.values()){
            if(addInfo.equals(AddInfo.DOUBLE_PANEL)){

                addOnSurcharge.put(addInfo, .25F);
            }
        }
        for(AddInfo addInfo : AddInfo.values()){
            if(addInfo.equals(AddInfo.SHARP_DENT)){

                addOnSurcharge.put(addInfo, .25F);
            }
        }
    }


   public Float getAddOnSurcharge(AddInfo addInfo){

        return addOnSurcharge.get(addInfo);
   }

}
