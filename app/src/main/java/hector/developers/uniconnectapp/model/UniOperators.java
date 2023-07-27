package hector.developers.uniconnectapp.model;

import java.io.Serializable;

public class UniOperators implements Serializable {
    private String id;
    private String desc;
    private String name;
    private String sector;
    private String amount;
    private String imgUrl;

    public UniOperators() {
    }

    public UniOperators(String id, String desc, String name, String sector, String imgUrl, String amount) {
        this.id = id;
        this.desc = desc;
        this.name = name;
        this.sector = sector;
        this.imgUrl = imgUrl;
        this.amount = amount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
