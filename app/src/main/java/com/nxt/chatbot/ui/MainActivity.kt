package com.nxt.chatbot.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.nxt.chatbot.R
import com.nxt.chatbot.data.Message
import com.nxt.chatbot.utils.BotResponse
import com.nxt.chatbot.utils.Constants.OPEN_GOOGLE
import com.nxt.chatbot.utils.Constants.OPEN_SEARCH
import com.nxt.chatbot.utils.Constants.RECEIVE_ID
import com.nxt.chatbot.utils.Constants.SEND_ID
import com.nxt.chatbot.utils.Time
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

//firebase

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("peter", "Francesca", "luigi", "Igor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerview()
        clickEvents()

        val random = (0..3).random()
        customMessage("Hello! Today you 're speaking with ${botList[random]}, How may I help?")

        listenForMessage()
    }

    private fun listenForMessage() {
        val ref = FirebaseDatabase.getInstance().getReference("/message")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java)

                adapter.insertMessage(message!!)
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun clickEvents() {
        btn_send.setOnClickListener {
            sendMessage()
        }

        et_message.setOnClickListener {
            GlobalScope.launch {
                delay(100)
            }
        }
    }


    private fun recyclerview() {
        adapter = MessagingAdapter()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(applicationContext)
    }

    private fun sendMessage() {
        val message = et_message.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            val ref = FirebaseDatabase.getInstance().getReference("/message").push()
            ref.setValue(Message(message, SEND_ID, timeStamp))
                .addOnSuccessListener {
                    et_message.text.clear()

                    rv_messages.scrollToPosition(adapter.itemCount - 1)
                }
            botResponse(message)
        }
    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        //xử lý mã hóa bất đồng bộ
        //khi bạn nt bot delay tra loi nhung có the nhắn 1 tin nhắn khác khi chưa có tin nhăn trả lời từ bot
        //launch 1 couroutine mới
        GlobalScope.launch {
            delay(1000)

            withContext(Dispatchers.Main) {
                //withcontext chạy trên mainthread của android : update tác vụ lâu dài lên UI
                val response = BotResponse.basicResponse(message)

                //adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)

                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String? = message.substringAfter("search")
                        site.data = Uri.parse("https://www.google.com/search?q=$searchTerm")
                        startActivity(site)
                    }
                }

                val ref = FirebaseDatabase.getInstance().getReference("/message").push()
                ref.setValue(Message(response, RECEIVE_ID, timeStamp))
            }
        }
    }

    //dam bao giư tn luôn ở cuối màn hình
    override fun onStart() {
        super.onStart()

        GlobalScope.launch {
            delay(10000)
            withContext(Dispatchers.Main) {
                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    //tn hien thi dau tien
    private fun customMessage(message: String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rv_messages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}