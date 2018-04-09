package org.thoughtcrime.securesms;

import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessageReplyDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.ArrayList;
import java.util.List;

public class RepliesHandler {
    private MessageReplyDatabase messageReplyDatabase;

    public RepliesHandler(Context context) {
        this.messageReplyDatabase = DatabaseFactory.getMessageReplyDatabase(context);
    }

    /**
     * This method only used by the conversation fragment since it will automatically
     * set the id of the replier to the sender's phonenumber.
     * @param record
     * @param reply
     * @param time
     */
    public void replyToMessageBySender(MessageRecord record, String reply, Long time) {
        this.messageReplyDatabase.replyToMessage(record, reply, time);
    }

    /**
     * This method is ONLY used by JobReceivers since when a reply is received, there
     * is no active MessageRecord object, therefore, all the required parameters are found
     * in the data message.
     *
     * @param messageHash
     * @param reply
     * @param replyTime
     * @param author
     * @param threadId
     */
    public void receiveReply(String messageHash, String reply, Long replyTime, String author, Long threadId) {
        this.messageReplyDatabase.replyToMessage(messageHash, reply, replyTime, author, threadId);
    }

    public boolean removeReply(MessageRecord record) {
        return this.messageReplyDatabase.removeReply(record) > 0;
    }

    public List<Reply> getMessageReplies(MessageRecord record) {
        List<Reply> results = new ArrayList<>();
        Cursor      cursor  = this.messageReplyDatabase.getMessageReplies(record);

        if (cursor == null) return results;

        while (cursor.moveToNext()) {
            Parcel c = Parcel.obtain();
            c.writeString(cursor.getString(cursor.getColumnIndexOrThrow(MessageReplyDatabase.REPLIER_ID)));
            c.setDataPosition(0);
            Address replierAddress = new Address(c);

            Long   replyDate = cursor.getLong(cursor.getColumnIndexOrThrow(MessageReplyDatabase.REPLY_DATE));
            String reply     = cursor.getString(cursor.getColumnIndexOrThrow(MessageReplyDatabase.REPLY));
            String replyId   = cursor.getString(cursor.getColumnIndexOrThrow(MessageReplyDatabase.ID));

            String messageType = cursor.getString(cursor.getColumnIndexOrThrow(MessageReplyDatabase.SMS_HASH)) != null
                    ? MessageReplyDatabase.SMS_HASH : MessageReplyDatabase.MMS_HASH;

            String messageHash = cursor.getString(cursor.getColumnIndexOrThrow(messageType));

            Reply replyObj = new Reply(
                    replierAddress,
                    replyDate,
                    reply,
                    replyId,
                    messageType,
                    messageHash
            );
            results.add(replyObj);
        }
        return results;
    }

    public class Reply {
        private Address replierAddress;
        private Long    replyDate;
        private String  reply;
        private String  replyId;
        private String  messageType;
        private String  messageHash;

        public Reply(Address replierAddress, Long replyDate, String reply, String replyId, String messageType, String messageHash) {
            this.replierAddress = replierAddress;
            this.replyDate      = replyDate;
            this.reply          = reply;
            this.replyId        = replyId;
            this.messageType    = messageType;
            this.messageHash    = messageHash;
        }

        public Address getReplierAddress() {
            return replierAddress;
        }

        public Long getReplyDate() {
            return replyDate;
        }

        public String getReply() {
            return reply;
        }

        // replyId represents the row id in the database
        public String getReplyId() {
            return replyId;
        }

        public String getMessageType() {
            return messageType;
        }

        public String getMessageHash() {
            return messageHash;
        }
    }
}

