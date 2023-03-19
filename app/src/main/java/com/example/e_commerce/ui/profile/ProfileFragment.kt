package com.example.e_commerce.ui.profile

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.e_commerce.R
import com.example.e_commerce.data.PrefManager
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.ui.cart.CartFragment
import com.example.e_commerce.ui.home.HomeFragment
import com.example.e_commerce.ui.login.ProfileBeforeLogin
import com.example.e_commerce.ui.menu.MenuFragment
import com.example.e_commerce.ui.myorder.MyOrderFragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var prefManager: PrefManager

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // val profileViewModel =
         //   ViewModelProvider(this).get(ProfileViewModel::class.java)




        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        prefManager = PrefManager(requireContext())

        if (prefManager.isLogin()){
            data()
        }else{
            loadFragment(MenuFragment())
        }

//        if (!prefManager.isLogin()){
//            loadFragment(ProfileBeforeLogin())
//        }



//        val currentUser = FirebaseAuth.getInstance().currentUser
//        val userName = currentUser!!.displayName
//        val userEmail = currentUser!!.email
//
//        binding.nameProfile.text = userName
//        binding.emailProfile.text = userEmail

        binding.myCartProfile.setOnClickListener {
            val fragment = CartFragment()
            loadFragment(fragment)
        }

        binding.myOrdersProile.setOnClickListener {
            val fragment = MyOrderFragment()
            loadFragment(fragment)
        }




//        if (prefManager.isLogin()){
//            val currentUser = FirebaseAuth.getInstance().currentUser
////            val userName = currentUser!!.displayName
//            val userEmail = currentUser!!.email
//
//    //        binding.nameProfile.text = userName
//            binding.emailProfile.text = userEmail
//        }



//        if (!prefManager.isLogin()){
//            val progressDialog = ProgressDialog(requireContext())
//            progressDialog.setTitle("Redirecting To login page")
//            progressDialog.show()
//           // startActivity(Intent(requireContext(), LoginActivity::class.java))
//
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun data(){
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userName = currentUser!!.displayName
        val userEmail = currentUser!!.email

        binding.nameProfile.text = userName
        binding.emailProfile.text = userEmail
    }

    private fun loadFragment(fragment: Fragment) {
        val manager = (requireContext() as AppCompatActivity).supportFragmentManager
        manager.beginTransaction().apply {

            replace(R.id.frame_layout, fragment)
            addToBackStack(null)
            commit()
        }
    }
}