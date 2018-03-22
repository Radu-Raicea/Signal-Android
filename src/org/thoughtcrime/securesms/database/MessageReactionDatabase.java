package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.thoughtcrime.securesms.database.model.MessageRecord;

import java.util.HashMap;


public class MessageReactionDatabase extends Database {

    private static final String TAG = MessageReactionDatabase.class.getSimpleName();

    public static final String TABLE_NAME = "reactions";
    public static final String ID         = "id";
    public static final String SMS_ID     = "sms_id";
    public static final String MMS_ID     = "mms_id";
    public static final String REACTION   = "reaction";
    public static final String REACTOR_ID = "reactor_id";
    public static final String HASH_ID    = "hash";

    public static final String[] PROJECTION = {MessageReactionDatabase.REACTION,
            MessageReactionDatabase.SMS_ID,
            MessageReactionDatabase.MMS_ID,
            MessageReactionDatabase.REACTION,
            MessageReactionDatabase.REACTOR_ID};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + "  INTEGER PRIMARY KEY, " +
            SMS_ID + " INTEGER DEFAULT NULL, " +
            MMS_ID + " INTEGER DEFAULT NULL, " +
            REACTION + " TEXT NOT NULL, " +
            REACTOR_ID + " TEXT NOT NULL, " +
            HASH_ID + " TEXT NOT NULL, " +
            "FOREIGN KEY(" + SMS_ID + ") REFERENCES " + SmsDatabase.TABLE_NAME + "(" + SmsDatabase.ID + ")," +
            "FOREIGN KEY(" + MMS_ID + ") REFERENCES " + MmsDatabase.TABLE_NAME + "(" + MmsDatabase.ID + "));";

    public MessageReactionDatabase(Context context, SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }

    public void reactToMessage(MessageRecord messageRecord, String reaction, int reactorId) {
        Log.w("MessageDatabase", "reacting to: " + messageRecord.getId());

        ContentValues values = new ContentValues();
        String messageType;

        if (messageRecord.isMms()) {
            values.put(MMS_ID, messageRecord.getId());
            messageType = MMS_ID;
        } else {
            values.put(SMS_ID, messageRecord.getId());
            messageType = SMS_ID;
        }
        values.put(REACTION, reaction);
        values.put(REACTOR_ID, reactorId);

        insertOrUpdate(values, messageRecord.getId(), messageType);
    }

    private int processPinSqlRequest(ContentValues values, long messageId, String messageType) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.update(TABLE_NAME, values, messageType + " = ? AND " +
                REACTOR_ID + " = ? ",
                new String[] {values.getAsInteger(messageType) + "", values.getAsString(REACTOR_ID)});
    }

    private void insertOrUpdate(ContentValues values, long messageId, String messageType) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        database.beginTransaction();

        int updated = database.update(TABLE_NAME, values, messageType + " = ? AND " +
                        REACTOR_ID + " = ? ",
                new String[] {values.getAsInteger(messageType) + "", values.getAsString(REACTOR_ID)});

        if (updated < 1) {
            database.insert(TABLE_NAME, null, values);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

}
