package rmg.pdrtracker.job.model;

import rmg.pdrtracker.job.prices.DentPriceMatrix;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jennparise
 * Date: 10/26/13
 * Time: 11:37 AM
 * To change this template use File | Settings | File Templates.
 */
public class DentPriceMatrixModel implements Serializable {

    DentPriceMatrix dentPriceMatrix;


    public DentPriceMatrix getDentPriceMatrix(){

        return this.dentPriceMatrix;
    }

    public void setDentPriceMatrix(DentPriceMatrix priceMatrix){
        dentPriceMatrix = priceMatrix;
    }

}
