package rmg.pdrtracker.job.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;
import rmg.pdrtracker.job.constants.*;
import rmg.pdrtracker.job.damagematrix.DentDamageKey;
import rmg.pdrtracker.job.dialogs.PrinterNotFoundDialogFragment;
import rmg.pdrtracker.job.model.*;
import rmg.pdrtracker.job.prices.AddOnSurcharge;
import rmg.pdrtracker.job.prices.DentPriceMatrix;
import rmg.pdrtracker.job.prices.RiPriceList;
import rmg.pdrtracker.login.model.LoginModel;
import rmg.pdrtracker.R;
import rmg.pdrtracker.login.model.LoginModelService;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class PrintJobActivity extends Activity implements PrinterNotFoundDialogFragment.NoticeDialogListener {

    public static String LOGTAG = "PdrTracker";
    // Height of a 8.5x11 page at 72dpi
    private final static int PAGE_HEIGHT_WITH_PADDING = 782;
    private final static int PAGE_WIDTH_WITH_PADDING = 590;
    private static int SPACE_BETWEEN_TABLE_ROWS = 15;
    public static final String ENCODING = "utf-8";


    private JobModel jobModel;
    private LoginModel loginModel;
    private JobDetailsModel jobDetailsModel;
    private DentDamageModel dentDamageModel;
    private PriceEstimateModel priceEstimateModel;
    private RiModel riModel;
    private AddInfoModel addInfoModel;

    private int contentObjNum = 100;
    private int pageObjNum = 1000;
    private HashMap<String, HashSet<String>> objContentsByPage = new HashMap<String, HashSet<String>>();
    private List<String> objPages = new ArrayList<String>();
    private HashSet<String> objContents = null;
    private int currentDamageMatrixTableRowLoc = 0;
    private int currentDamageMatrixTableColumnLoc = 5;
    int[] damageMatrixTableColumnLocations = {10,50,150,450, 500};
    private int multiplier = 1;

    private DentPriceMatrix dentPriceMatrix;
    private Map<CarArea, List<AddInfoItemModel>> addInfoListByCarAreaMap = new HashMap<CarArea, List<AddInfoItemModel>>();
    private RiPriceList riPriceList = new RiPriceList();


    private final static DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");


    private AddOnSurcharge addOnSurcharge = new AddOnSurcharge();

    private TextView jobName;
    private TextView jobEstimator;
    private TextView jobDate;
    private TextView insurance;
    private TextView claimNumber;
    private TextView customerName;
    private TextView customerPhone;
    private TextView vin;
    private TextView year;
    private TextView make;
    private TextView model;
    private TextView color;
    private TextView description;
    private Button btnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ModelService modelService = ModelService.getInstance();
        modelService.init(this, savedInstanceState);
        jobModel = modelService.getJobModel();

        LoginModelService loginModelService = LoginModelService.getInstance();
        loginModel = loginModelService.getLoginModel();

        initModel();

        dentPriceMatrix = jobModel.getDentPriceMatrixModel().getDentPriceMatrix();
        if(dentPriceMatrix == null){
            dentPriceMatrix = new DentPriceMatrix();
        }

        initAddInfo();


       // AppUtils.newInstance(this);
        Point screenSize = getScreenSize();
        Log.d(LOGTAG, "Screen size x: " + screenSize.x + " y: " + screenSize.y);

        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.DISPLAY_SHOW_HOME);
        actionBar.setDisplayShowTitleEnabled(true);
        setContentView(R.layout.print_layout);

        // Importing all assets like buttons, text fields
        initFields();
        btnLogin = (Button) findViewById(R.id.print_button);

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {


                File file;

                //   Uri uri = Uri.fromFile(file);
                //   Intent intent = new Intent ("org.androidprinting.intent.action.PRINT");
                //   intent.setDataAndType( uri, "text/plain" );
                //   getContext().startActivity(intent);
               // final PdfFactory pdfFactory = new PdfFactory();
                //pdfFactory.createWebViewFromHtml(getApplicationContext(), createMockHtml(), new LayoutListener() {
                 //   @Override
                 //   public void layoutComplete(WebView webView) {
                  //      pdfFactory.createPdfFromWebView(webView, "foo.pdf");
                   // }
               // });


                String pages = "";

                String contentString = createDateSection();
                contentString += createCustomerDetailsSection();
                contentString += createVehicleDetailsSection();
                contentString += createJobDescriptionSection();
                contentString += createTableHeaderSection();


                contentString += createTableSection();



                contentString += createPriceEstimateSection();



                pageObjNum++;
                if(!objPages.contains(pageObjNum+" 0 ")){
                    objPages.add(pageObjNum+" 0 ");
                }
                String pageObjString = pageObjNum+" 0 ";
                objContentsByPage.put(pageObjString, objContents);

                for(int i = 0; i < objPages.size(); i++){
                    pages += newPdfPage(objPages.get(i));
                }
                String completePdf = printTopOfPDF()+contentString+pages+endOfPdf();
               // System.out.println(completePdf);
                File file2;
                try {
                    file2 = createFile("foo2.pdf");
                    PrintWriter out = new PrintWriter(new FileWriter(file2));
                    out.print(completePdf);
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    throw new RuntimeException("Could not create file", e);
                }

                try{
                    Uri uri = Uri.fromFile(file2);
                    Intent intent = new Intent ("org.androidprinting.intent.action.PRINT");
                    intent.setDataAndType( uri, "text/plain" );
                    startActivity(intent);
                    goBackToJobList(getApplicationContext(), jobModel);
                }catch (ActivityNotFoundException e){
                    showNoticeDialog();

                }
            }
        });


    }

    private void initFields() {

        jobName = (TextView) findViewById(R.id.job_name);
        jobName.setText(jobDetailsModel.getJobName());

        jobEstimator = (TextView) findViewById(R.id.estimator_name);
        jobEstimator.setText(jobDetailsModel.getJobCreatorName());

        jobDate = (TextView) findViewById(R.id.job_date);
        jobDate.setText(DATE_FORMAT.format(jobDetailsModel.getJobDate()));

        insurance = (TextView) findViewById(R.id.insurance_name);
        insurance.setText(jobDetailsModel.getInsuranceCompanyName());

        claimNumber = (TextView) findViewById(R.id.claim_number);
        claimNumber.setText(jobDetailsModel.getInsuranceClaimNumber());

        customerName = (TextView) findViewById(R.id.customer_name);
        customerName.setText(jobDetailsModel.getCustomerName());

        customerPhone = (TextView) findViewById(R.id.customer_phone);
        customerPhone.setText(jobDetailsModel.getCustomerPhone());

        vin = (TextView) findViewById(R.id.vin);
        vin.setText(jobDetailsModel.getVin());

        year = (TextView) findViewById(R.id.vehicle_year);
        year.setText(jobDetailsModel.getVehicleYear());

        make = (TextView) findViewById(R.id.vehicle_make);
        make.setText(jobDetailsModel.getVehicleMake());

        model = (TextView) findViewById(R.id.vehicle_model);
        model.setText(jobDetailsModel.getVehicleModel());

        color = (TextView) findViewById(R.id.vehicle_color);
        color.setText(jobDetailsModel.getVehicleColor());

        description = (TextView) findViewById(R.id.job_description);
        description.setText(jobDetailsModel.getJobDescription());

    }
    private void initAddInfo(){

        List<AddInfoItemModel> addInfoList = new ArrayList<AddInfoItemModel>();
        for(CarArea carArea : CarArea.values()) {
            for(AddInfo addInfo : AddInfo.values())  {
                addInfoList.add(new AddInfoItemModel(carArea, AddInfoItemType.ADD_INFO_CODES, addInfo, 0, null));
            }
            addInfoList.add(new AddInfoItemModel(carArea, AddInfoItemType.COMMENT, null, 0, ""));
        }
        for(AddInfoItemModel addInfoItemModel : addInfoList) {
            CarArea carArea = addInfoItemModel.getCarArea();
            List<AddInfoItemModel> addInfoList2 = addInfoListByCarAreaMap.get(carArea);
            if (addInfoList2 == null) {
                addInfoList2 = new ArrayList<AddInfoItemModel>(10);
                addInfoListByCarAreaMap.put(carArea, addInfoList2);
            }
            addInfoList2.add(addInfoItemModel);
        }
    }

    Point getScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    private void goBackToJobList(Context context, JobModel jobModel) {

        Intent intent = new Intent(getApplicationContext(), JobListActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("jobModel", jobModel);
        bundle.putSerializable("loginModel", loginModel);
        intent.putExtras(bundle);

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void initModel() {

        jobDetailsModel = jobModel.getJobDetailsModel();
        priceEstimateModel = jobModel.getPriceEstimateModel();
        addInfoModel = jobModel.getAddInfoModel();
        riModel = jobModel.getRiModel();
        dentDamageModel = jobModel.getDentDamageModel();


    }

    public File createFile(String fileName) {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        ModelService modelService = ModelService.getInstance();
        modelService.saveModelState(savedInstanceState, jobModel);
    }
    private String createDateSection(){

        String contentString = createPdfContent("Date", "1 0 0 1 ", 390, PAGE_HEIGHT_WITH_PADDING, null);
        contentString += createPdfContent(new Date(System.currentTimeMillis()).toString(), "1 0 0 1 ", 420, PAGE_HEIGHT_WITH_PADDING, null);
        contentString += createPdfContent("Estimate Id:", "1 0 0 1 ", 390, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS, null);
        contentString += createPdfContent(Long.toString(jobModel.getId()), "1 0 0 1 ", 450, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS, null);

        return contentString;
    }
    private String createCustomerDetailsSection(){

        multiplier += 3;

        String contentString = createPdfContent("Job Name:", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent(jobDetailsModel.getJobName(), "1 0 0 1 ", 100, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Estimator:", "1 0 0 1 ", 250, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent(jobDetailsModel.getJobCreatorName(), "1 0 0 1 ", 350, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Insurance:", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(++multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getInsuranceCompanyName(), "1 0 0 1 ", 100, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent("Claim #:", "1 0 0 1 ", 250, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getInsuranceClaimNumber(), "1 0 0 1 ", 350, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent("Customer Name:", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(++multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getCustomerName(), "1 0 0 1 ", 100, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent("Customer Phone #:", "1 0 0 1 ", 250, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getCustomerPhone(), "1 0 0 1 ", 350, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);

        return contentString;

    }


    private String createVehicleDetailsSection(){

        multiplier += 2;

        String contentString = createPdfContent("VIN:", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent(jobDetailsModel.getVin(), "1 0 0 1 ", 45, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Year:", "1 0 0 1 ", 250, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent(jobDetailsModel.getVehicleYear(), "1 0 0 1 ", 290, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Make:", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(++multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getVehicleMake(), "1 0 0 1 ", 45, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent("Model:", "1 0 0 1 ", 250, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getVehicleModel(), "1 0 0 1 ", 290, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent("Color:", "1 0 0 1 ", 390, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);
        contentString += createPdfContent(jobDetailsModel.getVehicleColor(), "1 0 0 1 ", 425, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier), null);

        return contentString;

    }

    private String createJobDescriptionSection(){

        multiplier += 2;

        String contentString = createPdfContent("Job Description:", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent(jobDetailsModel.getJobDescription(), "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(++multiplier), null);

        return contentString;

    }

    private String createTableHeaderSection(){

        multiplier += 2;

        String contentString = createPdfContent("Line", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Item", "1 0 0 1 ", 10, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier+1), null);
        contentString += createPdfContent("Car Area", "1 0 0 1 ", 50, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Description", "1 0 0 1 ", 150, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Labor", "1 0 0 1 ", 450, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, null);
        contentString += createPdfContent("Hours", "1 0 0 1 ", 450, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier+1), null);
        multiplier += 2;
        String horizontalLine = "2 w\n" +
                "10 "+(PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier)+" m\n" +
                PAGE_WIDTH_WITH_PADDING+" "+ (PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier)+" l S";

        contentString += createPdfContent("Charge", "1 0 0 1 ", 500, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(multiplier-2), horizontalLine);

        return contentString;

    }

    private String createTableSection(){

        currentDamageMatrixTableRowLoc = PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(++multiplier);

        int index = 0;
        String contentString = "";

        for(CarArea carArea :CarArea.values()){
            // if((index % 4) == 0 ){
            //     currentDamageMatrixTableRowLoc =- SPACE_BETWEEN_TABLE_ROWS;
            // }

            for (DamageClassifier damageClassifier : DamageClassifier.values()) {
                for (DentSize dentSize : DentSize.values()) {
                    DentDamageKey nextDamageKey = new DentDamageKey(carArea, damageClassifier, dentSize);
                    if (dentDamageModel.isDamageOn(nextDamageKey)) {
                        String dentSizeString = nextDamageKey.getDentSize().getLabel();
                        String carAreaString = nextDamageKey.getCarArea().getLabel();
                        String damageClassifierString = nextDamageKey.getDamageClassifier().getLabel();
                        String damageSizeString = nextDamageKey.getDentSize().getLabel();
                        String damageNumberString = nextDamageKey.getDamageClassifier().getMin() + " - "+nextDamageKey.getDamageClassifier().getMax();
                        Float damageCost = Float.parseFloat(dentPriceMatrix.getDentPrice(nextDamageKey));
                       // System.out.println(dentSizeString+" "+carAreaString+" "+damageClassifierString);
                        //if((index % 4) == 0 ){
                        //currentDamageMatrixTableRowLoc =- SPACE_BETWEEN_TABLE_ROWS;
                        // }
                        contentString += createDentMatrixTableItem(" " + index + " ");
                        contentString += createDentMatrixTableItem(" " + carAreaString + " ");
                        contentString += createDentMatrixTableItem(" " + damageClassifierString + " ("+damageNumberString+") "+damageSizeString+" Size Dents");
                        contentString += createDentMatrixTableItem("  ");
                        contentString += createDentMatrixTableItem(" " + damageCost + " ");
                        currentDamageMatrixTableRowLoc = currentDamageMatrixTableRowLoc - SPACE_BETWEEN_TABLE_ROWS;
                        index++;
                    }
                }
            }

            for (AddInfoItemModel addInfoItem : addInfoListByCarAreaMap.get(carArea)) {
                //  if((index % 4) == 0 ){
                //     currentDamageMatrixTableRowLoc =- SPACE_BETWEEN_TABLE_ROWS;
                //  }

                String addInfoString = "";
                AddInfoItemModel modelAddInfoItem = addInfoModel.getAddInfoItem(addInfoItem);

                if(modelAddInfoItem != null && modelAddInfoItem.getAddInfo() != null){
                    if(modelAddInfoItem.isSelected()){

                        if(modelAddInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)) {
                            Float overSizeSurcharge = modelAddInfoItem.getQuantity()*addOnSurcharge.getAddOnSurcharge(modelAddInfoItem.getAddInfo());
                            addInfoString = modelAddInfoItem.getAddInfo().getLabel();
                            contentString += createDentMatrixTableItem(" " + index + " ");
                            contentString += createDentMatrixTableItem(" " + carArea.getLabel() + " ");
                            contentString += createDentMatrixTableItem(" " + addInfoString +" ("+modelAddInfoItem.getQuantity()+")" );
                            contentString += createDentMatrixTableItem("  ");
                            contentString += createDentMatrixTableItem(" " + overSizeSurcharge + " ");
                        }
                        else if(!addInfoItem.getAddInfo().equals(AddInfo.OVERSIZED_DENT)) {

                            addInfoString = modelAddInfoItem.getAddInfo().getLabel();
                            contentString += createDentMatrixTableItem(" " + index + " ");
                            contentString += createDentMatrixTableItem(" " + carArea.getLabel() + " ");
                            contentString += createDentMatrixTableItem(" " + addInfoString );
                            contentString += createDentMatrixTableItem("  ");
                            Float surcharge = addOnSurcharge.getAddOnSurcharge(modelAddInfoItem.getAddInfo());
                            contentString += createDentMatrixTableItem(" +" + (surcharge*100) + "% ");
                        }
                        else{
                            contentString += createDentMatrixTableItem("  ");
                        }
                        currentDamageMatrixTableRowLoc = currentDamageMatrixTableRowLoc - SPACE_BETWEEN_TABLE_ROWS;
                        index++;
                    }
                }
            }

            List<RiItemModel> riPriceListForCarArea = riPriceList.getRiPriceListForCarArea(carArea);
            if (riPriceListForCarArea != null) {
                for (RiItemModel riItem : riPriceListForCarArea) {

                    RiItemModel modelRiItem = riModel.getRiItem(riItem);
                    if (modelRiItem != null && modelRiItem.isSelected()) {

                        String laborString = Float.toString(modelRiItem.getLaborHours());
                        if(laborString.equals("0.0")){
                            laborString = " ";
                        }

                        String partCostString = Float.toString(modelRiItem.getPartCost());

                        if(partCostString.equals("0.0")){
                            partCostString = " ";
                        }

                        contentString += createDentMatrixTableItem(" " + index + " ");
                        contentString += createDentMatrixTableItem(" " + carArea.getLabel() + " ");
                        contentString += createDentMatrixTableItem(" " + modelRiItem.getRiItemName().getLabel() );
                        contentString += createDentMatrixTableItem(" " + laborString + " ");
                        contentString += createDentMatrixTableItem(" " + partCostString + " ");
                        currentDamageMatrixTableRowLoc = currentDamageMatrixTableRowLoc - SPACE_BETWEEN_TABLE_ROWS;
                        index++;
                    }

                    if (modelRiItem != null){
                        String partCostString = Float.toString(modelRiItem.getPartCost());

                        if (!partCostString.equals("0.0")) {
                            String laborString = Float.toString(modelRiItem.getLaborHours());
                            if(laborString.equals("0.0")){
                                laborString = " ";
                            }

                            contentString += createDentMatrixTableItem(" " + index + " ");
                            contentString += createDentMatrixTableItem(" " + carArea.getLabel() + " ");
                            contentString += createDentMatrixTableItem(" " + modelRiItem.getRiItemName().getLabel() );
                            contentString += createDentMatrixTableItem(" " + laborString + " ");
                            contentString += createDentMatrixTableItem(" " + partCostString + " (Part Cost) ");
                            currentDamageMatrixTableRowLoc = currentDamageMatrixTableRowLoc - SPACE_BETWEEN_TABLE_ROWS;
                            index++;
                        }
                    }
                }
            }
        }

        multiplier = (PAGE_HEIGHT_WITH_PADDING - currentDamageMatrixTableRowLoc)/15;
        return contentString;
    }

    private String createPriceEstimateSection(){

        multiplier += 2;

        String horizontalLine = "2 w\n" +
                "10 "+(PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier)+" m\n" +
                PAGE_WIDTH_WITH_PADDING+" "+ (PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier)+" l S";

        String contentString = createPdfContent("Cost:", "1 0 0 1 ", 450, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*(++multiplier), null);
        contentString += createPdfContent(Float.toString(priceEstimateModel.getPriceEstimate()), "1 0 0 1 ", 500, PAGE_HEIGHT_WITH_PADDING-SPACE_BETWEEN_TABLE_ROWS*multiplier, horizontalLine);

        return contentString;

    }

    private String printTopOfPDF(){
        String topOfPDF = "%PDF-1.1\n"+
                "1 0 obj\n"+
                "<<\n"+
                "/Type /Font\n"+
                "/Subtype /Type1\n"+
                "/Name /F1\n"+
                "/BaseFont /Helvetica\n"+
                "/Encoding /WinAnsiEncoding\n"+
                ">>\n"+
                "endobj\n"+
                "2 0 obj\n"+
                "<<\n"+
                "/Type /Font\n"+
                "/Subtype /Type1\n"+
                "/Name /F2\n"+
                "/BaseFont /Helvetica-Bold\n"+
                "/Encoding /WinAnsiEncoding\n"+
                ">>\n"+
                "endobj\n"+
                "3 0 obj\n"+
                "[/PDF /Text /ImageB]\n"+
                "endobj\n";

        return topOfPDF;
    }

    private String createPdfContent(String content, String location, int column, int row, String extraContent){
        if(objContents==null){
            objContents = new HashSet<String>();
        }
        boolean needsNewPage = false;
        String pdfContent = "";
        if(row < 11){
            row = PAGE_HEIGHT_WITH_PADDING;
            needsNewPage = true;
        }
        try{

            int lengthOfObj = 42 + content.getBytes(ENCODING).length + location.getBytes(ENCODING).length;

            pdfContent = contentObjNum+" 0 obj \n"+
            "<< /Length "+lengthOfObj+" >>\n"+
            "stream\n"+
            "BT\n"+
            "/F1 11 Tf\n"+
            location+" "+column+" "+row+" Tm ("+content+") Tj\n";
            if(extraContent != null && extraContent.length() > 0){
                pdfContent += extraContent+"\n";
            }
            pdfContent += "ET\n"+
            "endstream\n"+
            "endobj\n";


            if(needsNewPage){
                if(objContents.size() > 0) {
                    pageObjNum++;
                    if(!objPages.contains(pageObjNum+" 0 ")){
                        objPages.add(pageObjNum+" 0 ");
                    }
                    String pageObjString = pageObjNum+" 0 ";
                    objContentsByPage.put(pageObjString, objContents);
                    objContents = new HashSet<String>();
                    objContents.add(contentObjNum+" 0 R");
                    currentDamageMatrixTableRowLoc = row;
                }
                if(objContents.size() == 0) {
                    objContents.add(contentObjNum+" 0 R");
                }


            }else{
                objContents.add(contentObjNum+" 0 R");
            }
            contentObjNum++;
            return pdfContent;
        }catch(UnsupportedEncodingException e){
            Log.e(LOGTAG," Error creating pdf content "+e.getStackTrace());
        } finally {

            return pdfContent;
        }

    }

    private String newPdfPage(String pageNum){

       String pageObjString = pageNum;

       String pdfPage =  pageObjString+"obj \n"+
                "<<\n"+
                "/Type /Page\n"+
                "/Parent 4 0 R\n"+
                "/MediaBox [0 0 600 810]\n"+
                "/Rotate 0\n"+
                "/Resources\n"+
                "<<\n"+
                "/Font <<\n"+
                "/F1 1 0 R /F2 2 0 R >>\n"+
                "/ProcSet 3 0 R\n"+
                ">>\n"+
                "/Contents ["+getPageContents(pageObjString)+"]\n"+
                ">>\n"+
                "endobj\n";
        return pdfPage;
    }
    private String endOfPdf(){

        String endOfPdf =
        "11 0 obj\n"+
        "<<\n"+
        "/Subject (Print Estimate)\n"+
        "/Title (Print Estimate)\n"+
        "/Creator (Program DentApp)\n"+
        "/Author (DentApp)\n"+
        "/CreationDate ("+new Date(System.currentTimeMillis())+")\n"+
        ">>\n"+
        "endobj\n"+
        "4 0 obj\n"+
        "<<\n "+
        "/Type /Pages\n"+
        "/Count "+objPages.size()+"\n"+
        "/Kids ["+getPagesString()+"]\n"+
        ">>\n"+
        "endobj\n"+
        "12 0 obj\n"+
        "<<\n"+
        "/Type /Catalog\n"+
        "/Pages 4 0 R\n"+
        ">>\n"+
        "endobj\n"+
        "xref\n"+
        "0 13\n"+
        "0000000000 65535 f\n"+
        "0000000009 00000 n\n"+
        "0000000116 00000 n\n"+
        "0000000228 00000 n\n"+
        "0000001314 00000 n\n"+
        "0000000264 00000 n\n"+
        "0000000394 00000 n\n"+
        "0000000531 00000 n\n"+
        "0000000702 00000 n\n"+
        "0000000841 00000 n\n"+
        "0000000978 00000 n\n"+
        "0000001150 00000 n\n"+
        "0000001379 00000 n\n"+
        "trailer\n"+
        "<<\n"+
        "/Size 13\n"+
        "/Root 12 0 R\n"+
        "/Info 11 0 R\n"+
        ">>\n"+
        "startxref\n"+
        "1429\n"+
        "%%EOF\n";

        return endOfPdf;
    }
    private String getPagesString(){

           String pagesString = "";

           for(String pageString : objPages){
                pagesString += pageString+"R ";
           }

        return pagesString;
    }

    private String getPageContents(String pageString){

        String contentObjsString = "";
        for(String contentsOfPage : objContentsByPage.get((pageString))){
            contentObjsString += contentsOfPage+" ";
        }

        return contentObjsString;
    }

    private String createDentMatrixTableItem(String content){
        String pdfContent = null;

        if(currentDamageMatrixTableColumnLoc >= damageMatrixTableColumnLocations.length -1){
            currentDamageMatrixTableColumnLoc = 0;
        }else{
            currentDamageMatrixTableColumnLoc++;
        }
        pdfContent = createPdfContent(content, "1 0 0 1 ", damageMatrixTableColumnLocations[currentDamageMatrixTableColumnLoc], currentDamageMatrixTableRowLoc, null);
        return pdfContent;
    }

    public void showNoticeDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new PrinterNotFoundDialogFragment();
        dialog.show(getFragmentManager(), "PrinterNotFoundDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        goBackToJobList(getApplicationContext(), jobModel);
    }
}

