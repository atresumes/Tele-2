package com.trigma.tiktok.model;

public class FeedBack {
    public String Feedback;
    public String From;
    public String Rating;
    public String _id;

    public String getFeedback() {
        return this.Feedback;
    }

    public void setFeedback(String feedback) {
        this.Feedback = feedback;
    }

    public String getFrom() {
        return this.From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getRating() {
        return this.Rating;
    }

    public void setRating(String rating) {
        this.Rating = rating;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
}
