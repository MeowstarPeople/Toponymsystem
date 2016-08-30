package toponymsystem.island.com.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import toponymsystem.island.com.R;


public class DialogUtil {

    public static Dialog showDialogWithOkCancel(Context context, String title,
                                                String message, View.OnClickListener okListener, String Positive,
                                                View.OnClickListener cancelListener, String Negative) {
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.layout_custom_alert_dialog);
        ((TextView) dialog.getWindow().findViewById(
                R.id.layout_custom_alert_dialog_title)).setText(title);
        ((TextView) dialog.getWindow().findViewById(
                R.id.layout_custom_alert_dialog_message)).setText(message);
        ((TextView) dialog.getWindow().findViewById(
                R.id.layout_alert_message_dialog_left_tv)).setText(Positive);
        ((TextView) dialog.getWindow().findViewById(
                R.id.layout_custom_alert_dialog_right_tv)).setText(Negative);
        dialog.getWindow()
                .findViewById(R.id.layout_alert_message_dialog_left_ll)
                .setOnClickListener(okListener);
        dialog.getWindow()
                .findViewById(R.id.layout_custom_alert_dialog_right_ll)
                .setOnClickListener(cancelListener);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();

        Point point = DensityUtil.getScreenMetrics(context);

        layoutParams.width = point.x - DensityUtil.dip2px(context, 30);
        window.setAttributes(layoutParams);

        return dialog;
    }
}
