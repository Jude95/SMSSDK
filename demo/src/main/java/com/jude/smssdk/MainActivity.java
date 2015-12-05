package com.jude.smssdk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LocalAccountManager.AccountChangeListener{

    @Bind(R.id.name)
    TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        LocalAccountManager.getInstance(this).registerAccountChange(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalAccountManager.getInstance(this).unRegisterAccountChange(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.login);
        if (LocalAccountManager.getInstance(this).getCurAccount()==null){
            item.setTitle("登录");
        }else{
            item.setTitle("注销");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.login) {
            if (LocalAccountManager.getInstance(this).getCurAccount()==null){
                startActivity(new Intent(this, LoginActivity.class));
            }else{
                LocalAccountManager.getInstance(this).logout();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccountChange(Account account) {
        invalidateOptionsMenu();
        if (account!=null)
            name.setText(account.getName());
        else
            name.setText("未登录");
    }
}
