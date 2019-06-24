package dev.rouchy.roundUp.models;

import java.util.List;

public class TransactionFeed {
    private List<TransactionFeedItem> feedItems;

    public List<TransactionFeedItem> getFeedItems() {
        return feedItems;
    }

    public void setFeedItems(List<TransactionFeedItem> feedItems) {
        this.feedItems = feedItems;
    }
}
