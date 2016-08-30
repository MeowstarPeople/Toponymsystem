package toponymsystem.island.com.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import toponymsystem.island.com.R;
import toponymsystem.island.com.model.BarChartItemData;
import toponymsystem.island.com.utils.ScreenUtils;

/**
 * 自定义组件：柱状统计图
 */
public class BarChartView extends View {
    private int screenWidth, screenHeight;
    private BarChartItemData[] mItems = new BarChartItemData[]{
            new BarChartItemData("工业", 0),
            new BarChartItemData("旅游", 0),
            new BarChartItemData("交通", 0),
            new BarChartItemData("渔业", 0),
            new BarChartItemData("公共", 0)};

    //max value in mItems.
    private float maxValue = 10.0f;
    //max height of the bar
    private int maxHeight;
    private int[] mBarColors = new int[]{Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.MAGENTA};
    private Paint barPaint, linePaint, textPaint;
    private Rect barRect;

    private int leftMargin, topMargin, smallMargin;
    //the width of one bar item
    private int barItemWidth;
    //the spacing between two bar items.
    private int barSpace;
    //the width of the line.
    private int lineStrokeWidth;

    /**
     * The x-position of y-index and the y-position of the x-index..
     */
    private float x_index_startY = 0, y_index_startX = 50;
    private boolean statusHeightHasGet;

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        screenWidth = ScreenUtils.getScreenW(context);
        screenHeight = ScreenUtils.getScreenH(context);

        leftMargin = ScreenUtils.dip2px(context, 20);
        topMargin = ScreenUtils.dip2px(context, 75);
        smallMargin = ScreenUtils.dip2px(context, 5);

        barPaint = new Paint();

        linePaint = new Paint();
        lineStrokeWidth = ScreenUtils.dip2px(context, 1);
        linePaint.setStrokeWidth(lineStrokeWidth);
        linePaint.setColor(context.getResources().getColor(R.color.theme));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);

        barRect = new Rect(0, 0, 0, 0);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * Sub the height of status bar and action bar to get the accurate height of screen.
         */
        if (!statusHeightHasGet) {
            barRect.top = topMargin * 2;
            barRect.bottom = screenHeight - topMargin * 3;
            maxHeight = barRect.bottom - barRect.top;
            x_index_startY = barRect.bottom;
            statusHeightHasGet = true;
        }


        /**
         * 画X轴
         */
        canvas.drawLine(
                y_index_startX - lineStrokeWidth / 2,
                x_index_startY,
                screenWidth - leftMargin,
                x_index_startY,
                linePaint);
        /**
         * 画Y轴
         */
        canvas.drawLine(
                y_index_startX,
                x_index_startY + lineStrokeWidth / 2,
                y_index_startX,
                topMargin / 3,
                linePaint);
        for (int i = 0; i < mItems.length; i++) {
            /**
             *
             * 画矩形
             */
            barRect.left = (int) y_index_startX + barItemWidth * i + barSpace * (i + 1);
            barRect.top = mItems[i].itemValue == 0 ? barRect.bottom : topMargin * 2 + (int) (maxHeight * (1.0f - mItems[i].itemValue / maxValue));
            barRect.right = barRect.left + barItemWidth;
            barPaint.setColor(mBarColors[i % mBarColors.length]);
            canvas.drawRect(barRect, barPaint);

            /**
             * 画各用途文本
             */
            textPaint.setTextSize(24);
            String typeText = mItems[i].itemType;
            float textPathStartX = barRect.left + barItemWidth / 5;
            float textPathStartY = barRect.bottom + 30.0f;
            canvas.drawText(typeText, textPathStartX, textPathStartY, textPaint);

            /**
             * 画各用途数量值
             */
            String valueText = String.valueOf((int) mItems[i].itemValue);
            canvas.drawText(valueText, barRect.left - (textPaint.measureText(valueText) - barItemWidth) / 2, barRect.top - smallMargin, textPaint);
        }
        canvas.save();
    }

    public BarChartItemData[] getItems() {
        return mItems;
    }


    public void setItems(BarChartItemData[] items) {
        if (items == null) {
            throw new RuntimeException("BarChartView.setItems(): the param items cannot be null.");
        }
        if (items.length == 0) {
            return;
        }

        this.mItems = items;

        //Calculate the max value.
        maxValue = items[0].itemValue;
        for (BarChartItemData bean : items) {
            if (bean.itemValue > maxValue) {
                maxValue = bean.itemValue;
            }
        }

        //Get the width of each bar.
        getBarItemWidth(screenWidth, items.length);
        invalidate();
    }


    /**
     * Get the width of each bar which is depended on the screenWidth and item count.
     */
    private void getBarItemWidth(int screenW, int itemCount) {
        int minBarWidth = ScreenUtils.dip2px(getContext(), 30);
        int minBarSpacing = ScreenUtils.dip2px(getContext(), 20);

        barItemWidth = (screenW - leftMargin * 2) / (itemCount + 3);
        barSpace = (screenW - leftMargin * 2 - barItemWidth * itemCount) / (itemCount + 1);

        if (barItemWidth < minBarWidth || barSpace < minBarSpacing) {
            barItemWidth = minBarWidth;
            barSpace = minBarSpacing;
        }
    }
}
