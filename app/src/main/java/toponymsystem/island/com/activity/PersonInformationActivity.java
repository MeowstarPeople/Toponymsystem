package toponymsystem.island.com.activity;

import android.app.Dialog;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;
import toponymsystem.island.com.utils.DialogUtil;
import toponymsystem.island.com.utils.SharePreferenceUtil;

/**
 * Created by CuiXiaoYong
 * on 2016/4/9
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_personinformation)
public class PersonInformationActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;

    @AfterViews
    void afterView() {
        title.setText("个人信息");
    }

    private Dialog mDialog;

    @Click({R.id.back, R.id.go_next})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(this, HomeActivity_.class));
                this.finish();
                break;
            case R.id.go_next:
                mDialog = DialogUtil.showDialogWithOkCancel(this, "提示",
                        "确定要退出登录吗？", new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                setResult(RESULT_CANCELED);
                                SharePreferenceUtil.putBoolean(getApplicationContext(),
                                        "login", false);

                                SharePreferenceUtil.putString(getApplicationContext(),
                                        "username", "");

                                Intent intent = new Intent(PersonInformationActivity.this, HomeActivity_.class);
                                startActivity(intent);
                                finish();
                                if (mDialog != null) {
                                    mDialog.dismiss();
                                    mDialog = null;
                                }
                            }
                        }, "退出登录", new View.OnClickListener() {
                            @Override
                            public void onClick(View arg0) {
                                if (mDialog != null) {
                                    mDialog.dismiss();
                                    mDialog = null;
                                }
                            }
                        }, "取消");
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, HomeActivity_.class));
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
