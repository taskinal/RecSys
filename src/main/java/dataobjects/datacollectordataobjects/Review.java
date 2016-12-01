package dataobjects.datacollectordataobjects;

/**
 * Created by alimert on 10.11.2016.
 */
public class Review {

    private String reviewId ;

    private String reviewTitle ;

    private String reviewText ;

    private String reviewDate ;

    private double reviewRate ;

    private String url ;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(String reviewDate) {
        this.reviewDate = reviewDate;
    }

    public double getReviewRate() {
        return reviewRate;
    }

    public void setReviewRate(double rate) {
        this.reviewRate = rate;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
