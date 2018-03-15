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
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import org.thoughtcrime.securesms.components.AudioView;
import org.thoughtcrime.securesms.components.DocumentView;
import org.thoughtcrime.securesms.components.ThumbnailView;
import org.thoughtcrime.securesms.crypto.MasterSecret;
import org.thoughtcrime.securesms.database.DatabaseFactory;
import org.thoughtcrime.securesms.database.MmsSmsDatabase;
import org.thoughtcrime.securesms.database.model.MediaMmsMessageRecord;
import org.thoughtcrime.securesms.database.model.MessageRecord;
import org.thoughtcrime.securesms.database.model.MmsMessageRecord;
import org.thoughtcrime.securesms.mms.GlideRequests;
import org.thoughtcrime.securesms.util.DateUtils;
import org.thoughtcrime.securesms.util.views.Stub;

import java.util.Locale;

import static android.widget.RelativeLayout.ALIGN_BASELINE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

public class PinnedMessageAdapter extends RecyclerView.Adapter<PinnedMessageAdapter.ViewHolder> {
    private Context              context;
    private Cursor               dataCursor;
    private GlideRequests        glideRequests;
    private MasterSecret         masterSecret;
    private MmsSmsDatabase       db;
    private View                 view;

    public PinnedMessageAdapter(Activity mContext, Cursor cursor, MasterSecret masterSecret, GlideRequests glideRequests) {
        this.context       = mContext;
        this.dataCursor    = cursor;
        this.db            = DatabaseFactory.getMmsSmsDatabase(mContext);
        this.glideRequests = glideRequests;
        this.masterSecret  = masterSecret;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Stub<AudioView>     audioViewStub;
        private Stub<DocumentView>  documentViewStub;
        private Stub<ThumbnailView> mediaThumbnailStub;
        public  TextView            messageContent;
        public  TextView            recipient;
        public  TextView            time;
        public  View                wrapper;

        public ViewHolder(View v) {
            super(v);
            this.audioViewStub      = new Stub<>(v.findViewById(R.id.pinned_audio_view_stub));
            this.documentViewStub   = new Stub<>(v.findViewById(R.id.pinned_document_view_stub));
            this.mediaThumbnailStub = new Stub<>(v.findViewById(R.id.pinned_image_view_stub));
            this.messageContent     = v.findViewById(R.id.pinned_message_body);
            this.recipient          = v.findViewById(R.id.pinned_message_recipient);
            this.time               = v.findViewById(R.id.conversation_item_date);
            this.wrapper            = v.findViewById(R.id.pinned_message_wrapper);
        }
    }

