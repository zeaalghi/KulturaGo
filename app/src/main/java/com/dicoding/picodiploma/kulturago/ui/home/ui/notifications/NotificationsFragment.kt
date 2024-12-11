package com.dicoding.picodiploma.kulturago.ui.home.ui.notifications

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.picodiploma.kulturago.databinding.FragmentNotificationsBinding
import com.dicoding.picodiploma.kulturago.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)


        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val aboutUsTextView: TextView? = binding.textAboutUs

        val logOutButton: Button = binding.buttonLogOut

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            clearLoginState()
            navigateToLoginPage()
        }

        return root
    }

    private fun clearLoginState() {
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun navigateToLoginPage() {
        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
        val intent = Intent(
            requireContext(),
            LoginActivity::class.java
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
