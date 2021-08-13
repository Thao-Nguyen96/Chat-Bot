package com.nxt.chatbot.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nxt.chatbot.R
import com.nxt.chatbot.data.Message
import com.nxt.chatbot.utils.Constants.RECEIVE_ID
import com.nxt.chatbot.utils.Constants.SEND_ID
import kotlinx.android.synthetic.main.message_item.view.*
import kotlinx.coroutines.delay

class MessagingAdapter : RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    val messageList = mutableListOf<Message>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messageList[position]

        when (currentMessage.id) {
            SEND_ID -> {
                holder.itemView.tv_message.apply {
                    text = currentMessage.message
                    //hiện tn nguwoi dùng
                    visibility = View.VISIBLE
                }
                holder.itemView.tv_bot_message.visibility = View.GONE
            }

            RECEIVE_ID -> {
                holder.itemView.tv_bot_message.apply {
                    text = currentMessage.message
                    //hiện tn nguwoi dùng
                    visibility = View.VISIBLE
                }
                holder.itemView.tv_message.visibility = View.GONE
            }

        }
    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    fun insertMessage(message: Message) {
        this.messageList.add(message)
        //thông báo số item đã chèn
        notifyItemInserted(messageList.size)

        Log.d("bbb", messageList.toString())
    }

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //xoa tn khi click vào
        init {
            itemView.setOnClickListener {
                messageList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }
}