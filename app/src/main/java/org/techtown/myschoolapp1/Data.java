package org.techtown.myschoolapp1;

public class Data {
    // 금액 년도 수출입 작물분류 품목
    private String usd;
    private String year;
    private String isExport;
    private String category;
    private String name;

    public Data(String usd, String year, String isExport, String category, String name) {
        this.usd = usd;
        this.year = year;
        this.isExport = isExport;
        this.category = category;
        this.name = name;
    }

    public Data() {

    }

    public String getUsd() {
        return usd;
    }

    public void setUsd(String usd) {
        this.usd = usd;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getIsExport() {
        return isExport;
    }

    public void setIsExport(String isExport) {
        this.isExport = isExport;
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
}
