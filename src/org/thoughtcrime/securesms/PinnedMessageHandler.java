package org.thoughtcrime.securesms;

import android.content.Context;

import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MessagingDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;

public class PinnedMessageHandler {
    Context context;

    public PinnedMessageHandler(Context context) {
        this.context = context;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public MessagingDatabase getAppropriateDatabase(MessageRecord message) {
        if (message.isMms()) {
            return DatabaseFactory.getMmsDatabase(context);
        } else {
            return DatabaseFactory.getSmsDatabase(context);
        }
    }

    public boolean handlePinMessage(final MessageRecord message, MessagingDatabase databaseToQuery) {
        return databaseToQuery.pinMessage(message.getId());
    }

    public boolean handleUnpinMessage(final MessageRecord message, MessagingDatabase databaseToQuery) {
        return databaseToQuery.unpinMessage(message.getId());
    }
}
