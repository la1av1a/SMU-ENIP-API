package com.smu.smuenip.infrastructure.util.naver.ocr.dto;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class OcrResponseDto {

    private String version;
    private String requestId;
    private String timestamp;
    private Image[] images;

    public static class Address {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
        public ArrayList<Object> maskingPolys;
    }

    public static class BizNum {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
        public ArrayList<Object> maskingPolys;
    }

    public static class BoundingPoly {
        public ArrayList<Vertex> vertices;
    }

    public static class Code {
        public String text;
        public ArrayList<BoundingPoly> boundingPolys;
    }

    public static class Formatted {
        public String value;
        public String year;
        public String month;
        public String day;
        public String hour;
        public String minute;
        public String second;
    }

    public static class Count {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
    }

    public static class Date {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
        public ArrayList<Object> maskingPolys;
    }

    public static class Image {
        public Receipt receipt;
        public String uid;
        public String name;
        public String inferResult;
        public String message;
        public ValidationResult validationResult;
    }

    public static class Item {
        public Name name;
        public Code code;
        public Count count;
        public Price price;
    }

    public static class Meta {
        public String estimatedLanguage;
    }

    public static class Name {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
        public ArrayList<Object> maskingPolys;
    }

    public static class PaymentInfo {
        public Date date;
        public Time time;
    }

    public static class Price {
        public Price price;
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
    }

    public static class Receipt {
        public Meta meta;
        public Result result;
    }

    public static class Result {
        public StoreInfo storeInfo;
        public PaymentInfo paymentInfo;
        public ArrayList<SubResult> subResults;
        public TotalPrice totalPrice;
    }

    public static class Root {
        public String version;
        public String requestId;
        public long timestamp;
        public ArrayList<Image> images;
    }

    public static class StoreInfo {
        public Name name;
        public SubName subName;
        public BizNum bizNum;
        public ArrayList<Address> addresses;
        public ArrayList<Tel> tel;
    }

    public static class SubName {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
        public ArrayList<Object> maskingPolys;
    }

    public static class SubResult {
        public ArrayList<Item> items;
    }

    public static class Tel {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
    }

    public static class Time {
        public String text;
        public Formatted formatted;
        public ArrayList<BoundingPoly> boundingPolys;
    }

    public static class TotalPrice {
        public Price price;
    }

    public static class ValidationResult {
        public String result;
    }

    public static class Vertex {
        public double x;
        public double y;
    }


}