package org.techtown.myschoolapp1;

public class ApiData {
    private String year;
    private String code;
    private String category;
    private String name;
    private String isExport;
    private String kg;
    private String usd;


    public ApiData(String year, String code, String category, String name, String isExport, String kg, String usd) {
        this.year = year;
        this.code = code;
        this.category = category;
        this.name = name;
        this.isExport = isExport;
        this.kg = kg;
        this.usd = usd;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsExport() {
        return isExport;
    }

    public void setIsExport(String isExport) {
        this.isExport = isExport;
    }

    public String getKg() {
        return kg;
    }

    public void setKg(String kg) {
        this.kg = kg;
    }

    public String getUsd() {
        return usd;
    }

    public void setUsd(String usd) {
        this.usd = usd;
    }
}
