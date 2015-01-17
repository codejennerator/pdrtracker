package rmg.pdrtracker.job.damagematrix;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import rmg.pdrtracker.job.constants.CarArea;
import rmg.pdrtracker.job.damagematrix.additionalinfo.AddInfoPanel;
import rmg.pdrtracker.job.damagematrix.ri.RiPanel;
import rmg.pdrtracker.util.AppUtils;
import rmg.pdrtracker.R;

public class CarAreaLabel extends LinearLayout {

    int CAR_AREA_TEXT_SIZE = getResources().getInteger(R.integer.car_area_text_size);

    private final Context context;

    private final CarArea carArea;

    private final ViewGroup root;

    private final TextView carAreaLabel;

    private RiPanel riPanel;

    private Button riButton;

    private AddInfoPanel addInfoPanel;

    private Button addInfoButton;

    private boolean isOnRi;

    private boolean isOnAddInfo;

    public CarAreaLabel(Context context, CarArea carArea, ViewGroup root, RiPanel riPanel, AddInfoPanel addInfoPanel) {
        super(context);

        this.context = context;
        this.carArea = carArea;
        this.root = root;
        this.riPanel = riPanel;
        this.addInfoPanel = addInfoPanel;

        setOrientation(LinearLayout.HORIZONTAL);
        setPadding(0, 0, 0, 0);
        setBackgroundColor(getResources().getColor(R.drawable.car_area_label_background_off));

        carAreaLabel = new TextView(context);
        addView(carAreaLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        carAreaLabel.setText(carArea.getLabel());
        carAreaLabel.setTextSize(CAR_AREA_TEXT_SIZE);
        carAreaLabel.setGravity(Gravity.LEFT);
        carAreaLabel.setTextColor(getResources().getColor(R.drawable.car_area_label_foreground_off));
        carAreaLabel.setMinWidth(AppUtils.toDip(123));

    }

    private Button createRiButton(Context context) {
        Button button = new Button(context);
        button.setTextSize(10);
        button.setWidth(AppUtils.toDip(40));
        button.setMinimumWidth(AppUtils.toDip(40));
        button.setHeight(AppUtils.toDip(40));
        button.setMinimumHeight(AppUtils.toDip(40));
        button.setText("RI");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                riPanel.toggleFlyIn(carArea);
                riPanel.bringToFront();
                riPanel.invalidate();
                root.invalidate();
            }
        });

        return button;
    }

    private Button createAddInfoButton(Context context) {
        Button button = new Button(context);
        button.setTextSize(10);
        button.setWidth(AppUtils.toDip(45));
        button.setMinimumWidth(AppUtils.toDip(45));
        button.setHeight(AppUtils.toDip(40));
        button.setMinimumHeight(AppUtils.toDip(40));
        button.setText("Add");

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                addInfoPanel.toggleFlyIn(carArea);
                addInfoPanel.bringToFront();
                addInfoPanel.invalidate();
                root.invalidate();
            }
        });

        return button;
    }

    public void turnOnRi() {

        if (isOnRi) {
            return;
        }

        carAreaLabel.setTextColor(getResources().getColor(R.drawable.car_area_label_foreground_on));
        setBackgroundColor(getResources().getColor(R.drawable.car_area_label_background_on));

        if (riButton == null) {
            riButton = createRiButton(context);
        }

        addView(riButton, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        isOnRi = true;

    }
    public void turnOnAddInfo() {

        if (isOnAddInfo) {
            return;
        }

        carAreaLabel.setTextColor(getResources().getColor(R.drawable.car_area_label_foreground_on));
        setBackgroundColor(getResources().getColor(R.drawable.car_area_label_background_on));

        if (addInfoButton == null) {
            addInfoButton = createAddInfoButton(context);
        }

        addView(addInfoButton, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));

        isOnAddInfo = true;

    }

    public void turnOffRI() {

        if (!isOnRi) {
            return;
        }

        carAreaLabel.setTextColor(getResources().getColor(R.drawable.car_area_label_foreground_off));
        setBackgroundColor(getResources().getColor(R.drawable.car_area_label_background_off));

        if (riButton == null) {
            return;
        }

        riPanel.clearRiPanel(carArea);

        removeView(riButton);

        isOnRi = false;
    }
    public void turnOffAddInfo() {

        if (!isOnAddInfo) {
            return;
        }

        carAreaLabel.setTextColor(getResources().getColor(R.drawable.car_area_label_foreground_off));
        setBackgroundColor(getResources().getColor(R.drawable.car_area_label_background_off));

        if (addInfoButton == null) {
            return;
        }

        addInfoPanel.clearAddInfoPanel(carArea);

        removeView(addInfoButton);

        isOnAddInfo = false;
    }

    public Button getRiButton() {
        return riButton;
    }
    public Button getAddInfoButton() {
        return addInfoButton;
    }
}