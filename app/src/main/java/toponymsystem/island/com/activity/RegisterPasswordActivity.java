package toponymsystem.island.com.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.R;
import toponymsystem.island.com.model.RegisterData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.utils.StringUtil;
import toponymsystem.island.com.widget.ClearEditText;

/**
 * Created by CuiXiaoYong
 * on 2016/4/7
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_register_phonenumber)
public class RegisterPasswordActivity extends BaseActivity {

    @ViewById(R.id.password_input)
    ClearEditText passWord;
    @ViewById(R.id.conformPassword_input)
    ClearEditText conformPassword;
    @ViewById(R.id.tv_phoneNumber)
    TextView phoneNumber;
    @ViewById(R.id.title)
    TextView title;
    private String mPhoneNumber;
    private String mPassword, mConformPassword;

    @AfterViews
    void afterView() {
        mPhoneNumber = getIntent().getStringExtra("phoneNumber");
        phoneNumber.setText(mPhoneNumber);
        title.setText("注册");
    }

    @Click({R.id.back, R.id.go_next})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.go_next:
                mPassword = passWord.getText().toString().trim();
                mConformPassword = conformPassword.getText().toString().trim();
                if (mPassword.length() < 6) {
                    showToast("密码不能少于6位！");
                    return;
                } else {
                }
                if (StringUtil.isEmpty(mPassword)) {
                    showToast("密码不能为空！");
                    return;
                } else {
                }
                if (!mPassword.equals(mConformPassword)) {
                    showToast("密码不一致，请重新输入！");
                } else {
                    Intent intent = new Intent(this, SMSActivity_.class);
                    intent.putExtra("phone", mPhoneNumber);
                    intent.putExtra("password", mPassword);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }


}