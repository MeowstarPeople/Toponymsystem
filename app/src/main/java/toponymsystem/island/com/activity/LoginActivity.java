package toponymsystem.island.com.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.R;
import toponymsystem.island.com.model.LoginData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.utils.SharePreferenceUtil;
import toponymsystem.island.com.widget.ClearEditText;


/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/3/28
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewById(R.id.title)
    TextView title;
    @ViewById(R.id.right_title)
    TextView rightTitle;
    @ViewById(R.id.phone_number)
    ClearEditText phoneNumber;
    @ViewById(R.id.password_input)
    ClearEditText passWord;

    @Bean
    LoginData data;

    Context context;
    private static final int ERROR = -5;//已注册
    private static final int SUCCESS = 1;//未注册
    private static final String LOGIN = "login";//登录页面

    @AfterViews
    void afterView() {
        title.setText("登录");
        rightTitle.setVisibility(View.VISIBLE);
        rightTitle.setText("注册");
        context = getApplicationContext();
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

    @Click({R.id.back, R.id.login, R.id.right_title, R.id.forget_password})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                startActivity(new Intent(this, HomeActivity_.class));
                finish();
                break;
            case R.id.right_title:
                startActivity(new Intent(this, RegisterActivity_.class));
                break;
            case R.id.login:
                String mPhoneNumber = phoneNumber.getText().toString().trim();
                if (!isPhoneNumber(mPhoneNumber)) {
                    showToast("请输入正确的手机号");
                    return;
                } else {
                }
                String mPassword = passWord.getText().toString().trim();
                if (mPassword.length() < 6) {
                    showToast("密码不能少于6位");
                    return;
                } else {
                }
                getLogin(mPhoneNumber, mPassword);
                break;
            case R.id.forget_password:
//                startActivity(new Intent(this, HomeActivity_.class));
                break;
        }
    }

    /**
     * 注册请求
     *
     * @param phone
     * @param password
     */
    private void getLogin(final String phone, String password) {

        showLoading();
        PostParams params = new PostParams();
        params.put("phoneNumber", phone);
        params.put("password", password);
        Call<LoginData> mCall = mGsonClient.getLogin(params);
        mCall.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Response<LoginData> response, Retrofit retrofit) {
                if (null != response) {
                    data = response.body();
                    if (data.getCode() == ERROR) {
                        showToast("用户名或密码错误");
                        return;
                    } else if (data.getCode() == SUCCESS) {
                        Intent intent = new Intent(LoginActivity.this, HomeActivity_.class);
                        intent.putExtra("login", LOGIN);//代表登录页面
                        startActivity(intent);
                        SharePreferenceUtil.putBoolean(context, "login", true);
                        SharePreferenceUtil.putString(context, "username", phone);
                        showToast("登录成功");
                        finish();
                    }
                }
                dismissLoading();
            }

            @Override
            public void onFailure(Throwable throwable) {
                dismissLoading();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, HomeActivity_.class));
        }
        return super.onKeyDown(keyCode, event);
    }
}