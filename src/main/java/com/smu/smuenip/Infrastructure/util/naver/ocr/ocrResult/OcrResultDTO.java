package com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public class OcrResultDTO {

    public Result result;
    public Meta meta;

    public static class UnitPrice {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class TotalPrice {

        public Price price;
    }

    public static class Time {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Tel {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    @Getter
    public static class SubResult {

        public ArrayList<Item> items;
    }

    public static class SubName {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class StoreInfo {

        public Name name;
        public SubName subName;
        public BizNum bizNum;
        public ArrayList<Address> address;
        public ArrayList<Tel> tel;
    }

    @Getter
    public static class Result {

        public StoreInfo storeInfo;
        public PaymentInfo paymentInfo;

        public ArrayList<SubResult> subResults;
        public TotalPrice totalPrice;
    }

    public static class PriceInfo {

        public Price price;
        public UnitPrice unitPrice;
    }

    public static class Price {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class PaymentInfo {

        public Date date;
        public Time time;
        public CardInfo cardInfo;
        public ConfirmNum confirmNum;
    }

    public static class Number {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Name {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Meta {

    }

    @Getter
    public static class Item {

        public Name name;
        public Count count;
        public PriceInfo priceInfo;
    }

    @Getter
    public static class Formatted {

        public String value;
        public String year;
        public String month;
        public String day;
        public String hour;
        public String minute;
        public String second;
    }

    public static class Date {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Count {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class ConfirmNum {

        public String text;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Company {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class BizNum {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Address {

        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class CardInfo {

        public Company company;
        public Number number;
    }
}
