/*
 * Copyright (C) 2011 Whisper Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.thoughtcrime.securesms;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.thoughtcrime.securesms.components.ThumbnailView;
import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MmsSmsDatabase;
import org.thoughtcrime.securesms.database.model.MessageRecord;
import org.thoughtcrime.securesms.database.model.MmsMessageRecord;
import org.thoughtcrime.securesms.mms.GlideRequests;
import org.thoughtcrime.securesms.util.DateUtils;
import org.thoughtcrime.securesms.util.views.Stub;

import java.util.Locale;

public class PinnedMessageAdapter extends RecyclerView.Adapter<PinnedMessageAdapter.ViewHolder> {
    private Context        context;
    private Cursor         dataCursor;
    private MmsSmsDatabase db;
    private GlideRequests  glideRequests;
    private MasterSecret   masterSecret;
    private View           view;

    public PinnedMessageAdapter(Activity mContext, Cursor cursor, MasterSecret masterSecret, GlideRequests glideRequests) {
        dataCursor = cursor;
        context = mContext;
        db = DatabaseFactory.getMmsSmsDatabase(mContext);
        this.masterSecret = masterSecret;
        this.glideRequests = glideRequests;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Stub<ThumbnailView> mediaThumbnailStub;
        public  TextView            messageContent;
        public  TextView            recipient;
        public  TextView            time;

        public ViewHolder(View v) {
            super(v);
            messageContent = (TextView) v.findViewById(R.id.pinned_message_body);
            recipient = (TextView) v.findViewById(R.id.pinned_message_recipient);
            time = (TextView) v.findViewById(R.id.conversation_item_date);
            mediaThumbnailStub = new Stub<>(v.findViewById(R.id.pinned_image_view_stub));
        }
    }

    @Override
    public PinnedMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater        = LayoutInflater.from(parent.getContext());
        View           theInflatedView = inflater.inflate(R.layout.pinned_conversation_item_sent, null);
        this.view = theInflatedView;
        return new ViewHolder(theInflatedView);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);
        MmsSmsDatabase.Reader reader = db.readerFor(dataCursor, masterSecret);
        MessageRecord         record = reader.getCurrent();

        if (record.isMms()) {
            ConversationItem                        converationItem        = new ConversationItem(context);
            ConversationItem.ThumbnailClickListener thumbnailClickListener = converationItem.new ThumbnailClickListener(record);
            holder.mediaThumbnailStub.get().setImageResource(masterSecret, glideRequests,
                    ((MmsMessageRecord) record).getSlideDeck().getThumbnailSlide(),
                    true, true);
            holder.mediaThumbnailStub.get().setVisibility(View.VISIBLE);
            holder.mediaThumbnailStub.get().setThumbnailClickListener(thumbnailClickListener);
        }

        this.setMessageView(record, holder);
        holder.messageContent.setText(record.getDisplayBody().toString());
        holder.time.setText(DateUtils.getExtendedRelativeTimeSpanString(context, new Locale("en", "CA"),
                record.getTimestamp()));

        Button unpinButton = (Button) view.findViewById(R.id.unpin_button);
        unpinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PinnedMessageHandler handler = new PinnedMessageHandler(context);
                handler.handleUnpinMessage(record, handler.getAppropriateDatabase(record));
                ((ViewGroup) v.getParent().getParent().getParent()).removeAllViews();
            }
        });
    }

    private void setMessageView(MessageRecord record, ViewHolder viewHolder) {
        if (record.isOutgoing()) {
            viewHolder.recipient.setText(R.string.PinnedMessageActivity_own_name);
        }

        String messageSenderName = record.getRecipient().getName();
        if (messageSenderName == null) {
            messageSenderName = record.getRecipient().getAddress().toString();
        }
        viewHolder.recipient.setText(messageSenderName);
    }

    @Override
    public int getItemCount() {
        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }
}
