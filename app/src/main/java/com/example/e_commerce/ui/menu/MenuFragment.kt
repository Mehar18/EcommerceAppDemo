package com.example.e_commerce.ui.menu

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
import com.example.e_commerce.databinding.FragmentMenuBinding
import com.example.e_commerce.databinding.FragmentProfileBinding
import com.example.e_commerce.ui.home.HomeFragment
import com.example.e_commerce.ui.login.ProfileBeforeLogin
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    lateinit var prefManager: PrefManager
    val firebaseAuth = FirebaseAuth.getInstance()



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val profileViewModel =
            ViewModelProvider(this).get(MenuViewModel::class.java)
            val user = firebaseAuth.currentUser
        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        val root: View = binding.root
        prefManager = PrefManager(root.context)

        binding.logOut.setOnClickListener {
            firebaseAuth.signOut()
            prefManager.setLogin(false)
            loadFragment(HomeFragment())


        }
        if (prefManager.isLogin()) {
            binding.usernameCurrent.text = user!!.displayName
        }else{
//            binding.logOut.text = "Not Login Yet"
            loadFragment(ProfileBeforeLogin())
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun loadFragment(fragment: Fragment) {
        val manager = (requireContext() as AppCompatActivity).supportFragmentManager
        manager.beginTransaction().apply {
            replace(R.id.frame_layout,fragment)
            addToBackStack(null)
            commit()
        }
    }
}