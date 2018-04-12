package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Parcel;
import android.support.annotation.NonNull;

import org.thoughtcrime.securesms.database.model.MessageRecord;

public class MessageReplyDatabase extends Database {
    private static final String TAG = MessageReplyDatabase.class.getSimpleName();

    public static final String TABLE_NAME = "replies";
    public static final String ID         = "id";
    public static final String SMS_HASH   = "sms_id";
    public static final String MMS_HASH   = "mms_id";
    public static final String REPLY      = "reply";
    public static final String REPLIER_ID = "replier_id";
    public static final String HASH_ID    = "hash";
    public static final String REPLY_DATE = "date_sent";

    public static final String[] PROJECTION = {
            REPLY,
            SMS_HASH,
            MMS_HASH,
            REPLY,
            REPLIER_ID,
            REPLY_DATE,
            HASH_ID};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + "  INTEGER PRIMARY KEY, " +
            SMS_HASH + " TEXT DEFAULT NULL, " +
            MMS_HASH + " TEXT DEFAULT NULL, " +
            REPLY + " TEXT NOT NULL, " +
            REPLIER_ID + " TEXT NOT NULL, " +
            REPLY_DATE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + SMS_HASH + ") REFERENCES " + SmsDatabase.TABLE_NAME + "(" + SmsDatabase.HASH + ") ON DELETE CASCADE," +
            "FOREIGN KEY(" + MMS_HASH + ") REFERENCES " + MmsDatabase.TABLE_NAME + "(" + MmsDatabase.HASH + ") ON DELETE CASCADE);";

    public MessageReplyDatabase(Context context, SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }

    @NonNull
    private String getMessageType(MessageRecord messageRecord) {
        String messageType;
        if (messageRecord.isMms()) {
            messageType = MMS_HASH;
        } else {
            messageType = SMS_HASH;
        }
        return messageType;
    }

    public int removeReply(MessageRecord record) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        return database.delete(TABLE_NAME, getMessageType(record) + " = ?", new String[]{record.getHash()});
    }

    public int removeDanglingSmsReplies() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        return database.delete(TABLE_NAME,SMS_HASH + " in (SELECT " + SMS_HASH + " FROM " + TABLE_NAME + " OUTER LEFT JOIN "
                        + SmsDatabase.TABLE_NAME + " ON " + TABLE_NAME + "." + SMS_HASH + " = sms.hash WHERE sms.hash is null)",
                new String[]{});
    }

    public int removeDanglingMmsReplies() {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        return database.delete(TABLE_NAME,MMS_HASH + " in (SELECT " + MMS_HASH+ " FROM " + TABLE_NAME + " OUTER LEFT JOIN "
                        + MmsDatabase.TABLE_NAME + " ON " + TABLE_NAME + "." + MMS_HASH + " = mms.hash WHERE mms.hash is null)",
                new String[]{});
    }

    public void replyToMessage(MessageRecord record, String reply, Long time) {
        String        messageType;
        ContentValues values = new ContentValues();

        messageType = getMessageType(record);
        values.put(messageType, record.getHash());

        Address address;
        try {
            address = DatabaseFactory.getIdentityDatabase(context).getMyIdentity().getAddress();
            } catch (Exception e) {
                e.printStackTrace();
                Parcel p = Parcel.obtain();
                p.writeString("Me");
                p.setDataPosition(0);
                address = new Address(p);
            }

        values.put(REPLY, reply);
        values.put(REPLIER_ID, address.serialize());
        values.put(REPLY_DATE, time);

        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        db.insert(TABLE_NAME, null, values);
    }

    public void replyToMessage(String hash, String reply, Long replyTime, String replierAddress, Long threadId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String      type;
        ContentValues values = new ContentValues();

        type = db.rawQuery("SELECT " + MmsSmsColumns.HASH + " FROM " + SmsDatabase.TABLE_NAME
                + " WHERE " + MmsSmsColumns.HASH + " = ?", new String[] {hash})
                .getCount() > 0 ? SMS_HASH : MMS_HASH;

        values.put(type, hash);
        values.put(REPLY, reply);
        values.put(REPLIER_ID, replierAddress);
        values.put(REPLY_DATE, replyTime);

        db.insert(TABLE_NAME, null, values);

        notifyConversationListeners(threadId);
    }

    public Cursor getMessageReplies(MessageRecord record) {
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String hash = record.getHash();

        if (hash == null) return null;

        return database.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " +
                getMessageType(record) + " = ?" , new String[]{record.getHash()});

    }
}
