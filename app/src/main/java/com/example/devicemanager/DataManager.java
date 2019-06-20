package com.example.devicemanager;

import java.util.ArrayList;

public class DataManager {
    ArrayList<String> detail = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<String>();
    ArrayList<String> brand = new ArrayList<String>();
    ArrayList<String> model = new ArrayList<String>();
    ArrayList<String> serialNo = new ArrayList<String>();
    ArrayList<String> owner = new ArrayList<String>();
    ArrayList<String> ownerId = new ArrayList<String>();

    public DataManager() {

        detail.add("23 LED Dell S2340L");
        list.add("Monitor");
        brand.add("Dell");
        model.add("23 LED Dell S2340L");
        serialNo.add("CN0MDCK26418038J0A5T");
        owner.add("วิไลลักษณ์ พันะุ์พฤษ์");
        ownerId.add("10249");

        detail.add("Apple MD212TH/A MACBOOKPRO/25");
        list.add("Laptop");
        brand.add("Apple");
        model.add("MacbookPro MD212TH/A MACBOOKPRO/2.5");
        serialNo.add("SC02L48YHDR53");
        owner.add("ปราโมทย์  พาสนุก");
        ownerId.add("10068");

        detail.add("DELL MONITOR S2340 23 W-IPS FULL HD");
        list.add("Monitor");
        brand.add("Dell");
        model.add("Dell Monitor S2340 23");
        serialNo.add("CN0MDCK2641803BR0WMT");
        owner.add("ขวัญฤทัย  พัฒนภักดี");
        ownerId.add("10220");

        detail.add("DELL MONITOR S2340 23 W-IPS FULL HD");
        list.add("Monitor");
        brand.add("Dell");
        model.add("Dell Monitor S2340 23");
        serialNo.add("CN0MDCK2641803BR0TPT");
        owner.add(" ");
        ownerId.add(" ");


        detail.add("DELL MONITOR S2340 23 W-IPS FULL HD");
        list.add("Monitor");
        brand.add("Dell");
        model.add("Dell Monitor S2340 23");
        serialNo.add("CN0MDCK26418038R12ZT");
        owner.add("ชห้อง oper");
        ownerId.add(" ");

        detail.add("DELL MONITOR S2340 23 W-IPS FULL HD");
        list.add("Monitor");
        brand.add("Dell");
        model.add("Dell Monitor S2340 23");
        serialNo.add("CN0MDCK26418038R12ZT");
        owner.add("สุไรดา อากาซา");
        ownerId.add("10236");


        detail.add("EPSON PRINTER");
        list.add("Printer");
        brand.add("EPSON");
        model.add("TM-T82-303 EPSON Printer E C31CB104303");
        serialNo.add(" ");
        owner.add("ห้อง oper");
        ownerId.add("");

        detail.add("EPSON PRINTER");
        list.add("Printer");
        brand.add("EPSON");
        model.add("TM-T82-303 EPSON Printer E C31CB104303");
        serialNo.add(" ");
        owner.add("ห้อง oper");
        ownerId.add("");

        detail.add("NB DELL");
        list.add("Laptop");
        brand.add("Dell");
        model.add("NB Dell 3542-1.7GHz,4GB,500GB,15.6 inch with intel HD Graphic, Window 10");
        serialNo.add("B18K582");
        owner.add("ห้อง oper");
        ownerId.add(" ");


        detail.add("DELL NB INSPIRON 1 เครื่อง");
        list.add("Laptop");
        brand.add("Dell");
        model.add("NB DELL INSPIRON 3458TH-W561072TH 4GB,14inch");
        serialNo.add("17W9D82");
        owner.add("สุไรดา อากาซา");
        ownerId.add("10236");

    }

    public ArrayList<String> getDetail() {
        return detail;
    }

    public void setDetail(ArrayList<String> detail) {
        this.detail = detail;
    }

    public ArrayList<String> getList() {
        return list;
    }

    public void setList(ArrayList<String> list) {
        this.list = list;
    }

    public ArrayList<String> getModel() {
        return model;
    }

    public void setModel(ArrayList<String> model) {
        this.model = model;
    }

    public ArrayList<String> getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(ArrayList<String> serialNo) {
        this.serialNo = serialNo;
    }

    public ArrayList<String> getBrand() {
        return brand;
    }

    public void setBrand(ArrayList<String> brand) {
        this.brand = brand;
    }

    public ArrayList<String> getOwner() {
        return owner;
    }

    public void setOwner(ArrayList<String> owner) {
        this.owner = owner;
    }


}
