package rmg.pdrtracker.job.damagematrix.ri;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.constants.RiItemType;
import rmg.pdrtracker.job.model.JobModel;
import rmg.pdrtracker.job.model.ModelService;
import rmg.pdrtracker.job.model.RiItemModel;
import rmg.pdrtracker.job.model.RiModel;
import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;
import rmg.pdrtracker.job.prices.RiPriceList;

import java.util.List;

/**
 * Panel for allowing the input of the R/I values.
 */
public class RiPanel extends RelativeLayout {

    private boolean isVisible;

    private int width;

    private LinearLayout root;

    private TextView carAreaLabel;

    private RiTable riTable;

    private PartReplacementTable partReplacementTable;

    private RiPriceList riPriceList = new RiPriceList();

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
    public RiPanel(Context context) {
        super(context);
    }

    public RiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RiPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        Context context = getContext();

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RiPanel);

        width = a.getDimensionPixelSize(R.styleable.RiPanel_android_layout_width, 0);
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
        root.setPadding(AppUtils.toDip(10), 0, AppUtils.toDip(10), 0);
        scrollView.addView(root, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(getResources().getColor(R.drawable.label_background));

        LinearLayout topHeaderRow = createTopHeaderRow(context);
        createCarAreaLabel(context, topHeaderRow);
        createDoneButton(context, topHeaderRow);

        createRiTable(context);
        createPartReplacementTable(context);

    }

    private void createRiTable(Context context) {
        riTable = new RiTable(context);
        root.addView(riTable, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    private void createPartReplacementTable(Context context) {
        partReplacementTable = new PartReplacementTable(context);
        root.addView(partReplacementTable, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
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
     * Label to display what car area RI is being performed on.
     */
    private void createCarAreaLabel(Context context, LinearLayout horizLayout) {
        carAreaLabel = new TextView(context);
        horizLayout.addView(carAreaLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(50), 1.0F));
        carAreaLabel.setText("R&I");
        carAreaLabel.setTextSize(24);
        carAreaLabel.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        carAreaLabel.setTextColor(getResources().getColor(R.drawable.label_foreground));
        carAreaLabel.setBackgroundColor(getResources().getColor(R.drawable.label_background));
    }

    private Button createDoneButton(Context context, LinearLayout parent) {
        Button button = new Button(context);
        parent.addView(button, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, AppUtils.toDip(50)));
        button.setPadding(0, 0, 0, 0);
        button.offsetTopAndBottom(0);
        button.setText("Done");
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                RiPanel.this.flyOut();
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
        RiModel riModel = jobModel.getRiModel();

        carAreaLabel.setText(carArea.getLabel());

        riTable.reset();
        partReplacementTable.reset();

        List<RiItemModel> riPriceListForCarArea = riPriceList.getRiPriceListForCarArea(carArea);
        if (riPriceListForCarArea != null) {
            for (RiItemModel riItem : riPriceListForCarArea) {
                RiItemType riItemType = riItem.getRiItemType();

                RiItemModel modelRiItem = riModel.getRiItem(riItem);
                if (modelRiItem == null) {
                    modelRiItem = riItem;
                    riModel.addRiItem(modelRiItem);
                }

                if (riItemType == RiItemType.LABOR) {
                    riTable.addItem(modelRiItem);
                } else {
                    partReplacementTable.addItem(modelRiItem);
                }
            }
        }

        if (riTable.getSize() != 0) {
            riTable.setVisibility(VISIBLE);
        } else {
            riTable.setVisibility(INVISIBLE);
        }

        if (partReplacementTable.getSize() != 0) {
            partReplacementTable.setVisibility(VISIBLE);
        } else {
            partReplacementTable.setVisibility(INVISIBLE);
        }

        if (isVisible) {
            animate().translationX(-width);
        } else {
            animate().translationX(0);
        }

        isVisible = !isVisible;

    }

    public void clearRiPanel(CarArea carArea) {

        ModelService modelService = ModelService.getInstance();
        JobModel jobModel = modelService.getJobModel();
        RiModel riModel = jobModel.getRiModel();

        carAreaLabel.setText(carArea.getLabel());

        riTable.reset();

        partReplacementTable.reset();

        List<RiItemModel> riPriceListForCarArea = riPriceList.getRiPriceListForCarArea(carArea);
        if (riPriceListForCarArea != null) {
            for (RiItemModel riItem : riPriceListForCarArea) {
                RiItemType riItemType = riItem.getRiItemType();

                RiItemModel modelRiItem = riModel.getRiItem(riItem);
                if (modelRiItem == null) {
                    modelRiItem = riItem;
                    // jenn riModel.addRiItem(modelRiItem);
                }

                if (riItemType == RiItemType.LABOR) {
                    riTable.clearItem(modelRiItem);
                } else {
                    partReplacementTable.clearItem(modelRiItem);
                }
            }
        }

    }
}
