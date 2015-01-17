package rmg.pdrtracker.job.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jenn
 * Date: 10/8/13
 * Time: 1:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PriceEstimateModel implements Serializable {

    Float priceEstimate;

    public Float getPriceEstimate() {
        if(priceEstimate==null) {
            return 0.0F;
        }
        return priceEstimate;
    }

    public void setPriceEstimate(Float priceEstimate) {

        this.priceEstimate = priceEstimate;
    }
}
