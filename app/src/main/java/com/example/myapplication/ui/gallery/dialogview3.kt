package com.example.myapplication.ui.gallery

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.myapplication.*
import com.example.myapplication.databinding.MyroomdialogviewBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.ArrayList

lateinit var auth: FirebaseAuth
class dialogview3(val data1:String,val roomlist:HashMap<*,*>): DialogFragment() {
    //        View元素綁定
    private lateinit var binding: MyroomdialogviewBinding
    lateinit var roomphone:HashMap<*,*>
    lateinit var auth: FirebaseAuth
    var nowpeoplevalue =0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//            binding實例化
        binding = MyroomdialogviewBinding.inflate(layoutInflater)

//            關閉按鈕
        binding.close.setOnClickListener {
            dismiss()
        }

//      進入按鈕
        binding.access.setOnClickListener {
            nextpage()
        }

        roomphone=roomlist[data1] as HashMap<*,*>
        val roominfo = roomphone["roomINFO"] as HashMap<*, *>
        binding.textView84.text = roominfo["date"].toString()
        binding.textView81.text = roominfo["time"].toString()
        binding.textView79.text = roominfo["price"].toString()
        binding.textView78.text = roominfo["other"].toString()
        val limitpeople = roominfo["peoplelimit"].toString()
        try {
            val nowpeople = roominfo["roommember"] as ArrayList<String>
            nowpeople.onEach { ele->
                if(ele.isNotBlank()){
                    nowpeoplevalue+=1
                }
            }
        }
        catch(e:Exception){

        }
        binding.textView82.text = "$nowpeoplevalue/$limitpeople"
        binding.textView76.text = roominfo["number"].toString()
        startendpoint(roominfo)
        val roomrule = roomphone["roomRULE"] as HashMap<*, *>
        manwoman = roomrule["gender"].toString()
        pet = roomrule["pet"].toString()
        smoke = roomrule["smoke"].toString()
        child = roomrule["child"].toString()
        iconselect()



        return binding.root
    }

    lateinit var manwoman: String
    lateinit var pet: String
    lateinit var smoke: String
    lateinit var child: String

    fun iconselect() {
        when (manwoman) {
            "限男" -> {
                binding.wm.setImageResource(R.drawable.manonly)
            }
            "限女" -> {
                binding.wm.setImageResource(R.drawable.girlonly)
            }
            "皆可" -> {
                binding.wm.setImageResource(R.drawable.boy_girl2)
            }
        }
        when (pet) {
            "可" -> {
                binding.pet.setImageResource(R.drawable.ic_pet)
            }
            "不可" -> {
                binding.pet.setImageResource(R.drawable.ic_pet_n)
            }
        }
        when (child) {
            "可" -> {
                binding.child.setImageResource(R.drawable.ic_child)
            }
            "不可" -> {
                binding.child.setImageResource(R.drawable.ic_child_n)
            }
        }
        when (smoke) {
            "可" -> {
                binding.smokeing.setImageResource(R.drawable.ic_smoking)
            }
            "不可" -> {
                binding.smokeing.setImageResource(R.drawable.ic_smoking_n)
            }
        }
    }

    fun startendpoint(roominfo: HashMap<*, *>) {
        try {
            val startpointselect = roominfo["startpoint"].toString()
            val startpointfinal = startpointselect.substring(
                startpointselect.indexOf("區") - 2,
                startpointselect.indexOf("區")
            )
            binding.textView85.text = startpointfinal
            val endpointselect = roominfo["endpoint1"].toString()
            val endpointfinal = endpointselect.substring(
                endpointselect.indexOf("區") - 2,
                endpointselect.indexOf("區")
            )
            binding.textView86.text = endpointfinal
        } catch (e: Exception) {

        }
    }
//    fun entergooglemap(){
//        auth = FirebaseAuth.getInstance()
//        var database = FirebaseDatabase.getInstance().reference
//        val dataListener = object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                val root = dataSnapshot.value as HashMap<*, *>
//                val room = root["room"] as HashMap<*, *>
//                val roomowner=room[data1] as HashMap<*,*>
//                val roominfo = roomowner["roomINFO"] as HashMap<*, *>
//                val ownerstartpoint = roominfo["startpoint"].toString()
//                val ownerendpoint = roominfo["endpoint1"].toString()
//                val url = Uri.parse(
//                    "https://www.google.com/maps/dir/?api=1&origin=" + ownerstartpoint + "&destination=" + ownerendpoint + "&travelmode=driving"
//                )
//                val intent = Intent().apply {
//                    action = "android.intent.action.VIEW"
//                    data = url
//                }
//                startActivity(intent)
//            }
//            override fun onCancelled(databaseError: DatabaseError) {
//            }
//        }
//        database.addValueEventListener(dataListener)
//    }

    fun nextpage(){
        Intent(requireContext(), room::class.java).apply {
            putExtra("Data", this@dialogview3.data1)
            startActivity(this)
            Toast.makeText(requireContext(), "進入組隊房間", Toast.LENGTH_LONG)
                .show()
        }
    }
}