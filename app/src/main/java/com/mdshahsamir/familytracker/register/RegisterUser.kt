package com.mdshahsamir.familytracker.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.mdshahsamir.familytracker.R
import com.mdshahsamir.familytracker.databinding.RegisterUserFragmentBinding

class RegisterUser : Fragment() {

    private lateinit var viewModel: RegisterUserViewModel
    private lateinit var binding: RegisterUserFragmentBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_user_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(RegisterUserViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            if (binding.username.text.toString().isNotEmpty() && binding.password.text.toString().isNotEmpty()){
                auth.createUserWithEmailAndPassword(binding.username.text.toString().trim(), binding.password.text.toString().trim())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                           updateUserProfile()
                        } else {
                            // Sign in failed
                            Toast.makeText(requireContext(),
                                    "Failed to Sign up! Something went wrong! Try  again please",
                                    Toast.LENGTH_SHORT).show()
                        }

                        // ...
                    }
            }
        }
    }

    private fun updateUserProfile(){
        val profileChangeRequest = UserProfileChangeRequest.Builder()
                .setDisplayName(binding.displayName.text.toString())
                .build()
        FirebaseAuth.getInstance()
                .currentUser?.updateProfile(profileChangeRequest)?.addOnSuccessListener {
            findNavController().navigate(RegisterUserDirections.actionRegisterUserToMapsFragment())
        }
    }

}