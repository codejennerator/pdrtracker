package rmg.pdrtracker.job.model;

import rmg.pdrtracker.job.constants.AddInfo;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.AddInfoItemType;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: jenn
 * Date: 10/3/13
 * Time: 12:10 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddInfoItemModel  implements Serializable {

    CarArea carArea;

    AddInfo addInfo;

    AddInfoItemType addInfoItemType;

    int quantity;

    String note;

    private boolean isSelected;

    public AddInfoItemModel(CarArea carArea, AddInfoItemType addInfoItemType, AddInfo addInfo, int quantity, String note) {

        this.carArea = carArea;
        this.addInfo = addInfo;
        this.addInfoItemType = addInfoItemType;
        this.quantity = quantity;
        this.note = note;

    }

    public CarArea getCarArea() {
        return carArea;
    }

    public AddInfo getAddInfo() {
        return addInfo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public AddInfoItemType getAddInfoItemType() {
        return addInfoItemType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddInfoItemModel that = (AddInfoItemModel) o;

        if (carArea != that.carArea) return false;
        if (addInfo != that.addInfo) return false;

        return true;
    }
}