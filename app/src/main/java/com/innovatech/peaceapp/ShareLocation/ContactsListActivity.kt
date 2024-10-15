package com.innovatech.peaceapp.ShareLocation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.innovatech.peaceapp.R
import com.innovatech.peaceapp.ShareLocation.Beans.Contact

class ContactsListActivity : AppCompatActivity() {

    //private lateinit var btnSearch: ImageView
    private lateinit var etSearch: EditText
    private lateinit var btnSaveChanges: Button
    private lateinit var rvContacts: RecyclerView
    private lateinit var contactAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_contacts_list)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        askForPermissions()
        initComponents()
        // Configuración inicial del RecyclerView y Adapter
        val contactList = loadContacts()
        contactAdapter = Adapter(contactList)
        rvContacts.layoutManager = LinearLayoutManager(applicationContext)
        rvContacts.adapter = contactAdapter

        initListeners()
        editTextListeners(contactList)

    }

    private fun askForPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS),
                1001)
        }
    }

    private fun initComponents() {
        //btnSearch = findViewById(R.id.ivSearch)
        etSearch = findViewById(R.id.etSearch)
        btnSaveChanges = findViewById(R.id.btnSaveChanges)
        rvContacts = findViewById(R.id.rv_contacts)
    }
    private fun initListeners(){
//        btnSearch.setOnClickListener(){
//            showGeneratedUrlDialog(generateRandomString())
//        }
    }

    @SuppressLint("Range")
    private fun loadContacts(): List<Contact> {
        val contactList = mutableListOf<Contact>()
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {
            while (it.moveToNext()) {
                var name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone
                    .DISPLAY_NAME))
                var phoneNumber = it.getString(it.getColumnIndex(ContactsContract
                    .CommonDataKinds.Phone.NUMBER))
                Log.i("Contact", "Name: $name - Phone: $phoneNumber - ${it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))}")

                var image = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone
                    .PHOTO_URI))
                if(image == null) { // asignar imagen por defecto
                    image = "https://static.vecteezy.com/system/resources/thumbnails/009/292/244/small/default-avatar-icon-of-social-media-user-vector.jpg"
                }
                var position = contactList.size

                var contact = Contact(name, phoneNumber, image)
                contactList.add(contact)
            }
        }

        return contactList
    }


    private fun editTextListeners(contactList: List<Contact>) {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {  }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterContacts(contactList, s.toString())
            }

            override fun afterTextChanged(s: Editable?) { }

        })
    }

    private fun filterContacts(contactList: List<Contact>, search: String) {
        val filteredList = contactList.filter {
            it.name.contains(search, ignoreCase = true) || it.phone.contains(search)
        }

        contactAdapter.updateContacts(filteredList)
    }




    private fun generateRandomString() : String {
        val length = 6
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

    private fun showGeneratedUrlDialog(url:String) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_generated_url)

        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnCopy = dialog.findViewById<CardView>(R.id.cvCopyContainer)
        val btnContinue = dialog.findViewById<Button>(R.id.btnContinue)
        val tvUrlGenerada = dialog.findViewById<TextView>(R.id.tvGeneratedUrl)

        tvUrlGenerada.text = "http://www.peaceapp.com/$url"

        btnContinue.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }


}