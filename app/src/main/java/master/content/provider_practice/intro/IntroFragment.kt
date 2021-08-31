package master.content.provider_practice.intro

import android.R
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import master.content.provider_practice.MainActivity
import master.content.provider_practice.databinding.FragmentIntroBinding
import android.widget.ArrayAdapter


class IntroFragment : Fragment() {

    // binding
    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        super.onStart()

        if ((activity as MainActivity).hasReadWriteContactsPermission())
            setUpListView()
        // showContacts()


    }

    @SuppressLint("Recycle", "SetTextI18n")
    private fun showContacts() {
        val mColumnProjection = arrayOf(
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
            ContactsContract.Contacts.CONTACT_STATUS,
            ContactsContract.Contacts.HAS_PHONE_NUMBER
        )

        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            mColumnProjection,
            null,
            null,
            null
        )
        if (cursor != null && cursor.count > 0) {
            val stringBuilderQueryResult = StringBuilder("")
            while (cursor.moveToNext()) {

/*
                stringBuilderQueryResult.append(
                    """${cursor.getString(0)} , ${cursor.getString(1)} , ${cursor.getString(2)}"""
                            + "\n"
                )
*/
                stringBuilderQueryResult.append(
                    " - Name { ${cursor.getString(0)} }\n"
                )
            }
            binding.textViewQueryResult.text = stringBuilderQueryResult.toString()
        } else {
            binding.textViewQueryResult.text = "No Contacts in device!"
        }
    }

    private fun getContactNames(): List<String> {
        val contacts: MutableList<String> = ArrayList()
        val cr: ContentResolver = requireContext().contentResolver
        val cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null)
        if (cursor!!.moveToFirst()) {
            do {
                val name =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contacts.add("- Name { $name }")
            } while (cursor.moveToNext())
        }
        cursor.close()
        return contacts
    }

    private fun setUpListView() {
        val contacts = getContactNames()
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), R.layout.simple_list_item_1, contacts)
        binding.listView.adapter = adapter
    }
}
