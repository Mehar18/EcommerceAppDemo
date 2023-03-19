package com.example.e_commerce.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.e_commerce.R
import com.example.e_commerce.adapter.DealOfTheDayAdapter
import com.example.e_commerce.adapter.ItemRecyclerAdapter
import com.example.e_commerce.adapter.TopDealAdapter
import com.example.e_commerce.data.DealsOfTheDayData
import com.example.e_commerce.data.ItemListDataClass
import com.example.e_commerce.data.PrefManager
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.ui.deals.DealsFragment
import com.example.e_commerce.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment(),DealOfTheDayAdapter.OnItemClick {

    private var _binding: FragmentHomeBinding? = null
    private val list: ArrayList<ItemListDataClass> = ArrayList()
    private val listDeals: ArrayList<DealsOfTheDayData> = ArrayList()
    private val topDeals:ArrayList<DealsOfTheDayData> = ArrayList()
    private lateinit var prefManager: PrefManager



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var imageIds =
        intArrayOf(R.drawable.viewpager1, R.drawable.viewpager2, R.drawable.viewpager3)
    private var currentPage = 0
    private val handler = Handler(Looper.getMainLooper())

private val autoSlide: Runnable = object : Runnable {
    override fun run() {
        if (currentPage == imageIds.size) {
            currentPage = 0
        }
        binding.viewPager.setCurrentItem(currentPage++, false)
        handler.postDelayed(this, 1000)
    }
}




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        data()
        binding.itemRecyclerView.adapter = ItemRecyclerAdapter(list)
        binding.itemRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        binding.itemRecyclerView.layoutManager = GridLayoutManager(requireContext(),4)
        binding.itemRecyclerView.setHasFixedSize(true)
        Log.d("list", list.toString())

        dataDeals()
        binding.dealOfTheDayRecyclerview.adapter = DealOfTheDayAdapter(listDeals,this)
        binding.dealOfTheDayRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dealOfTheDayRecyclerview.setHasFixedSize(true)
        Log.d("list", list.toString())

        topDeals()
        binding.topDealsRecyclerView.adapter = TopDealAdapter(topDeals)
        binding.topDealsRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 2)
        binding.topDealsRecyclerView.setHasFixedSize(true)
        prefManager = PrefManager(requireContext())

        if (prefManager.isLogin()) {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userName = currentUser!!.displayName
           binding.deliverToName.text = getString(R.string.deliver, userName)
        }




        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = SlideShowPagerAdapter()
        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(autoSlide, 1000)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(autoSlide)
    }

    private fun data() {
        list.add(ItemListDataClass(R.drawable.mobile, "Mobile"))
        list.add(ItemListDataClass(R.drawable.fashion, "Fashion"))
        list.add(ItemListDataClass(R.drawable.electronics, "Electronics"))
        list.add(ItemListDataClass(R.drawable.accessories, "Accessories"))
        list.add(ItemListDataClass(R.drawable.mobile, "Mobile"))
        list.add(ItemListDataClass(R.drawable.fashion, "Fashion"))
        list.add(ItemListDataClass(R.drawable.electronics, "Electronics"))
        list.add(ItemListDataClass(R.drawable.accessories, "Accessories"))

    }
    private fun dataDeals() {
        listDeals.add(DealsOfTheDayData(R.drawable.buds, "Oneplus Audio Days"))
        listDeals.add(DealsOfTheDayData(R.drawable.tab, "Best selling tablets from Samsung, Apple on"))
        listDeals.add(DealsOfTheDayData(R.drawable.mobilephones, "Best selling Mobile"))
        listDeals.add(DealsOfTheDayData(R.drawable.electronicaccessories, "Handpicked high performance"))

    }

    private fun topDeals(){
        topDeals.add(DealsOfTheDayData(R.drawable.topdeals1,"Home & Kitchen"))
        topDeals.add(DealsOfTheDayData(R.drawable.topdeals2,"Clothing & Accessories"))
        topDeals.add(DealsOfTheDayData(R.drawable.topdeals3,"Gym & fitness"))
        topDeals.add(DealsOfTheDayData(R.drawable.topdeals4,"Fashion & Bags"))

    }

    private inner class SlideShowPagerAdapter : PagerAdapter() {
        override fun getCount(): Int {
            return imageIds.size
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val imageView = ImageView(activity)
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.setImageResource(imageIds[position])
            container.addView(imageView)

            return imageView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as ImageView)
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

    }
    private fun loadFragment(fragment: Fragment) {
            val manager = (requireContext() as AppCompatActivity).supportFragmentManager
        manager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            commit()
        }
        }


    override fun onClickItem(position: Int) {
            val fragment = DealsFragment()
        loadFragment(fragment)
    }
}