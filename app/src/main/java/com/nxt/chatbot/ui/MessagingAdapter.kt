package com.nxt.chatbot.ui

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.nxt.chatbot.R
import com.nxt.chatbot.data.Message
import com.nxt.chatbot.utils.Constants.RECEIVE_ID
import com.nxt.chatbot.utils.Constants.SEND_ID
import kotlinx.android.synthetic.main.activity_main.*
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

                val builder = AlertDialog.Builder(itemView.context)
                builder.setTitle("Delete Message")
                builder.setMessage("go tin nhan tu ban ")
                builder.setIcon(android.R.drawable.ic_dialog_alert)
                builder.setPositiveButton("Yes") { dialog, which ->

                    messageList.removeAt(adapterPosition)
                    notifyItemRemoved(adapterPosition)
                    dialog.dismiss()

                }
                builder.setNegativeButton("No") { dialog, which ->
                    dialog.dismiss()
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        }
    }
}