package com.example.myapplication

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.databinding.ActivityRoomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_room.*
import java.lang.Exception
import java.util.ArrayList

lateinit var auth: FirebaseAuth
lateinit var data1:String
lateinit var textviewlist:List<TextView>
lateinit var imagebtnlist:List<ImageButton>
class room : AppCompatActivity() {
    lateinit var binding:ActivityRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        textviewlist= listOf(binding.textView15,binding.textView16,binding.textView17,binding.textView18,binding.textView33,binding.textView31)
        imagebtnlist= listOf(binding.imageButton9,binding.imageButton12,binding.imageButton13,binding.imageButton14,binding.imageButton3,binding.imageButton4)
        data1 = intent.getStringExtra("Data").toString()
        dataselect()
        whenenterroom()
        binding.imageButton15.setOnClickListener {
            if(data1 != usersphone){
                Toast.makeText(this, "駕駛才能鎖定房間唷", Toast.LENGTH_LONG).show()
            }
            else {
                try {
                    if(roominfo["nolockorlocked"]=="nolock") {
                        nolock_to_locked()
                    }
                    else{
                        locked_to_nolock()
                    }
                }
                catch(e:Exception){
                    Log.d("test",e.toString())
                }
            }
        }
    }
    val imagelist= listOf<List<Int>>(
        listOf(R.drawable.sam1,R.drawable.sam2,R.drawable.sam3,R.drawable.sam4),
        listOf(R.drawable.emma1,R.drawable.emma2,R.drawable.emma3))
    lateinit var profilelist:HashMap<*,*>
    lateinit var roomlist:HashMap<*,*>
    lateinit var usersphone:String
    lateinit var user:HashMap<*,*>
    lateinit var roominfo:HashMap<*,*>
    fun dataselect(){
        auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance().reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {

                    val root = dataSnapshot.value as HashMap<*, *>
                    profilelist = root["profile"] as HashMap<*, *>
                    val driver = profilelist[data1] as HashMap<*, *>
                    val driversname = driver["name"].toString()
                    binding.textView13.text = driversname
                    roomlist = root["room"] as HashMap<*, *>
                    usersphone = auth.currentUser?.phoneNumber.toString()
                    user = profilelist[usersphone] as HashMap<*, *>
                    val roomowner = roomlist[data1] as HashMap<*, *>
                    roominfo = roomowner["roomINFO"] as HashMap<*, *>
                    val roommember = roominfo["roommember"] as ArrayList<String>
                    selectarraydata(roommember, profilelist)
                    imageButton16.setOnClickListener {
                        val sitearray = roominfo["sitearray"] as ArrayList<String>
                        findsitearraysvalue(sitearray)
                    }

                }
                catch (e:Exception){

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        database.addValueEventListener(dataListener)
    }
    fun selectarraydata(roommember:ArrayList<String>,profilelist:HashMap<*,*>){
        for (i in roommember.indices) {
            filldata(roommember[i],imagebtnlist[i],textviewlist[i],profilelist)
            Log.d("77777",roommember[i])
        }

    }
    fun filldata(roommembersphone:String,imagebtn:ImageButton,textV:TextView,profilelist: HashMap<*,*>){
        val members=profilelist[roommembersphone] as HashMap<*,*>
        textV.text=members["name"].toString()
        if(members["gender"]=="male") {
            val samList=imagelist[0]
            imagebtn.setImageResource(samList.random())
        }
        else{
            val emmaList=imagelist[1]
            imagebtn.setImageResource(emmaList.random())
        }
    }
    fun locked_to_nolock(){
        auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance().reference
        binding.imageButton15.setImageResource(R.drawable.ic_lock_open)
        binding.imageButton9.setBackgroundColor(Color.parseColor("#325774"))
        binding.imageButton12.setBackgroundColor(Color.parseColor("#325774"))
        binding.imageButton13.setBackgroundColor(Color.parseColor("#325774"))
        binding.imageButton14.setBackgroundColor(Color.parseColor("#325774"))
        binding.imageButton3.setBackgroundColor(Color.parseColor("#325774"))
        binding.imageButton4.setBackgroundColor(Color.parseColor("#325774"))
        database.child("room").child(data1).child("roomINFO").child("nolockorlocked")
            .setValue("nolock")
    }
    fun nolock_to_locked(){
        auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance().reference
        binding.imageButton15.setImageResource(R.drawable.ic_locked)
        binding.imageButton9.setBackgroundColor(Color.parseColor("#A63C24"))
        binding.imageButton12.setBackgroundColor(Color.parseColor("#A63C24"))
        binding.imageButton13.setBackgroundColor(Color.parseColor("#A63C24"))
        binding.imageButton14.setBackgroundColor(Color.parseColor("#A63C24"))
        binding.imageButton3.setBackgroundColor(Color.parseColor("#A63C24"))
        binding.imageButton4.setBackgroundColor(Color.parseColor("#A63C24"))
        database.child("room").child(data1).child("roomINFO").child("nolockorlocked")
            .setValue("locked")
    }
    fun whenenterroom(){
        try {
            auth = FirebaseAuth.getInstance()
            var database = FirebaseDatabase.getInstance().reference
            database.child("room").child(data1).child("roomINFO").get().addOnSuccessListener {
                val roominfomation=it.value as HashMap<*,*>
                if (roominfomation["nolockorlocked"] == "locked") {
                    binding.imageButton15.setImageResource(R.drawable.ic_locked)
                    binding.imageButton9.setBackgroundColor(Color.parseColor("#A63C24"))
                    binding.imageButton12.setBackgroundColor(Color.parseColor("#A63C24"))
                    binding.imageButton13.setBackgroundColor(Color.parseColor("#A63C24"))
                    binding.imageButton14.setBackgroundColor(Color.parseColor("#A63C24"))
                    binding.imageButton3.setBackgroundColor(Color.parseColor("#A63C24"))
                    binding.imageButton4.setBackgroundColor(Color.parseColor("#A63C24"))
                    Log.d("777", "789")
                } else {
                    binding.imageButton15.setImageResource(R.drawable.ic_lock_open)
                    binding.imageButton9.setBackgroundColor(Color.parseColor("#325774"))
                    binding.imageButton12.setBackgroundColor(Color.parseColor("#325774"))
                    binding.imageButton13.setBackgroundColor(Color.parseColor("#325774"))
                    binding.imageButton14.setBackgroundColor(Color.parseColor("#325774"))
                    binding.imageButton3.setBackgroundColor(Color.parseColor("#325774"))
                    binding.imageButton4.setBackgroundColor(Color.parseColor("#325774"))
                    Log.d("777", "456")
                }
            }
        }
        catch(e:Exception) {
            Log.d("777", "123")
        }
    }



    fun entergooglemap(site: String){
        auth = FirebaseAuth.getInstance()
        var database = FirebaseDatabase.getInstance().reference
        val dataListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val root = dataSnapshot.value as HashMap<*, *>
                val room = root["room"] as HashMap<*, *>
                val roomowner=room[data1] as HashMap<*,*>
                roominfo = roomowner["roomINFO"] as HashMap<*, *>
                val ownerstartpoint = roominfo["startpoint"].toString()
                val ownerendpoint = roominfo["endpoint1"].toString()
                val url = Uri.parse(
                    "https://www.google.com/maps/dir/?api=1&origin=" + ownerstartpoint + "&destination=" + ownerendpoint + "&travelmode=driving&waypoints="+site
                )
                val intent = Intent().apply {
                    action = "android.intent.action.VIEW"
                    data = url
                }
                startActivity(intent)
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        }
        database.addValueEventListener(dataListener)
    }
