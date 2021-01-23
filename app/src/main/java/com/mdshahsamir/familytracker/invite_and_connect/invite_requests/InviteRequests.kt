package com.mdshahsamir.familytracker.invite_and_connect.invite_requests

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.mdshahsamir.familytracker.R
import com.mdshahsamir.familytracker.data_models.InvitationDataModel
import com.mdshahsamir.familytracker.databinding.FragmentInviteRequestsBinding
import java.util.*
import kotlin.collections.ArrayList

class InviteRequests : Fragment() {


    private lateinit var requestListAdapter: RequestListAdapter
    private lateinit var binding: FragmentInviteRequestsBinding
    private var invitationList: ArrayList<InvitationDataModel> = ArrayList()

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invite_requests, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestListAdapter = RequestListAdapter()

        binding.inviteRequestList.adapter = requestListAdapter
        binding.inviteRequestList.layoutManager = LinearLayoutManager(requireContext(),RecyclerView.VERTICAL, false)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference
        database.child("invitations").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                invitationList.clear()
                snapshot.children.forEach {
                    val invitation = it.getValue<InvitationDataModel>()
                    if (invitation != null && invitation.receiver == auth.currentUser?.email) {
                       invitationList.add(invitation)
                        Log.i("invitaions", invitation.toString())
                    }
                }
                requestListAdapter.submitList(invitationList)
                requestListAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


}