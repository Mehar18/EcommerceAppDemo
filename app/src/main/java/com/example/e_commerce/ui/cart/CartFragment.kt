package com.example.e_commerce.ui.cart

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.adapter.MyCartAdapter
import com.example.e_commerce.data.DealItem
import com.example.e_commerce.data.PrefManager
import com.example.e_commerce.ui.home.HomeFragment
import com.example.e_commerce.ui.menu.MenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartFragment : Fragment() {
    private  lateinit var progressDialog: ProgressDialog
    private val list: ArrayList<DealItem> = ArrayList()
    private val list2: ArrayList<DealItem> = ArrayList()
    val TAG = "CartFragment"

    lateinit var mAdapter: MyCartAdapter
    lateinit var priceTV: TextView
    lateinit var recyclerView: RecyclerView
    lateinit var prefManager: PrefManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val mView = inflater.inflate(R.layout.fragment_cart, container, false)
     //   progressDialog = ProgressDialog(requireContext())
       // progressDialog.show()
        priceTV = mView.findViewById(R.id.total_price)
        val placedOrder: Button = mView.findViewById(R.id.place_order)
        placedOrder.setOnClickListener {
            getDataToPlaceOrder()
            loadFragment(HomeFragment())
            val fragment = OrderPlaced()
            loadFragment(fragment)
        }

        recyclerView = mView.findViewById(R.id.cart_recycler_view)
        mAdapter = MyCartAdapter(list)
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(mView.context, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        prefManager = PrefManager(requireContext())

        if (prefManager.isLogin()) {
            getData()
        }else{
//            recyclerView.visibility = View.GONE
//            placedOrder.visibility = View.GONE
            loadFragment(MenuFragment())


        }


        return mView
    }

    private fun getData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users")
                .child(currentUser!!.displayName!!)
                .child("Cart Item")
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
                        list.add(DealItem(itemImage, itemName, itemPrice))
                        // Log.d("list",list.toString())

                    }
                    priceTV.text = "\u20B9 $price"
                    mAdapter = MyCartAdapter(list)
                    recyclerView.adapter = mAdapter
                   // progressDialog.dismiss()


                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun getDataToPlaceOrder() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userRef =
            FirebaseDatabase.getInstance().reference.child("Users").child(currentUser!!.displayName!!)
                .child("Cart Item")
        val userRefPlacedOrder = FirebaseDatabase.getInstance().reference.child("Users").child(
            currentUser.displayName!!
        ).child("My Order")
        val adminReference = FirebaseDatabase.getInstance().getReference("OrdersReceived")



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
                        list2.add(DealItem(itemImage, itemName, itemPrice))

//                    }

//                    for (item in list2) {
//                        Log.d("child",item.name)
                        val itemQuery = userRef.orderByChild(itemName)
                        itemQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (itemSnapshot in snapshot.children) {
                                    // Get the item data
                                    val itemData = itemSnapshot.getValue(DealItem::class.java)
                                    itemSnapshot.ref.removeValue()
                                    userRefPlacedOrder.child(itemData!!.name).setValue(itemData)
                                    userRefPlacedOrder.push()
                                    adminReference.child(currentUser.displayName!!)
                                        .child(itemData.name).setValue(itemData)
                                    // Remove the item from the cart reference

                                    mAdapter.notifyDataSetChanged()

                                    // Add the item to the my order reference

                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, "onCancelled", error.toException())
                            }

                        })
                    }

                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


    }

    private fun loadFragment(fragment: Fragment) {
        val manager = (requireContext() as AppCompatActivity).supportFragmentManager
        manager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }
    }


}