//    fun leaveroom(){
//        auth = FirebaseAuth.getInstance()
//        var phone = auth.currentUser?.phoneNumber.toString()
//        var database = FirebaseDatabase.getInstance().reference
//        database.child("room").child(data1).child("roomINFO").get().addOnSuccessListener {
//            val roominfoleave=it.value as java.util.HashMap<String,Any>
//            val roommember =roominfoleave["roommember"] as ArrayList<String>
//            roommember.remove(phone)
//            roominfoleave.put("roommember",roommember)
//            database.child("profile").child(phone).updateChildren(roominfoleave)
//        }
//        database.child("profile").child(phone).get().addOnSuccessListener {
//            val userinfoleave=it.value as java.util.HashMap<String,Any>
//            val joining =userinfoleave["joining"] as ArrayList<String>
//            joining.remove(data1)
//            userinfoleave.put("joining",joining)
//            database.child("profile").child(phone).updateChildren(userinfoleave)
//        }
//    }
    fun findsitearraysvalue(sitearray:ArrayList<String>){
        var site0=""
        var finsite=""
        for (i in sitearray.indices) {

            Log.d("77777",sitearray[i]+"%7C")

            var site=sitearray[i]+"%7c"
            Log.d("000", site)
            site0+=site
        }
        finsite=site0
        entergooglemap(finsite)
    }
}