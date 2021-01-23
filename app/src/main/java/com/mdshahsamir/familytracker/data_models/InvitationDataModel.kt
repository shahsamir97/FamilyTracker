package com.mdshahsamir.familytracker.data_models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class InvitationDataModel() {
    var sender : String = ""
    var receiver: String = ""
    var status: Boolean = false
    var senderUid = ""


    constructor(sender: String, receiver: String , status: Boolean, senderUid: String) : this() {
        this.sender = sender
        this.receiver = receiver
        this.status = status
        this.senderUid = senderUid
    }

}
