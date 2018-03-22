package org.thoughtcrime.securesms.database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;


public class MessageReactionDatabase extends Database {

    private static final String TAG = MessageReactionDatabase.class.getSimpleName();

    public static final String TABLE_NAME = "reactions";
    public static final String ID         = "id";
    public static final String SMS_ID     = "sms_id";
    public static final String MMS_ID     = "mms_id";
    public static final String REACTION   = "reaction";
    public static final String REACTOR_ID = "reactor_id";

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
            "FOREIGN KEY(" + SMS_ID + ") REFERENCES " + SmsDatabase.TABLE_NAME + "(" + SmsDatabase.ID + ")," +
            "FOREIGN KEY(" + MMS_ID + ") REFERENCES " + MmsDatabase.TABLE_NAME + "(" + MmsDatabase.ID + "));";

    public MessageReactionDatabase(Context context, SQLiteOpenHelper databaseHelper) {
        super(context, databaseHelper);
    }


}
