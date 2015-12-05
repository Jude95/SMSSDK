package com.jude.smssdk;

import java.io.Serializable;

/**
 * Created by Mr.Jude on 2015/12/5.
 */
public class Account implements Serializable{
    private String number;
    private String password;
    private String name;

    public Account(String number) {
        this.number = number;
    }

    public Account(String number, String password, String name) {
        this.number = number;
        this.password = password;
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Account)
            return number.equals(((Account) o).getNumber());
        else
            return false;
    }

    @Override
    public int hashCode() {
        try {
            return Integer.parseInt(number.substring(number.length()-6,number.length()-1));
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public String toString() {
        return "number:"+number+"   password:"+password+"  name:"+name;
    }
}
