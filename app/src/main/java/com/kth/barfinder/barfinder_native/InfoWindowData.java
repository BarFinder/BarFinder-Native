package com.kth.barfinder.barfinder_native;

/**
 * Created by armin on 08.02.2018.
 */

public class InfoWindowData {
    private String name;
    private String image;
    private String Institution;
    private String Review;
    private String Price;

    public String getname() { return name; }

    public void setname(String name) { this.name = name; }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstitution() {
        return Institution;
    }

    public void setInstitution(String Institution) {
        this.Institution = Institution;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String Review) {
        this.Review = Review;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }
}

