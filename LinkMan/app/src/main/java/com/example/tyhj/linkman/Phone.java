package com.example.tyhj.linkman;

/**
 * Created by Tyhj on 2016/3/9.
 */
public class Phone {
    String number;
    int image;
    public Phone(String str1,int str2){
        number=str1;
        image=str2;
    }

    public String getNumber() {
        return number;
    }

    public int getImage() {
        return image;
    }
}
