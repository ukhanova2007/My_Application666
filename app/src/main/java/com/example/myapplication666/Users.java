package com.example.myapplication666;

public class Users {
    //Calendar today = Calendar.getInstance();
    public String currentDateAndTime;
    public boolean fl;
    public String name, surname, name3;
    public String name_o, surname_o, name3_o;
    public Users(){}
    public Users(String name, String surname, String name3, String name_o,
                 String surname_o, String name3_o, String currentDateAndTime, boolean fl){
        this.name = name;
        this.surname = surname;
        this.name3 = name3;
        this.name_o = name_o;
        this.surname_o = surname_o;
        this.name3_o = name3_o;
        this.currentDateAndTime = currentDateAndTime;
        this.fl = fl;
    }
    public String getName(){ return name; }
    public String getSurname(){ return surname; }
    public String getName3(){ return name3; }
    public String getName_o(){ return name_o; }
    public String getSurname_o(){ return surname_o; }
    public String getName3_o(){ return name3_o; }
    public String getcurrentDateAndTime(){ return currentDateAndTime; }
    //public Calendar gettoday(){ return today; }
    public boolean getfl(){ return fl; }
}
