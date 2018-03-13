package org.thoughtcrime.securesms;

import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.Iterator;
import java.util.LinkedList;

public class SearchHandler {

    private LinkedList<MessageRecord> messageRecordList;
    private LinkedList<SearchResult>  searchResultList;
    private int                       searchIndex = -1;
    private int                       positionIndex = 0;
    private String                    searchedTerm = null;

    public SearchHandler() {
        messageRecordList = new LinkedList<MessageRecord>();
        searchResultList = new LinkedList<SearchResult>();
    }

    public void search(String term) {
        positionIndex = 0;
        searchResultList.clear();
        searchedTerm = term;

        //search messageRecordList and push position (which is the index of the list) and    messageRecord into searchResultList
        Iterator<MessageRecord> iterator = messageRecordList.iterator();
        while (iterator.hasNext()) {
            MessageRecord messageRecord = iterator.next();
            if (messageRecord.getBody().getBody().toString().toLowerCase().contains(term)) {
                SearchResult searchResult = new SearchResult(positionIndex, messageRecord);
                searchResultList.add(searchResult);
            }
            positionIndex++;
        }
    }

    //Used to add messageRecords when conversation gets new messages
    public void addMessageRecord(MessageRecord messageRecord) {
        messageRecordList.addFirst(messageRecord);
    }

    public void addSearchedResult(int position, MessageRecord messageRecord) {
        searchResultList.addFirst(new SearchResult(position, messageRecord));
    }

    //Used to delete messageRecords when user deleted message records from conversation
    public void deleteMessageRecord(long messageId) {
        Iterator<MessageRecord> iterator = messageRecordList.iterator();
        
        while (iterator.hasNext()) {
            MessageRecord messageRecord = iterator.next();
            if (messageRecord.getId() == messageId) {
                iterator.remove();
                continue;
            }
        }
    }

    //returns the next position in the searchResultList to scrollTo
    public int getNextResultPosition() {
        if (searchIndex < getResultNumber() - 1) return searchResultList.get(++searchIndex).getPosition();
        return -1;
    }

    //returns the previous position in the searchResultList
    public int getPreviousResultPosition() {
        if (searchIndex > 0) return searchResultList.get(--searchIndex).getPosition();
        return -1;
    }

    public boolean hasResults() {
        return searchResultList.size() > 0;
    }

    public int getResultNumber() {
        return searchResultList.size();
    }

    public boolean hasMessageRecords() {
        return messageRecordList.size() > 0;
    }

    //TODO check if message record is a searchedResult
    public boolean isSearchedMessage(MessageRecord searchedMessageRecord) {
        Iterator<SearchResult> iterator = searchResultList.iterator();
        
        while (iterator.hasNext()) {
            SearchResult messageRecord = iterator.next();
            if (messageRecord.getMessageRecord().getId() == searchedMessageRecord.getId()) {
                return true;
            }
        }

        return false;
    }

    public String getSearchedTerm() {
        return searchedTerm;
    }

    public LinkedList<MessageRecord> getMessageRecordList() {
        return messageRecordList;
    }

    public LinkedList<SearchResult> getSearchResultList() {
        return searchResultList;
    }

    public void setMessageRecordList(LinkedList<MessageRecord> messageRecordList) {
        this.messageRecordList = messageRecordList;
    }

    //not sure if should create seperate class file, needs to be accessed outside

    //currently bind in conversationAdapter doesn't pass the position. implement this if it passes position to get easier search
    public class SearchResult {
        private int position;
        private MessageRecord messageRecord;

        public SearchResult(int position, MessageRecord messageRecord) {
            this.position = position;
            this.messageRecord = messageRecord;
        }

        public int getPosition() {
            return position;
        }

        public MessageRecord getMessageRecord() {
            return messageRecord;
        }
    }
}
