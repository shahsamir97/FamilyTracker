package com.mdshahsamir.familytracker.login_register.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mdshahsamir.familytracker.R
import com.mdshahsamir.familytracker.databinding.LoginFragmentBinding

class Login : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            findNavController().navigate(R.id.mapsFragment)
        }

        binding.loginButton.setOnClickListener {
            if (!binding.username.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()){
                auth.signInWithEmailAndPassword(binding.username.text.toString().trim(), binding.password.text.toString().trim())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            it.findNavController().navigate(R.id.mapsFragment)
                        } else {
                            Log.e("Error :", task.exception.toString())
                        }
                    }.addOnFailureListener {
                        it.printStackTrace()
                        }
            }
        }

        binding.createAccountText.setOnClickListener {
            it.findNavController().navigate(R.id.registerUser)
        }
    }

}