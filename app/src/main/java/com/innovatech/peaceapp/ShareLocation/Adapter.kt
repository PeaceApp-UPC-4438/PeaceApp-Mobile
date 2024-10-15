package com.innovatech.peaceapp.ShareLocation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.ShareLocation.Beans.Contact
import com.innovatech.peaceapp.ShareLocation.ViewHolder.ContactViewHolder

class Adapter(var contactList:List<Contact>): RecyclerView.Adapter<ContactViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ContactViewHolder(layoutInflater.inflate(R.layout.card_contact, parent, false))
    }

    override fun getItemCount(): Int = contactList.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item=contactList[position]
        holder.render(item)
    }
    fun updateContacts(newContactList: List<Contact>){
        contactList = newContactList
        notifyDataSetChanged()
    }

}