package toponymsystem.island.com.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import toponymsystem.island.com.R;
import toponymsystem.island.com.model.RegisterData;
import toponymsystem.island.com.rest.Dto.PostParams;
import toponymsystem.island.com.widget.ClearEditText;

/**
 * Created by CuiXiaoYong
 * on 2016/4/8
 * E-Mail:hellocui@aliyun.com
 */
@EActivity(R.layout.activity_sms)
public class SMSActivity extends BaseActivity {

    // 填写从短信SDK应用后台注册得到的APPKEY
    private static String APPKEY = "116d3ab55cdc4";
    // 填写从短信SDK应用后台注册得到的APPSECRET
    private static String APPSECRET = "fef751f927050cefece620db3b09eb43";
    private EventHandler eh;
    @ViewById(R.id.code_input)
    ClearEditText code;//输入的验证码
    @ViewById(R.id.get_code)
    TextView getCode;//获取验证码
    @ViewById(R.id.title)
    TextView title;//标题
    @ViewById(R.id.send_code_phone)
    TextView sendCodePhone;//显示发送手机号

    @ColorRes(R.color.hint_color)
    int fontColorGray;
    @ColorRes(R.color.theme)
    int fontColorBlue;
    private TimeCount mTimeCount;
    private String mPhoneNumber;//注册手机号
    private String mPassword;//注册密码
    @Bean
    RegisterData data;
    private static final int HAS_REGISTER = -4;//已注册
    private static final int NO_REGISTER = 1;//未注册

    private static final String REGISTER = "register";//注册页面

    @Click({R.id.back, R.id.go_next, R.id.get_code})
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back:
                this.finish();
                break;
            case R.id.get_code:
                SMSSDK.getVerificationCode("86", mPhoneNumber);//请求获取验证码
                mTimeCount.start();// 开始计时
                sendCodePhone.setText("已发送验证码至：" + mPhoneNumber);
                break;
            case R.id.go_next:
                SMSSDK.submitVerificationCode("86", mPhoneNumber, code.getText().toString());
                break;
            default:
                break;
        }
    }

    @AfterViews
    void afterView() {
        title.setText("注册");
        mTimeCount = new TimeCount(60000, 1000);
        mPhoneNumber = getIntent().getStringExtra("phone");//获取手机号
        mPassword = getIntent().getStringExtra("password");//获取密码
        SMSSDK.initSDK(this, APPKEY, APPSECRET);
        eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                mHandler.sendMessage(msg);
            }
        };
        SMSSDK.registerEventHandler(eh);
    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (result == SMSSDK.RESULT_COMPLETE) {
                //短信注册成功后，
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {//提交验证码成功
                    getRegister(mPhoneNumber, mPassword);
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //已经验证
                    showToast("验证码已发送");
                }
            } else {
                try {
                    ((Throwable) data).printStackTrace();
                } catch (Exception e) {
                    SMSLog.getInstance().w(e);
                }
            }
        }
    };

    /**
     * 注册请求
     *
     * @param phone
     * @param password
     */
    private void getRegister(final String phone, String password) {

        showLoading();
        PostParams params = new PostParams();
        params.put("phoneNumber", phone);
        params.put("password", password);
        Call<RegisterData> mCall = mGsonClient.getRegister(params);
        mCall.enqueue(new Callback<RegisterData>() {
            @Override
            public void onResponse(Response<RegisterData> response, Retrofit retrofit) {
                if (null != response) {
                    data = response.body();
                    if (data.getCode() == HAS_REGISTER) {
                        showToast("此用户名已注册！");
                        return;
                    } else if (data.getCode() == NO_REGISTER) {
                        Intent intent = new Intent(SMSActivity.this, HomeActivity_.class);
                        intent.putExtra("register", REGISTER);//代表注册页面
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
        dismissLoading();
    }

    @Override
    public void onDestroy() {
        SMSSDK.unregisterEventHandler(eh);
        super.onDestroy();
    }

    private class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            getCode.setText("获取验证码");
            getCode.setClickable(true);
            getCode.setTextColor(fontColorBlue);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            getCode.setClickable(false);//防止重复点击
            getCode.setText(millisUntilFinished / 1000 + "秒后重发");
            getCode.setTextColor(fontColorGray);
        }
    }
}
