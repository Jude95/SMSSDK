package com.jude.smssdk;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.jude.smssdk_mob.Callback;
import com.jude.smssdk_mob.SMSManager;
import com.jude.smssdk_mob.TimeListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Mr.Jude on 2015/12/5.
 */
public class FindPasswordActivity extends AppCompatActivity  implements TimeListener {
    public static final int EXTERNAL_STORAGE_REQ_CODE = 10 ;

    @Bind(R.id.til_number)
    TextInputLayout tilNumber;
    @Bind(R.id.btn_code)
    Button btnCode;
    @Bind(R.id.til_code)
    TextInputLayout tilCode;
    @Bind(R.id.til_password)
    TextInputLayout tilPassword;
    @Bind(R.id.btn_modify)
    Button btnModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        ButterKnife.bind(this);
        SMSManager.getInstance().registerTimeListener(this);
        btnCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modify();
            }
        });
    }

    public void sendCode() {
        if (!requestPermission()){
            return;
        }
        if (tilNumber.getEditText().getText().toString().length() != 11) {
            Toast.makeText(this, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!LocalAccountManager.getInstance(this).exist(tilNumber.getEditText().getText().toString())){
            Toast.makeText(this,"手机号还未注册",Toast.LENGTH_SHORT).show();
            return;
        }
        SMSManager.getInstance().sendMessage(this, "86", tilNumber.getEditText().getText().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSManager.getInstance().unRegisterTimeListener(this);
    }

    public void modify(){
        if (tilPassword.getEditText().getText().toString().length() < 6 || tilPassword.getEditText().getText().toString().length() > 12) {
            Toast.makeText(this,"请输入6-12位密码",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(tilCode.getEditText().getText().toString())){
            Toast.makeText(this,"请输入验证码",Toast.LENGTH_SHORT).show();
            return;
        }

        SMSManager.getInstance().verifyCode(this, "86", tilNumber.getEditText().getText().toString(), tilCode.getEditText().getText().toString(), new Callback() {
            @Override
            public void success() {
                if (LocalAccountManager.getInstance(FindPasswordActivity.this).modifyPassword(FindPasswordActivity.this, tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())) {
                    Intent i = new Intent();
                    i.putExtra("number", tilNumber.getEditText().getText().toString());
                    i.putExtra("password", tilPassword.getEditText().getText().toString());
                    setResult(RESULT_OK, i);
                    Toast.makeText(FindPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(FindPasswordActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void error(Throwable error) {
                Toast.makeText(FindPasswordActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLastTimeNotify(int lastSecond) {
        if (lastSecond > 0)
            btnCode.setText(lastSecond + "秒后重新发送");
        else
            btnCode.setText("发送验证码");
    }

    @Override
    public void onAbleNotify(boolean valuable) {
        btnCode.setEnabled(valuable);
    }

    public boolean requestPermission(){
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED
                ||ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this, "please give me the permission", Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.READ_PHONE_STATE},
                        EXTERNAL_STORAGE_REQ_CODE);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //申请成功，进行相应操作
                    sendCode();
                } else {
                    //申请失败，可以继续向用户解释。
                }
                return;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
