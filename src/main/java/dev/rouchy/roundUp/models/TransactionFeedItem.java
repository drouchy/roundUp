package dev.rouchy.roundUp.models;

public class TransactionFeedItem {
    private String feedItemUid;
    private String categoryUid;
    private Amount amount;
    private TransactionDirection direction;
    private TransactionStatus status;

    public String getFeedItemUid() {
        return feedItemUid;
    }

    public void setFeedItemUid(String feedItemUid) {
        this.feedItemUid = feedItemUid;
    }

    public String getCategoryUid() {
        return categoryUid;
    }

    public void setCategoryUid(String categoryUid) {
        this.categoryUid = categoryUid;
    }

    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public TransactionDirection getDirection() {
        return direction;
    }

    public void setDirection(TransactionDirection direction) {
        this.direction = direction;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
