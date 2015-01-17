package rmg.pdrtracker.db;


import android.content.Context;
import rmg.pdrtracker.job.model.JobDetailsModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.login.model.UserModel;
import rmg.pdrtracker.login.model.LoginModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Allows database to be populated with some dummy data.
 */
public class DatabaseInitializer {

    public void initJobList(Context context) {

        resetDb(context);

        List<JobModel> jobModelList = new ArrayList<JobModel>(10);

        JobModel jobModel = new JobModel();
        jobModelList.add(jobModel);

        JobDetailsModel jobDetailsModel = new JobDetailsModel();
        jobModel.setJobDetailsModel(jobDetailsModel);
        jobDetailsModel.setJobName("Donald's Car");
        jobDetailsModel.setJobDate(new Date());
        jobDetailsModel.setJobCreatorName("Bob Gaddy");
        jobDetailsModel.setInsuranceCompanyName("Prudential");
        jobDetailsModel.setInsuranceClaimNumber("1");
        jobDetailsModel.setCustomerName("Donald Thomas");
        jobDetailsModel.setCustomerPhone("(999) 999-9999");
        jobDetailsModel.setVin("111111111");
        jobDetailsModel.setVehicleYear("1995");
        jobDetailsModel.setVehicleMake("Honda");
        jobDetailsModel.setVehicleModel("Prelude");
        jobDetailsModel.setVehicleColor("Yellow");
        jobDetailsModel.setJobDescription("Double door ding");

        jobModel = new JobModel();
        jobModelList.add(jobModel);

        jobDetailsModel = new JobDetailsModel();
        jobModel.setJobDetailsModel(jobDetailsModel);
        jobDetailsModel.setJobName("Bob's Car");
        jobDetailsModel.setJobDate(new Date());
        jobDetailsModel.setJobCreatorName("Donald Thomas");
        jobDetailsModel.setInsuranceCompanyName("All State");
        jobDetailsModel.setInsuranceClaimNumber("2");
        jobDetailsModel.setCustomerName("Bob Gaddy");
        jobDetailsModel.setCustomerPhone("(999) 999-9999");
        jobDetailsModel.setVin("222222222");
        jobDetailsModel.setVehicleYear("1989");
        jobDetailsModel.setVehicleMake("Honda");
        jobDetailsModel.setVehicleModel("Civic");
        jobDetailsModel.setVehicleColor("Gold");
        jobDetailsModel.setJobDescription("Triple door ding");

        for(JobModel nextJobModel : jobModelList) {
            JobDao jobDao = new JobDao(context);
            jobDao.open();
            jobDao.saveJob(nextJobModel);
            jobDao.close();
        }

    }

    public void initLogin(Context context) {

        resetDb(context);

        List<LoginModel> loginModelList = new ArrayList<LoginModel>(10);

        LoginModel loginModel = new LoginModel();
        loginModelList.add(loginModel);

        UserModel userModel = new UserModel();
        loginModel.setUserModel(userModel);
        userModel.setUser("test");
        userModel.setPassword("test");

        for(LoginModel nextLoginModel : loginModelList) {
            LoginDao loginDao = new LoginDao(context);
            loginDao.open();
            loginDao.saveLogin(nextLoginModel);
            loginDao.close();
        }

    }

    private void resetDb(Context context) {
        JobDao jobDao = new JobDao(context);
        jobDao.open();
        jobDao.deleteAll();
        jobDao.close();
    }

}
