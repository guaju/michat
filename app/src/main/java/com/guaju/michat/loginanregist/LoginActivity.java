package com.guaju.michat.loginanregist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.guaju.michat.R;
import com.tencent.qcloud.tlslibrary.activity.ImgCodeActivity;
import com.tencent.qcloud.tlslibrary.helper.Util;
import com.tencent.qcloud.tlslibrary.service.Constants;
import com.tencent.qcloud.tlslibrary.service.TLSService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tencent.tls.platform.TLSErrInfo;
import tencent.tls.platform.TLSPwdLoginListener;
import tencent.tls.platform.TLSUserInfo;

import static com.tencent.qalsdk.service.QalService.context;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.bt_login)
    Button btLogin;
    @BindView(R.id.tv_regist)
    TextView tvRegist;
    @BindView(R.id.tv_cellphone_login)
    TextView tvCellphoneLogin;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.bt_login, R.id.tv_regist, R.id.tv_cellphone_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_login:
                login();
                break;
            case R.id.tv_regist:
                regist();

                break;
            case R.id.tv_cellphone_login:
                break;
        }
    }

    private void regist() {
        Intent intent = new Intent(LoginActivity.this, RegistActivity.class);
       startActivityForResult(intent,0);
    }

    private void login() {
        username = etUsername.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "用户名密码不能为空~", Toast.LENGTH_SHORT).show();
            return;
        }
        if (username.length()<4||username.length()>32){
            Toast.makeText(this, "用户名需在4位到32位之间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.length()<8){
            Toast.makeText(this, "密码需大于等于8位", Toast.LENGTH_SHORT).show();
        }
        //登录
        realLogin();


    }

    private void realLogin() {
        //初始化TLSService
        TLSService tlsService = TLSService.getInstance();
        //调用自带注册方法 参数3是登录的监听
        tlsService.TLSPwdLogin(username, password, new PwdLoginListener());
    }

    class PwdLoginListener implements TLSPwdLoginListener {
        @Override
        public void OnPwdLoginSuccess(TLSUserInfo userInfo) {
            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
            TLSService.getInstance().setLastErrno(0);
            //跳转
//            jumpToSuccActivity();
        }
       //验证码校验成功的回调
        @Override
        public void OnPwdLoginReaskImgcodeSuccess(byte[] picData) {
            ImgCodeActivity.fillImageview(picData);
        }
        //获取验证码
        @Override
        public void OnPwdLoginNeedImgcode(byte[] picData, TLSErrInfo errInfo) {
            Intent intent = new Intent(context, ImgCodeActivity.class);
            intent.putExtra(Constants.EXTRA_IMG_CHECKCODE, picData);
            intent.putExtra(Constants.EXTRA_LOGIN_WAY, Constants.USRPWD_LOGIN);
            context.startActivity(intent);
        }
        //登录失败
        @Override
        public void OnPwdLoginFail(TLSErrInfo errInfo) {
            TLSService.getInstance().setLastErrno(-1);
            Toast.makeText(LoginActivity.this, "登录失败"+errInfo.Msg, Toast.LENGTH_SHORT).show();

            Util.notOK(context, errInfo);
        }
        //登录超时
        @Override
        public void OnPwdLoginTimeout(TLSErrInfo errInfo) {
            TLSService.getInstance().setLastErrno(-1);
            Toast.makeText(LoginActivity.this, "登录超时"+errInfo.Msg, Toast.LENGTH_SHORT).show();

            Util.notOK(context, errInfo);
        }
    }
    //跳转到注册页面的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==0){
           if (resultCode==1){
               String username = data.getStringExtra("username");
               etUsername.setText(username);
           }
        }
    }
}
