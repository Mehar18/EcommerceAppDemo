package com.example.e_commerce.ui.myorder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.adapter.MyCartAdapter
import com.example.e_commerce.adapter.MyOrdersAdapter
import com.example.e_commerce.data.DealItem
import com.example.e_commerce.data.MyOrderItem
import com.example.e_commerce.data.PrefManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MyOrderFragment : Fragment() {

    private val myOrdersList: ArrayList<MyOrderItem> = ArrayList()

    lateinit var mAdapter: MyOrdersAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var prefManager: PrefManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_my_order, container, false)

        recyclerView = view.findViewById(R.id.myOrdersRecyclerView)
        mAdapter = MyOrdersAdapter(myOrdersList)
        recyclerView.adapter=mAdapter
        recyclerView.layoutManager = LinearLayoutManager(view.context,
        LinearLayoutManager.VERTICAL,false)
        recyclerView.setHasFixedSize(true)

        prefManager = PrefManager(requireContext())

        if (prefManager.isLogin()){
            getData()
        }







        return view
    }

    private fun getData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userRef =
            FirebaseDatabase.getInstance().reference.child("OrdersReceived")
                .child(currentUser!!.displayName!!)
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    var itemImage: Int
                    var itemName: String
                    var itemPrice: Int
                    var price = 0
                    for (snap in snapshot.children) {
                        val cartData = snap.getValue(DealItem::class.java)
                        itemImage = cartData?.image!!
                        itemName = cartData.name
                        itemPrice = cartData.price
                        price += cartData.price
                        myOrdersList.add(MyOrderItem(itemImage, itemName, itemPrice))
                        // Log.d("list",list.toString())

                    }
                  //  priceTV.text = "\u20B9 $price"
                    mAdapter = MyOrdersAdapter(myOrdersList)
                    recyclerView.adapter = mAdapter
                    // progressDialog.dismiss()


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }




}