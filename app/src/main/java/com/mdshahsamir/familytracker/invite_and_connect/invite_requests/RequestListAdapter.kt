package com.mdshahsamir.familytracker.invite_and_connect.invite_requests

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mdshahsamir.familytracker.data_models.InvitationDataModel
import com.mdshahsamir.familytracker.databinding.InviteReqItemBinding


class RequestListAdapter(var requests: ArrayList<DatabaseReference>) : ListAdapter<InvitationDataModel, RequestListAdapter.InviteViewHolder>(RequestListDiffCallback()){



    class InviteViewHolder(var binding: InviteReqItemBinding) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var database: DatabaseReference

        fun bind(inviteRequest: InvitationDataModel, databaseReference: DatabaseReference) {
            binding.sender.text = inviteRequest.sender
            binding.acceptButton.setOnClickListener {
                databaseReference.removeValue()
                saveInConnections(inviteRequest.senderUid)
            }
            binding.ignoreButton.setOnClickListener {
                databaseReference.removeValue()
            }
        }

        private fun saveInConnections(senderUid: String){
            database = Firebase.database.reference
            database.child("connections").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(senderUid)
            database.child("connections").child(senderUid).setValue(FirebaseAuth.getInstance().currentUser?.uid)
        }

        companion object{
            fun from(parent: ViewGroup): InviteViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = InviteReqItemBinding.inflate(inflater, parent, false)

                return InviteViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InviteViewHolder {
        return InviteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: InviteViewHolder, position: Int) {
        holder.bind(getItem(position), requests[position])
    }
}

class RequestListDiffCallback : DiffUtil.ItemCallback<InvitationDataModel>() {
    override fun areItemsTheSame(
        oldItem: InvitationDataModel,
        newItem: InvitationDataModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: InvitationDataModel,
        newItem: InvitationDataModel
    ): Boolean {
        return oldItem.status == newItem.status &&
                oldItem.senderUid == newItem.senderUid &&
                oldItem.sender == newItem.sender

    }

}
