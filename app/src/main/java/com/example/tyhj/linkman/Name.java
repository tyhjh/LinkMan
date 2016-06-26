package com.example.tyhj.linkman;

/**
 * Created by Tyhj on 2016/3/8.
 */
public class Name {
    private String name;
    private String number1;
    private String number2;
    private  String number3;
    private String imageUrl;
    private String id;
    private  byte[] bs;
   public Name(String str1,String str2,String str3,String str4,String str5,String str6, byte[] bs){
        name=str1;
        imageUrl=str2;
        number1=str3;
        number2=str4;
        number3=str5;
        id=str6;
       this.bs=bs;
    }
    public String getName(){
        return name;
    }
    public String getImageUrl(){
        return imageUrl;
    }
    public String getNumber1() {
        return number1;
    }
    public String getNumber2() {
        return number2;
    }

    public byte[] getBs() {
        return bs;
    }

    public String getNumber3() {
        return number3;
    }

    public String getId() {
        return id;
    }
}
