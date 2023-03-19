package com.example.e_commerce.ui.deals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.adapter.DealAdapter
import com.example.e_commerce.data.DealItem
import com.example.e_commerce.data.PrefManager


class DealsFragment : Fragment(),DealAdapter.OnItemClick {
        private lateinit var mAdapter:DealAdapter
        private val list:ArrayList<DealItem> = ArrayList()
    private lateinit var recyclerView:RecyclerView
    lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefManager= PrefManager(requireContext())

        val mView= inflater.inflate(R.layout.fragment_deals, container, false)


        recyclerView = mView.findViewById(R.id.deals_recycler_view)
        mAdapter = DealAdapter(this)
        recyclerView.adapter = mAdapter
        mAdapter.updateList(list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(),2)
        dealData()

        return mView
    }

    private fun dealData(){
        list.add(DealItem(R.drawable.mobilephones,"Redmi Note 11", 14999))
        list.add(DealItem(R.drawable.tab,"Samsung Tab",21000))
        list.add(DealItem(R.drawable.topdeals3,"Treadmill",25000))
        list.add(DealItem(R.drawable.topdeals4,"Travel Bag",1200))

    }



    override fun onItemClick(position: Int) {
        if (prefManager.isLogin()) {
            val fragment = DetailedItemFragment()
            val bundle = Bundle()

            bundle.putString("image", list[position].image.toString())
            bundle.putString("name", list[position].name)
            bundle.putString("price", list[position].price.toString())
            fragment.arguments = bundle
            loadFragment(fragment)
        }else{
            loadFragment(DetailedItemFragment())
        }

    }
    private fun loadFragment(fragment: Fragment) {
        val manager = (requireContext() as AppCompatActivity).supportFragmentManager
        manager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
    }


}