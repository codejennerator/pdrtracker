package rmg.pdrtracker.job.damagematrix.additionalinfo;



import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.model.AddInfoItemModel;
import rmg.pdrtracker.job.model.AddInfoModel;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.constants.AddInfo;
import rmg.pdrtracker.job.constants.AddInfoItemType;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static rmg.pdrtracker.util.AppUtils.toDip;

/**
 * Panel for allowing the input of the Additional Info values.
 */
public class AddInfoPanel extends RelativeLayout {

    private boolean isVisible;

    private int width;

    private LinearLayout root;

    private TextView carAreaLabel;

    private AddInfoTable addInfoTable;

    private AddInfoItemModel addInfoItemModel;

    private Map<CarArea, List<AddInfoItemModel>> addInfoListByCarAreaMap = new HashMap<CarArea, List<AddInfoItemModel>>();



    /**
     * Preferred to animate a view View Property Animator
     * view.animate().setDuration(2000);
     * view.animate().translationX(-100);
     * view.animate().translationY(-100);
     * <p/>
     * Another way to animate if View Property Animator is not an option:
     * PropertyValuesHolder pvhx = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, -100F);
     * PropertyValuesHolder pvhy = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -100F);
     * ObjectAnimator.ofPropertyValuesHolder(view, pvhx, pvhy).start();
     */
    public AddInfoPanel(Context context) {
        super(context);
    }

    public AddInfoPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public AddInfoPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

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

        Context context = getContext();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.AddInfoPanel);

        width = a.getDimensionPixelSize(R.styleable.AddInfoPanel_android_layout_width, 0);
        a.recycle();

        setY(0);
        setX(-width);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prevents click bleed through
            }
        });

        ScrollView scrollView = new ScrollView(context);
        addView(scrollView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        scrollView.setBackgroundColor(getResources().getColor(R.drawable.panel_background));

        root = new LinearLayout(context);
        scrollView.addView(root, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(getResources().getColor(R.drawable.label_background));

        LinearLayout topHeaderRow = createTopHeaderRow(context);
        createCarAreaLabel(context, topHeaderRow);
        createDoneButton(context, topHeaderRow);

        createAddInfoTable(context);

    }

    private void createAddInfoTable(Context context) {
        addInfoTable = new AddInfoTable(context);
        root.addView(addInfoTable, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    /**
     * Header row at the top of the RI panel that contains the the car area label and the done button.
     */
    private LinearLayout createTopHeaderRow(Context context) {
        LinearLayout horizLayout = new LinearLayout(context);
        root.addView(horizLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        horizLayout.setOrientation(LinearLayout.HORIZONTAL);
        horizLayout.setPadding(0, 0, 0, 0);
        return horizLayout;
    }



    /**
     * Label to display what car area Additional info is being performed on.
     */
    private void createCarAreaLabel(Context context, LinearLayout horizLayout) {

        carAreaLabel = new TextView(context);
        horizLayout.addView(carAreaLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, toDip(50), 1.0F));
        carAreaLabel.setText("Additional Info");
        carAreaLabel.setTextSize(24);
        carAreaLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        carAreaLabel.setTextColor(getResources().getColor(R.drawable.label_foreground));
        carAreaLabel.setBackgroundColor(getResources().getColor(R.drawable.label_background));
    }

    private Button createDoneButton(Context context, LinearLayout parent) {
        Button button = new Button(context);
        parent.addView(button, new LinearLayout.LayoutParams(toDip(150), toDip(40)));
        button.setPadding(0, 0, 0, 0);
        carAreaLabel.setTextSize(24);
        button.offsetTopAndBottom(0);
        button.setText("Done");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AddInfoPanel.this.flyOut();
            }
        });
        return button;
    }

    public void flyOut() {
        animate().translationX(-width);
        isVisible = false;
    }

    public void toggleFlyIn(CarArea carArea) {

        ModelService modelService = ModelService.getInstance();
        JobModel jobModel = modelService.getJobModel();
        AddInfoModel addInfoModel = jobModel.getAddInfoModel();

        carAreaLabel.setText(carArea.getLabel());

        addInfoTable.reset();

        if (addInfoListByCarAreaMap != null) {
            for (AddInfoItemModel addInfoItem : addInfoListByCarAreaMap.get(carArea)) {
                AddInfoItemType addInfoItemType = addInfoItem.getAddInfoItemType();
                AddInfoItemModel modelAddInfoItem = addInfoModel.getAddInfoItem(addInfoItem);
                if (modelAddInfoItem == null) {
                    modelAddInfoItem = addInfoItem;
                    addInfoModel.addAddInfoItem(modelAddInfoItem);
                }

                if (addInfoItemType == AddInfoItemType.ADD_INFO_CODES) {
                    addInfoTable.addItem(modelAddInfoItem);
                }
                if (addInfoItemType == AddInfoItemType.COMMENT) {
                    addInfoTable.addComment(modelAddInfoItem);
                }

            }

        }

        if (addInfoTable.getSize() != 0) {
            addInfoTable.setVisibility(VISIBLE);
        } else {
            addInfoTable.setVisibility(INVISIBLE);
        }

        if (isVisible) {
            animate().translationX(-width);
        } else {
            animate().translationX(0);
        }

        isVisible = !isVisible;

    }

    public void clearAddInfoPanel(CarArea carArea) {

        ModelService modelService = ModelService.getInstance();
        JobModel jobModel = modelService.getJobModel();
        AddInfoModel addInfoModel = jobModel.getAddInfoModel();

        int numOverSizedDents = 0;

        carAreaLabel.setText(carArea.getLabel());

        //addInfoTable.reset();
        addInfoTable.resetFromClear();

        if (addInfoListByCarAreaMap != null) {

            for (AddInfoItemModel addInfoItem : addInfoListByCarAreaMap.get(carArea)) {
                System.out.println("car area clear "+carArea.getLabel());
                AddInfoItemModel modelAddInfoItemInit = addInfoModel.getAddInfoItem(addInfoItem);
                if (modelAddInfoItemInit == null) {
                    modelAddInfoItemInit = addInfoItem;
                }

                if(modelAddInfoItemInit.getQuantity() > 0){
                    numOverSizedDents = modelAddInfoItemInit.getQuantity();

                }
            }

            for (AddInfoItemModel addInfoItem : addInfoListByCarAreaMap.get(carArea)) {
                System.out.println("car area clear "+carArea.getLabel());
                AddInfoItemModel modelAddInfoItem = addInfoModel.getAddInfoItem(addInfoItem);
                if (modelAddInfoItem == null) {
                    modelAddInfoItem = addInfoItem;
                   // jenn addInfoModel.removeAddInfoItem(modelAddInfoItem);
                }

                addInfoTable.clearItem(modelAddInfoItem, numOverSizedDents);

            }

        }

    }
}

