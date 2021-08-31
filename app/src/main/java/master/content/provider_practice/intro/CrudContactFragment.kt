package master.content.provider_practice.intro

import android.annotation.SuppressLint
import android.content.ContentProviderOperation
import android.content.ContentProviderResult
import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import master.content.provider_practice.MainActivity
import master.content.provider_practice.R
import master.content.provider_practice.TAG
import master.content.provider_practice.databinding.FragmentCrudContactBinding
import master.content.provider_practice.toastDeniedPermission


class CrudContactFragment : Fragment(),
    View.OnClickListener,
    LoaderManager.LoaderCallbacks<Cursor> {


    // binding
    private var _binding: FragmentCrudContactBinding? = null
    private val binding get() = _binding!!

    // crud
    private var mColumnProjection = arrayOf(
        ContactsContract.Contacts._ID,
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts.CONTACT_STATUS,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

    private var firstTimeLoaded: Boolean = false
    private var recentOpPerfomed: Int = 0
    private val LOAD_CONTACTS = 1
    private val WRITE_CONTACTS = 2
    private val UPDATE_CONTACTS = 3
    private val DELETE_CONTACTS = 4


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCrudContactBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        binding.buttonLoadData.setOnClickListener(this)
        binding.buttonAddContact.setOnClickListener(this)
        binding.buttonRemoveContact.setOnClickListener(this)
        binding.buttonUpdateContact.setOnClickListener(this)

    }

    /*
    *
    *  impl LoaderManager
    *
    * */

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return if (id == 1)
            CursorLoader(
                requireContext(),
                ContactsContract.Contacts.CONTENT_URI,
                mColumnProjection, null, null, null
            )
        else CursorLoader(requireContext()) // return null

    }

    @SuppressLint("SetTextI18n")
    override fun onLoadFinished(loader: Loader<Cursor>, cursor: Cursor?) {
        if (cursor != null && cursor.count > 0) {
            val stringBuilderQueryResult = StringBuilder("")
            while (cursor.moveToNext()) {
                stringBuilderQueryResult.append(
                    "- Name { " + cursor.getString(0) + "\t" + cursor.getString(1) + " }\n"
                )
            }
            binding.textViewQueryResult.text = stringBuilderQueryResult.toString()
        } else {
            binding.textViewQueryResult.text = "No Contacts in device"
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {

    }


    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.buttonLoadData -> loadContacts()
            R.id.buttonRemoveContact -> deleteContact()
            R.id.buttonUpdateContact -> updateContact()
            R.id.buttonAddContact -> addContact()

        }
    }

    /*
    *
    * process
    * */

    private fun deleteContact() {
        if (!(activity as MainActivity).hasReadWriteContactsPermission())
            toastDeniedPermission(requireContext())
        else {
            Log.d(TAG, "has permission -> deleting Contact ..")
            val whereClause =
                ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY + " = '" + binding.editTextContactName.getText()
                    .toString() + "'"
            /*
            DELETE FROM <table_name> where column1 = selection_value
            */
            requireContext().contentResolver.delete(
                ContactsContract.RawContacts.CONTENT_URI,
                whereClause,
                null
            )
        }
    }

    private fun addContact() {
        if (!(activity as MainActivity).hasReadWriteContactsPermission())
            toastDeniedPermission(requireContext())
        else {
            Log.d(TAG, "has permission -> adding Contact .. ")

            val newName: String = binding.editTextContactName.text.toString()

            if (newName != "" && newName.isNotEmpty()) {
                val cops = ArrayList<ContentProviderOperation>()

                cops.add(
                    ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(
                            ContactsContract.RawContacts.ACCOUNT_TYPE,
                            "accountname2@gmail.com"
                        )
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, "com.google")
                        .build()
                )


                cops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            binding.editTextContactName.text.toString()
                        )
                        .build()
                )



                try {
                    requireContext().contentResolver.applyBatch(ContactsContract.AUTHORITY, cops)
                } catch (exception: Exception) {
                    Log.d(TAG, exception.message!!)
                    Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show();

                }
            }

        }
    }

    private fun updateContact() {
        if (!(activity as MainActivity).hasReadWriteContactsPermission())
            toastDeniedPermission(requireContext())
        else {
            Log.d(TAG, "has permission -> updating Contact .. ")
            val updateValue: List<String> =
                binding.editTextContactName.text.toString().split(" ")
            val result: Array<ContentProviderResult>? = null

            val targetString: String?
            val newString: String?

            if (updateValue.size == 2) {
                targetString = updateValue[0]
                newString = updateValue[1]


                if (newString != "" && newString.isNotEmpty()) {
                    val where = ContactsContract.RawContacts._ID + " = ? "
                    val params = arrayOf(targetString)
                    val contentResolver: ContentResolver = requireContext().contentResolver
                    val contentValues = ContentValues()
                    contentValues.put(
                        ContactsContract.RawContacts.DISPLAY_NAME_PRIMARY,
                        newString
                    )
                    // UPDATE <table_name> SET column1 = value1, column2 = value2 where column3 = selection_value
                    contentResolver.update(
                        ContactsContract.RawContacts.CONTENT_URI,
                        contentValues,
                        where,
                        params
                    )
                }
            }
        }
    }

    private fun loadContacts() {
        if (!firstTimeLoaded) {
            loaderManager.initLoader(1, null, this)
            firstTimeLoaded = true
        } else
            loaderManager.restartLoader(1, null, this)

    }


}
