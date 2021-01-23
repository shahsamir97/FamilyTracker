package com.mdshahsamir.familytracker.invite_and_connect.invite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import com.mdshahsamir.familytracker.R
import com.mdshahsamir.familytracker.data_models.InvitationDataModel
import com.mdshahsamir.familytracker.databinding.FragmentInviteMembersBinding

class InviteMembers : Fragment() {

    private lateinit var binding: FragmentInviteMembersBinding
    private lateinit var database: DatabaseReference
    private lateinit var currentUser: FirebaseUser
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite_members, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        currentUser = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance().reference

        binding.inviteButton.setOnClickListener {
            val emailText = binding.email.text.toString().trim()
            if (emailText.isNotEmpty()){
             sendInvitation(emailText)
            }
        }
    }


    private fun sendInvitation(emailText: String){
        val invitation = InvitationDataModel(sender = currentUser.email!!,
            receiver = emailText,
            status = false,
            senderUid = currentUser.uid)
        database.child("invitations").push().setValue(invitation).addOnSuccessListener {
            Toast.makeText(requireContext(), "Invitation Sent", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Invitation Failed", Toast.LENGTH_SHORT).show()
        }
    }


}