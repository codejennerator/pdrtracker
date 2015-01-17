package rmg.pdrtracker.job.model;

import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.RiItemType;
import rmg.pdrtracker.job.constants.RiItemName;

import java.io.Serializable;

public class RiItemModel implements Serializable {

    private final CarArea carArea;
    private RiItemType riItemType;
    private final RiItemName riItemName;

    private Float laborCost;
    private Float laborHours;
    private Float partCost;
    private boolean isSelected;

    public RiItemModel(CarArea carArea, RiItemType riItemType, RiItemName riItemName, Float partCost, Float laborCost) {
        this.carArea = carArea;
        this.riItemType = riItemType;
        this.riItemName = riItemName;

        this.partCost = partCost;
        if(partCost==null)  {
            this.partCost = 0.0F;
        }

        this.laborCost = laborCost;
        if(laborCost==null)  {
            this.laborCost = 20.0F;
        }

          this.laborHours = laborCost;
          if(laborHours==null)  {
              this.laborHours = 0.0F;
         }
    }

    public CarArea getCarArea() {
        return carArea;
    }

    public RiItemType getRiItemType() {
        return riItemType;
    }

    public RiItemName getRiItemName() {
        return riItemName;
    }

    public Float getPartCost() {
        return partCost;
    }

    public void setPartCost(Float partCost) {
        this.partCost = partCost;
    }

    public Float getLaborCost() {
        return 20.0F;
    }

    public void setLaborCost(Float laborCost) {
        this.laborCost = laborCost;
    }

     public Float getLaborHours() {
         return laborHours;
     }

    public void setLaborHours(Float laborHours) {
         this.laborHours = laborHours;
     }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RiItemModel that = (RiItemModel) o;

        if (carArea != that.carArea) return false;
        if (riItemName != that.riItemName) return false;
        if (riItemType != that.riItemType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = carArea.hashCode();
        result = 31 * result + riItemType.hashCode();
        result = 31 * result + riItemName.hashCode();
        return result;
    }
}
