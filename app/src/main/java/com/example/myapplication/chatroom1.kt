package com.example.myapplication


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.chatmessage
import com.example.myapplication.ui.home.RoomAdapter
import com.example.myapplication.ui.test.nav_test
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_chatroom1.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*


class chatroom1 : AppCompatActivity() {

    companion object{
        val TAG = "ChatLog"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatroom1)

        recyclerview_chatroom1.adapter = adapter

        val user = intent.getParcelableExtra<User>(newmessage.USER_KEY)

        textView61.text = user?.name

        listenformessage()

        send_button_chat_log.setOnClickListener {
            Log.d(TAG, "Attempt to send message...")
            performSendMessage()
        }
    }

    private fun listenformessage(){

        //val ref = FirebaseDatabase.getInstance().getReference("/message")

        val fromid = FirebaseAuth.getInstance().uid
        val toid = toUser?.UID
        val ref = FirebaseDatabase.getInstance().getReference("/user-message/$fromid")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(chatmessage::class.java)
                //Log.d(TAG, chatMessage.text)}
                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)



                    if (chatMessage.fromid == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    }else{
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
            }
        })
    }



    private fun performSendMessage(){
        //send message to firebase
        val text = editTextTextMultiLine2.text.toString()

        val fromid = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(newmessage.USER_KEY)
        val toid = user!!.name

        if (fromid == null) return

        //val reference = FirebaseDatabase.getInstance().getReference("/message").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-message/$fromid/$toid")
            .push()

        val toreference = FirebaseDatabase.getInstance().getReference("/user-message/$toid/$fromid")
            .push()

        val chatMessage = chatmessage(reference.key!!, text, fromid!!, toid,
            System.currentTimeMillis()/ 1000)
        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Saved our chat message: ${reference.key}")
            editTextTextMultiLine2.text.clear()
            recyclerview_chatroom1.scrollToPosition(adapter.itemCount - 1)
        }

        toreference.setValue(chatMessage)
    }

    fun back5(p0: View){
        startActivity(Intent(this,newmessage::class.java))
    }

}

class ChatFromItem(val text:String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}


class ChatToItem(val text:String): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
    }
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}