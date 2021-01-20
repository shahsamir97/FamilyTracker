package com.mdshahsamir.familytracker.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
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
            if (!binding.username.text.toString().isNullOrEmpty() && !binding.password.text.toString().isNullOrEmpty()){
                auth.createUserWithEmailAndPassword(binding.username.text.toString(), binding.password.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                        } else {
                            // If sign in fails, display a message to the user.

                        }

                        // ...
                    }
            }
        }
    }

}