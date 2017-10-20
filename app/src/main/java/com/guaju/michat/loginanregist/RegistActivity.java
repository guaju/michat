package com.guaju.michat.loginanregist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.guaju.michat.R;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.StrAccountLogin;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSStrAccRegListener;
import tencent.tls.platform.TLSUserInfo;

import static com.tencent.qalsdk.service.QalService.context;

public class RegistActivity extends AppCompatActivity {

    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_repassword)
    EditText etRepassword;
    @BindView(R.id.bt_regist)
    Button btRegist;
    private TLSService tlsService;
    private String username;
    private String password;
    private String repassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_regist)
    public void onViewClicked() {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        repassword = etRepassword.getText().toString().trim();

        regist(username, password, repassword);
    }

    private void regist(String username, String password, String repassword) {
        if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)||TextUtils.isEmpty(repassword))
        {
            Toast.makeText(this, "输入项目不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
         if (username.length()<=4||username.length()>=32){
             Toast.makeText(this, "用户名不能小于4位不能大于32位", Toast.LENGTH_SHORT).show();
             return;
         }
         if (password.length()<8){
             Toast.makeText(this, "密码需大于等于8位", Toast.LENGTH_SHORT).show();
             return;
         }
         if(!password.equals(repassword)){
             Toast.makeText(this, "两个密码不一致，请重新输入", Toast.LENGTH_SHORT).show();
             return;
         }
          realRegist(username,repassword);
    }

    private void realRegist(String username, String password) {
        tlsService = TLSService.getInstance();
        int result = tlsService.TLSStrAccReg(username, password, new StrAccRegListener() );
        if (result == TLSErrInfo.INPUT_INVALID) {
            Toast.makeText(this, "账号不合法", Toast.LENGTH_SHORT).show();
        }

    }

   //注册监听
   class StrAccRegListener implements TLSStrAccRegListener {
    @Override
    public void OnStrAccRegSuccess(TLSUserInfo userInfo) {
        Toast.makeText(RegistActivity.this, "成功注册 " + userInfo.identifier, Toast.LENGTH_SHORT).show();
        TLSService.getInstance().setLastErrno(0);
        StrAccountLogin login = new StrAccountLogin(context);

        Intent intent = new Intent();
        intent.putExtra("username",username);
        setResult(1,intent);
        finish();
        // login.doStrAccountLogin(username, password);
    }
    //注册失败
    @Override
    public void OnStrAccRegFail(TLSErrInfo errInfo)
    {
        Toast.makeText(RegistActivity.this, "注册失败 " + errInfo.Msg, Toast.LENGTH_SHORT).show();

        Util.notOK(context, errInfo);
    }
    //注册超时
    @Override
    public void OnStrAccRegTimeout(TLSErrInfo errInfo) {
        Toast.makeText(RegistActivity.this, "注册失败 " + errInfo.Msg, Toast.LENGTH_SHORT).show();

        Util.notOK(context, errInfo);
    }
}
}