    @Override
    public PinnedMessageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater        = LayoutInflater.from(parent.getContext());
        View           theInflatedView = inflater.inflate(R.layout.pinned_conversation_item, null);
        this.view = theInflatedView;
        return new ViewHolder(theInflatedView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        dataCursor.moveToPosition(position);
        ConversationItem      conversationItem = new ConversationItem(context);
        MmsSmsDatabase.Reader reader           = db.readerFor(dataCursor, masterSecret);
        MessageRecord         record           = reader.getCurrent();

        this.recyleViewHolder(holder);
        this.setMessageView(record, holder);

        if (isDocument(record)) {
            ConversationItem.ThumbnailClickListener thumbnailClickListener
                    = conversationItem.new ThumbnailClickListener(record);
            ConversationItem.AttachmentDownloadClickListener attachmentDownloadClickListener
                    = conversationItem.new AttachmentDownloadClickListener();
            holder.documentViewStub.get().setDocument(((MediaMmsMessageRecord)record).getSlideDeck().getDocumentSlide(), true);
            holder.documentViewStub.get().setDocumentClickListener(thumbnailClickListener);
            holder.documentViewStub.get().setDownloadClickListener(attachmentDownloadClickListener);
            holder.documentViewStub.get().setVisibility(view.VISIBLE);
        } else if (isAudio(record)) {
            ConversationItem.AttachmentDownloadClickListener attachmentDownloadClickListener
                    = conversationItem.new AttachmentDownloadClickListener();
            holder.audioViewStub.get().setAudio(masterSecret, ((MediaMmsMessageRecord)record).getSlideDeck().getAudioSlide(), true);
            holder.audioViewStub.get().setDownloadClickListener(attachmentDownloadClickListener);
            holder.audioViewStub.get().setVisibility(view.VISIBLE);
        } else if (isVideo(record)) {
            ConversationItem.ThumbnailClickListener thumbnailClickListener
                    = conversationItem.new ThumbnailClickListener(record);

            holder.mediaThumbnailStub.get().setImageResource(masterSecret, glideRequests,
                    ((MmsMessageRecord) record).getSlideDeck().getThumbnailSlide(),
                    true, true);
            holder.mediaThumbnailStub.get().setThumbnailClickListener(thumbnailClickListener);
            holder.mediaThumbnailStub.get().setVisibility(View.VISIBLE);
        }

        holder.messageContent.setText(record.getDisplayBody().toString());
        holder.time.setText(DateUtils.getExtendedRelativeTimeSpanString(context, new Locale("en", "CA"),
                record.getTimestamp()));

        holder.wrapper.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(R.string.PinnedMessageActivity_unpin_prompt);
            builder.setCancelable(true);

            builder.setPositiveButton(R.string.yes, (dialog, id) -> {
                PinnedMessageHandler handler = new PinnedMessageHandler(context);
                handler.handleUnpinMessage(record, handler.getAppropriateDatabase(record));

                ((ViewGroup)v.getParent().getParent()).removeAllViews();
                dialog.cancel();

                // Update cursor
                PinnedMessageLoader l = new PinnedMessageLoader(context,record.getThreadId(), 0);
                swapCursor(l.getCursor());
            });

            builder.setNegativeButton(R.string.no, (dialog, id) -> dialog.cancel());
            builder.create().show();
            return true;
        });
    }

    private void setMessageView(MessageRecord record, ViewHolder viewHolder) {
        LayoutParams lp = (LayoutParams) viewHolder.wrapper.getLayoutParams();
        if (record.isOutgoing()) {
            lp.addRule(ALIGN_PARENT_RIGHT);
            viewHolder.wrapper.setLayoutParams(lp);
            viewHolder.recipient.setText(R.string.PinnedMessageActivity_own_name);
            return;
        } else {
            lp.addRule(ALIGN_PARENT_LEFT);
            viewHolder.wrapper.setLayoutParams(lp);
        }

        String messageSenderName = record.getRecipient().getName();
        if (messageSenderName == null) {
            messageSenderName = record.getRecipient().getAddress().toString();
        }
        viewHolder.recipient.setText(messageSenderName);
    }

    private boolean isAudio(MessageRecord messageRecord) {
        return messageRecord.isMms() && ((MmsMessageRecord)messageRecord).getSlideDeck().getAudioSlide() != null;
    }

    private boolean isVideo(MessageRecord messageRecord) {
        return messageRecord.isMms() && ((MmsMessageRecord)messageRecord).getSlideDeck().getThumbnailSlide() != null;
    }

    private boolean isDocument(MessageRecord messageRecord) {
        return messageRecord.isMms() && ((MmsMessageRecord)messageRecord).getSlideDeck().getDocumentSlide() != null;
    }

    private void recyleViewHolder(ViewHolder holder) {
        holder.documentViewStub.get().setVisibility(view.GONE);
        holder.audioViewStub.get().setVisibility(view.GONE);
        holder.mediaThumbnailStub.get().setVisibility(View.GONE);
        holder.messageContent.setText("");
        holder.recipient.setText("");

        LinearLayout.LayoutParams lpMessage = new LinearLayout.LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        LinearLayout messageBubble = (LinearLayout)holder.wrapper.findViewById(R.id.pinned_body_bubble);
        holder.messageContent.setLayoutParams(lpMessage);

        LayoutParams lp = (LayoutParams) holder.wrapper.getLayoutParams();

        lp.addRule(ALIGN_PARENT_RIGHT,0);
        lp.addRule(ALIGN_PARENT_LEFT,0);
        holder.wrapper.setLayoutParams(lp);
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
