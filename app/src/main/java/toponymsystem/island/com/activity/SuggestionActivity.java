package toponymsystem.island.com.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import toponymsystem.island.com.R;

/**
 * Created by CuiXiaoYong E-mial:hellocui@aliyun.com
 * on 2016/4/29
 */
@EActivity(R.layout.activity_suggestion)
public class SuggestionActivity extends BaseActivity {
    @ViewById(R.id.suggestion_text)
    EditText suggestion_text;
    @ViewById(R.id.maximum)
    TextView maximum;
    @ViewById(R.id.title)
    TextView title;
    private int words = 300;
    private CharSequence temp;

    @AfterViews
    void afterViews() {
        title.setText("意见反馈");
        maximum.setText("还可以输入" + words + "字");
        init();
    }

    @Click({R.id.back, R.id.go_next})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.go_next:
                if ("".equals(suggestion_text.getText().toString())) {
                    showToast("请输入反馈内容");
                    return;
                } else {
                    showToast("提交成功，谢谢反馈！");
                    finish();
                }
                break;
        }
    }

    private void init() {

        suggestion_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                maximum.setText("还可以输入" + (words - s.length()) + "字");
            }

            @Override
            public void afterTextChanged(Editable s) {
                int editEnd = suggestion_text.getSelectionEnd();
                int editStart = suggestion_text.getSelectionStart();
                if (temp.length() > words) {
                    s.delete(editStart - 1, editEnd);
                    int tempSelection = editStart;
                    suggestion_text.setText(s);
                    suggestion_text.setSelection(tempSelection - 1);
                }
            }
        });
    }
}
