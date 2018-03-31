package org.thoughtcrime.securesms;


import android.content.Context;
import android.database.Cursor;
import android.os.Parcel;

import org.thoughtcrime.securesms.database.Address;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessageReactionDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReactionsHandler {
    private MessageReactionDatabase reactionsDb;

    public ReactionsHandler(Context context) {
        this.reactionsDb = DatabaseFactory.getMessageReactionDatabase(context);
    }

    /**
     * NOT TEST
     * This method only used by the conversation fragment since it will automatically
     * compute the reactorID's to the user itself, Please uses another method if the reaction made
     * by the recipient. such method can be accessed directly from the MessageReactionDB class
     * The reaction time will be set to the current system time in milliseconds
     * @param record
     * @param reaction
     */
    public void reactToMessage(MessageRecord record, String reaction, Long time) {
        this.reactionsDb.reactToMessage(record, reaction, time);
    }

    /**
     * NOT TESTED
     * This method is ONLY used by JobReceivers since when a reaction received, there
     * is no active MessageRecord Message object, all what is required the MessageHash being reacted,
     * reaction, the reactionDate.
     * This Method can also be used by Fragments however, the developer should handle the time
     * of the reaction manually
     * @param messageHash
     * @param reaction
     * @param reactionTime
     * @param reactorID
     */
    public void reactToMessage(String messageHash, String reaction, Long reactionTime, Address reactorID) {
        this.reactionsDb.reactToMessage(messageHash, reaction, reactionTime, reactorID);
    }

    public boolean deleteReaction(MessageRecord record) {
        return this.reactionsDb.removeReaction(record) > 0 ? true:false;
    }

    public List<Reaction> getMessageReactions(MessageRecord record) {
        List<Reaction> results = new ArrayList<>();
        Cursor         cursor  = this.reactionsDb.getMessageReaction(record);

        if (cursor == null) return results;

        while (cursor.moveToNext()) {
            Parcel c = Parcel.obtain();
            c.writeString(cursor.getString(cursor.getColumnIndexOrThrow(MessageReactionDatabase.REACTOR_ID)));
            c.setDataPosition(0);
            Address reactor = new Address(c);

            Long reactionDate = cursor.getLong(cursor.getColumnIndexOrThrow(MessageReactionDatabase.REACTION_DATE));
            String reaction = cursor.getString(cursor.getColumnIndexOrThrow(MessageReactionDatabase.REACTION));
            String reactionId = cursor.getString(cursor.getColumnIndexOrThrow(MessageReactionDatabase.ID));

            String messageType = cursor.getString(cursor.getColumnIndexOrThrow(MessageReactionDatabase.SMS_HASH)) != null
                    ? MessageReactionDatabase.SMS_HASH : MessageReactionDatabase.MMS_HASH;

            String messageHash = cursor.getString(cursor.getColumnIndexOrThrow(messageType));

            Reaction reactionObj = new Reaction (
                reactor,
                reactionDate,
                reaction,
                reactionId,
                messageType,
                messageHash
            );

            results.add(reactionObj);
        }

        return results;
    }

    public Map<String, Integer> getReactionCounts(List<Reaction> messageReactions) {
        Map<String, Integer> reactionCounts = new HashMap<>();

        for (Reaction reactionObj : messageReactions) {
            String reaction = reactionObj.getReaction();
            if (reactionCounts.containsKey(reaction)) {
                reactionCounts.put(reaction, reactionCounts.get(reaction) + 1);
            } else {
                reactionCounts.put(reaction, 1);
            }
        }

        return reactionCounts;
    }

    public class Reaction {
        private Address reactor;
        private Long    reactionDate;
        private String  reaction;
        private String  reactionId;
        private String  messageType;
        private String  messageHash;

        public Reaction(Address reactor, Long reactionDate, String reaction, String reactionId, String messageType, String messageHash) {
            this.reactor      = reactor;
            this.reactionDate = reactionDate;
            this.reaction     = reaction;
            this.reactionId   = reactionId;
            this.messageType  = messageType;
            this.messageHash  = messageHash;
        }

        public Address getReactor() {
            return reactor;
        }

        public Long getReactionDate() {
            return reactionDate;
        }

        public String getReaction() {
            return reaction;
        }

        public String getReactionId() {
            return reactionId;
        }

        public String getMessageType() {
            return messageType;
        }

        public String getMessageHash() {
            return messageHash;
        }
    }
}
