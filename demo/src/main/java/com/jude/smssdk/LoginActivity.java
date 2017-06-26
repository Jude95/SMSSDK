package com.jude.smssdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mr.Jude on 2015/12/5.
 */
public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_LOGIN = 12356;
    @BindView(R.id.tilNumber)
    TextInputLayout tilNumber;
    @BindView(R.id.tilPassword)
    TextInputLayout tilPassword;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.find)
    TextView find;
    @BindView(R.id.register)
    TextView register;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LocalAccountManager.getInstance(LoginActivity.this).check(LoginActivity.this, tilNumber.getEditText().getText().toString(), tilPassword.getEditText().getText().toString())) {
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), REQUEST_LOGIN);
            }
        });
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(LoginActivity.this, FindPasswordActivity.class), REQUEST_LOGIN);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            tilNumber.getEditText().setText(data.getStringExtra("number"));
            tilPassword.getEditText().setText(data.getStringExtra("password"));
        }
    }
}
