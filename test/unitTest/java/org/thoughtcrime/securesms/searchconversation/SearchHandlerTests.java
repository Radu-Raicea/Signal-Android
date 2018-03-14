package org.thoughtcrime.securesms.searchconversation;

import org.junit.Before;
import org.junit.Test;
import org.thoughtcrime.securesms.SearchConversationMocks;
import org.thoughtcrime.securesms.SearchHandler;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.Iterator;
import java.util.LinkedList;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SearchHandlerTests extends SearchConversationMocks {

    private SearchHandler searchHandler;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        super.messageRecordOne     = mock(MessageRecord.class);
        super.messageRecordTwo     = mock(MessageRecord.class);
        super.messageRecordThree   = mock(MessageRecord.class);
        super.messageRecordFour    = mock(MessageRecord.class);
        super.messageRecordFive    = mock(MessageRecord.class);
        super.messageRecordList    = new LinkedList<>();

        super.setUpMessageRecords();
        super.setUpMessageRecordList();

        searchHandler = new SearchHandler();
        searchHandler.setMessageRecordList(super.messageRecordList);
    }

    @Test
    public void testSearchCompleteWord() {
        searchHandler.search("hello");
        Iterator<SearchHandler.SearchResult> iterator = searchHandler.getSearchResultList().iterator();

        assertEquals(searchHandler.getResultNumber(), 2);
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "Hello World");
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "hello");
    }

    @Test
    public void testSearchIsCaseInsensitive() {
        searchHandler.search("HeLlO");
        Iterator<SearchHandler.SearchResult> iterator = searchHandler.getSearchResultList().iterator();

        assertEquals(searchHandler.getResultNumber(), 2);
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "Hello World");
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "hello");
    }

    @Test
    public void testSearchWithoutResults() {
        searchHandler.search("helllo");
        assertEquals(searchHandler.getResultNumber(), 0);
    }

    @Test
    public void testSearchIncompleteWord() {
        searchHandler.search("bye");
        Iterator<SearchHandler.SearchResult> iterator = searchHandler.getSearchResultList().iterator();

        assertEquals(searchHandler.getResultNumber(), 2);
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "Goodbye World");
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "goodbye");
    }

    @Test
    public void testSearchAfterAddingMessageRecord() {
        searchHandler.addMessageRecord(messageRecordFive);
        searchHandler.search("hello");
        Iterator<SearchHandler.SearchResult> iterator = searchHandler.getSearchResultList().iterator();

        assertEquals(searchHandler.getResultNumber(), 3);
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "hello again");
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "Hello World");
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "hello");
    }

    @Test
    public void testSearchAfterDeletingMessageRecord() {
        searchHandler.deleteMessageRecord(3);
        searchHandler.search("hello");
        Iterator<SearchHandler.SearchResult> iterator = searchHandler.getSearchResultList().iterator();

        assertEquals(searchHandler.getResultNumber(), 1);
        assertEquals(iterator.next().getMessageRecord().getBody().getBody(), "Hello World");
    }

}
