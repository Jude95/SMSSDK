package com.jude.smssdk;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Mr.Jude on 2015/12/5.
 * demo里的一个本地账号管理器。模拟登录注册等。
 */
public class LocalAccountManager {
    public static final String ACCOUNT_FILE = "ACCOUNT";
    private static LocalAccountManager instance;
    private HashSet<Account> mAccounts;
    private Account mCurAccount;
    //账号变动订阅列表,观察者模式
    private ArrayList<AccountChangeListener> mAccountChangeListener = new ArrayList<>();

    public interface AccountChangeListener{
        void onAccountChange(Account account);
    }

    private LocalAccountManager(Context ctx) {
        mAccounts = read(ctx);
        if (mAccounts == null) mAccounts = new HashSet<>();
    }

    public Account getCurAccount(){
        return mCurAccount;
    }

    public void registerAccountChange(AccountChangeListener listener){
        mAccountChangeListener.add(listener);
        listener.onAccountChange(mCurAccount);
    }

    public void unRegisterAccountChange(AccountChangeListener listener){
        mAccountChangeListener.remove(listener);
    }

    public void logout(){
        onNextAccount(null);
    }

    private void onNextAccount(Account account){
        mCurAccount = account;
        for (AccountChangeListener accountChangeListener : mAccountChangeListener) {
            accountChangeListener.onAccountChange(account);
        }
    }

    public static LocalAccountManager getInstance(Context ctx) {
        if (instance == null){
            instance = new LocalAccountManager(ctx);
        }
        return instance;
    }

    public boolean create(Context ctx,Account account){
        if (mAccounts.add(account)){
            save(ctx, mAccounts);
            return true;
        }
        return false;
    }

    public boolean modifyPassword(Context ctx, String number, String newPassword){
        for (Account account : mAccounts) {
            if(account.getNumber().equals(number)){
                account.setPassword(newPassword);
                save(ctx, mAccounts);
                return true;
            }
        }
        return false;
    }

    public boolean check(String number,String password){
        for (Account account : mAccounts) {
            if(account.getNumber().equals(number)&&account.getPassword().equals(password)){
                onNextAccount(account);
                return true;
            }
        }
        return false;
    }

    public boolean exist(String number){
        return mAccounts.contains(new Account(number));
    }

    private static void save(Context ctx,HashSet<Account> data){
        writeObjectToFile(data,new File(ctx.getFilesDir(),ACCOUNT_FILE));
    }

    private static HashSet<Account> read(Context ctx){
        return (HashSet<Account>) readObjectFromFile(new File(ctx.getFilesDir(),ACCOUNT_FILE));
    }

    public static void writeObjectToFile(Object object, File file) {
        ObjectOutputStream objectOut = null;
        FileOutputStream fileOut = null;
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            fileOut = new FileOutputStream(file,false);
            objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            fileOut.getFD().sync();

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
            if (fileOut != null) {
                try {
                    fileOut.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
        }
    }

    public static Object readObjectFromFile(File file) {
        ObjectInputStream objectIn = null;
        Object object = null;
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(file);
            objectIn = new ObjectInputStream(fileIn);
            object = objectIn.readObject();

        } catch (FileNotFoundException e) {
            // Do nothing
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {
                    // do nowt
                }
            }
            if(fileIn != null){
                try {
                    fileIn.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return object;
    }
}
