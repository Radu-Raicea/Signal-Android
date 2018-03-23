package org.thoughtcrime.securesms.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.thoughtcrime.securesms.database.model.MessageRecord;

public class MessageReactionDatabase extends Database {
    private static final String TAG = MessageReactionDatabase.class.getSimpleName();

    public static final String TABLE_NAME    = "reactions";
    public static final String ID            = "id";
    public static final String SMS_HASH      = "sms_id";
    public static final String MMS_HASH      = "mms_id";
    public static final String REACTION      = "reaction";
    public static final String REACTOR_ID    = "reactor_id";
    public static final String HASH_ID       = "hash";
    public static final String REACTION_DATE = "date_sent";

    public static final String[] PROJECTION = {MessageReactionDatabase.REACTION,
                                                MessageReactionDatabase.SMS_HASH,
                                                MessageReactionDatabase.MMS_HASH,
                                                MessageReactionDatabase.REACTION,
                                                MessageReactionDatabase.REACTOR_ID,
                                                MessageReactionDatabase.REACTION_DATE,
                                                MessageReactionDatabase.HASH_ID};

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + ID + "  INTEGER PRIMARY KEY, " +
            SMS_HASH + " TEXT DEFAULT NULL, " +
            MMS_HASH + " TEXT DEFAULT NULL, " +
            REACTION + " TEXT NOT NULL, " +
            REACTOR_ID + " TEXT NOT NULL, " +
            HASH_ID + " TEXT, " +
            REACTION_DATE + " INTEGER NOT NULL, " +
            "FOREIGN KEY(" + SMS_HASH + ") REFERENCES " + SmsDatabase.TABLE_NAME + "(" + SmsDatabase.HASH + ")," +
            "FOREIGN KEY(" + MMS_HASH + ") REFERENCES " + MmsDatabase.TABLE_NAME + "(" + MmsDatabase.HASH + "));";

    public MessageReactionDatabase(Context context, SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }

    public void reactToMessage(MessageRecord messageRecord, String reaction, int reactorId) {
        Log.w("MessageDatabase", "reacting to: " + messageRecord.getId());

        ContentValues values = new ContentValues();
        String        messageType;

        if (messageRecord.isMms()) {
            values.put(MMS_HASH, messageRecord.getId());
            messageType = MMS_HASH;
        } else {
            values.put(SMS_HASH, messageRecord.getId());
            messageType = SMS_HASH;
        }
        values.put(REACTION, reaction);
        values.put(REACTOR_ID, reactorId);

        insertOrUpdate(values, messageRecord.getId(), messageType);
    }

    private int processReactionSqlRequest(ContentValues values, long messageId, String messageType) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.update(TABLE_NAME, values, messageType + " = ? AND " +
                        REACTOR_ID + " = ? ",
                new String[]{values.getAsInteger(messageType) + "", values.getAsString(REACTOR_ID)});
    }

    private void insertOrUpdate(ContentValues values, long messageId, String messageType) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        database.beginTransaction();

        int updated = database.update(TABLE_NAME, values, messageType + " = ? AND " +
                        REACTOR_ID + " = ? ",
                new String[]{values.getAsInteger(messageType) + "", values.getAsString(REACTOR_ID)});

        if (updated < 1) {
            database.insert(TABLE_NAME, null, values);
        }

        database.setTransactionSuccessful();
        database.endTransaction();
    }

}
