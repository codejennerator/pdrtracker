package rmg.pdrtracker.job.model;

import java.io.Serializable;

public class JobModel implements Serializable {

    long id = -1;

    long userId = -1;

    JobDetailsModel jobDetailsModel = new JobDetailsModel();

    DentDamageModel dentDamageModel = new DentDamageModel();

    GeneralNotesModel generalNotesModel = new GeneralNotesModel();

    PriceEstimateModel priceEstimateModel = new PriceEstimateModel();

    DentPriceMatrixModel dentPriceMatrixModel = new DentPriceMatrixModel();

    RiModel riModel = new RiModel();

    AddInfoModel addInfoModel = new AddInfoModel();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public JobDetailsModel getJobDetailsModel() {
        return jobDetailsModel;
    }

    public void setJobDetailsModel(JobDetailsModel jobDetailsModel) {
        this.jobDetailsModel = jobDetailsModel;
    }

    public DentDamageModel getDentDamageModel() {
        return dentDamageModel;
    }

    public void setDentDamageModel(DentDamageModel dentDamageModel) {
        this.dentDamageModel = dentDamageModel;
    }

    public RiModel getRiModel() {
        return riModel;
    }

    public void setPriceEstimateModel(PriceEstimateModel priceEstimateModel) {
        this.priceEstimateModel = priceEstimateModel;
    }

    public PriceEstimateModel getPriceEstimateModel() {
        return priceEstimateModel;
    }

    public void setRiModel(RiModel riModel) {
        this.riModel = riModel;
    }

    public GeneralNotesModel getGeneralNotesModel() {
        return generalNotesModel;
    }

    public void setGeneralNotesModel(GeneralNotesModel generalNotesModel) {
        this.generalNotesModel = generalNotesModel;
    }


    public AddInfoModel getAddInfoModel() {
        return addInfoModel;
    }

    public void setAddInfoModel(AddInfoModel addInfoModel) {
        this.addInfoModel = addInfoModel;
    }

    public void setDentPriceMatrixModel(DentPriceMatrixModel dentPriceMatrixModel) {
        this.dentPriceMatrixModel = dentPriceMatrixModel;
    }

    public DentPriceMatrixModel getDentPriceMatrixModel() {
        return dentPriceMatrixModel;
    }

}
