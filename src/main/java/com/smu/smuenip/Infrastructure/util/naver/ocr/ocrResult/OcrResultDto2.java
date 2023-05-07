package com.smu.smuenip.Infrastructure.util.naver.ocr.ocrResult;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class OcrResultDto2 {

    public Result result;
    public Meta meta;

    public static class Address {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class BizNum {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class CardInfo {
        public Number number;
    }

    public static class ConfirmNum {
        public String text;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Count {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Date {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
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

        public Formatted(String value, String year, String month, String day, String hour, String minute, String second) {
            this.value = value;
            this.year = year;
            this.month = month;
            this.day = day;
            this.hour = hour;
            this.minute = minute;
            this.second = second;
        }
    }

    public static class ImageSize {
        public int width;
        public int height;
    }

    @Getter
    public static class Item {
        public Name name;
        public Count count;
        public PriceInfo priceInfo;
    }

    public static class Meta {
        public ImageSize imageSize;
    }

    public static class Name {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class Number {
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

    public static class Price {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class PriceInfo {
        public Price price;
    }

    @Getter
    public static class Result {
        public StoreInfo storeInfo;
        public PaymentInfo paymentInfo;
        public ArrayList<SubResult> subResults;
        public TotalPrice totalPrice;
    }

    public static class Root {
        public Result result;
        public Meta meta;
    }

    public static class StoreInfo {
        public Name name;
        public SubName subName;
        public BizNum bizNum;
        public ArrayList<Address> address;
    }

    public static class SubName {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    @Getter
    public static class SubResult {
        public ArrayList<Item> items;
    }

    public static class Time {
        public String text;
        public Formatted formatted;
        public ArrayList<ArrayList<ArrayList<Integer>>> boundingBoxes;
    }

    public static class TotalPrice {
        public Price price;
    }


}
