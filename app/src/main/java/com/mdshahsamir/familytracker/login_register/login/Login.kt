package com.mdshahsamir.familytracker.login_register.login

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.mdshahsamir.familytracker.R
import com.mdshahsamir.familytracker.databinding.LoginFragmentBinding

class Login : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        Firebase.initialize(requireContext())
        loginPageGIFAnimation()
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            findNavController().navigate(LoginDirections.actionLoginToMapsFragment())
        }


        binding.loginButton.setOnClickListener {
            if (!binding.username.text.isNullOrEmpty() && !binding.password.text.isNullOrEmpty()){
                try {
                    auth.signInWithEmailAndPassword(binding.username.text.toString().trim(), binding.password.text.toString().trim())
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Snackbar.make(binding.root,"Login Successful", Snackbar.LENGTH_LONG).show()
                                it.findNavController().navigate(LoginDirections.actionLoginToMapsFragment())
                            } else {
                                Log.e("Error :", task.exception.toString())
                            }
                        }.addOnFailureListener {
                            it.printStackTrace()
                                Snackbar.make(binding.root,"Login Failed", Snackbar.LENGTH_LONG).show()
                            }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        binding.createAccountText.setOnClickListener {
            it.findNavController().navigate(LoginDirections.actionLoginToRegisterUser())
        }
    }

    private fun loginPageGIFAnimation(){
        Glide.with(requireContext()).asGif().load(R.drawable.login_page_gif_animation).listener(object :
            RequestListener<GifDrawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }
        }).into(binding.gifAnimation)
    }

}