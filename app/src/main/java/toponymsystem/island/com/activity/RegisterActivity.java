package toponymsystem.island.com.activity;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toponymsystem.island.com.R;

@EActivity(R.layout.activity_register)
public class RegisterActivity extends BaseActivity {


    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.phone_number_input)
    TextView phoneNumber;


    @AfterViews
    void afterView() {
        title.setText("注册");
    }


    /**
     * 判断是否为手机号码
     *
     * @param phoneNumber
     * @return
     */
    private boolean isPhoneNumber(String phoneNumber) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    @Click({R.id.back, R.id.go_next})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.go_next:
                String mPhoneNumber = phoneNumber.getText().toString().trim();

                if (isPhoneNumber(mPhoneNumber)) {

                    Intent intent = new Intent(RegisterActivity.this, RegisterPasswordActivity_.class);
                    intent.putExtra("phoneNumber", mPhoneNumber);
                    startActivity(intent);

                } else {
                    showToast("请输入正确的手机号！");
                }
                break;
            default:
                break;
        }

    }
}
