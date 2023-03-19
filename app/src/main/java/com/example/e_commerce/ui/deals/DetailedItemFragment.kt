package com.example.e_commerce.ui.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e_commerce.R
import com.example.e_commerce.data.DealItem
import com.example.e_commerce.data.PrefManager
import com.example.e_commerce.ui.cart.CartFragment
import com.example.e_commerce.ui.home.HomeFragment
import com.example.e_commerce.ui.menu.MenuFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class DetailedItemFragment : Fragment() {

    lateinit var prefManager: PrefManager



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager = PrefManager(requireContext())

        val mView = inflater.inflate(R.layout.fragment_detailed_item, container, false)
        if (prefManager.isLogin()) {
            val currentUser = FirebaseAuth.getInstance().currentUser

            val image = arguments?.getString("image")
            val name = arguments?.getString("name")
            val price = arguments?.getString("price")

            val itemImage: ImageView = mView.findViewById(R.id.detailed_item_image)
            itemImage.setImageResource(image!!.toInt())

            val itemName: TextView = mView.findViewById(R.id.detailed_item_name)
            itemName.text = name

            val itemPrice: TextView = mView.findViewById(R.id.detailed_item_price)
            itemPrice.text = "\u20B9 $price"

            val databaseReference = FirebaseDatabase.getInstance().reference.child("Users")
                .child(currentUser!!.displayName!!)


            val addToCart: Button = mView.findViewById(R.id.detailed_add_to_cart)
            addToCart.setOnClickListener {

                //if (prefManager.isLogin()) {
                val data = DealItem(image.toInt(), name!!, price!!.toInt())
                databaseReference.child("Cart Item").child(name).setValue(data)
                    .addOnSuccessListener {
                        Toast.makeText(mView.context, "Added To cart", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(mView.context, "Please Try Again", Toast.LENGTH_SHORT).show()
                    }


                val fragment = CartFragment()
                loadFragment(fragment)
                //  }else{
                //     loadFragment(HomeFragment())
            }
            //  }

            val buyNowButton: Button = mView.findViewById(R.id.buyNow)
            buyNowButton.setOnClickListener {
                val data = DealItem(image.toInt(), name!!, price!!.toInt())
                databaseReference.child("Cart Item").child(name).setValue(data)
                    .addOnSuccessListener {
                        Toast.makeText(mView.context, "Added To cart", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(mView.context, "Please Try Again", Toast.LENGTH_SHORT).show()
                    }


                val fragment = CartFragment()
                loadFragment(fragment)
            }
        }else{
            loadFragment(MenuFragment())
        }





        return mView
    }
    private fun loadFragment(fragment: Fragment) {
        val manager = (requireContext() as AppCompatActivity).supportFragmentManager
        manager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    }

    companion object {

    }
}