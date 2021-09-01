package master.content.provider_practice

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import master.content.provider_practice.databinding.ActivityMainBinding


var TAG = "debug_tag"

fun toastDeniedPermission(context: Context) {
    Toast.makeText(context, "Permission Denied ..", Toast.LENGTH_SHORT).show()
}

fun toastGrantedPermission(context: Context) {
    Toast.makeText(context, "Permission Granted ..", Toast.LENGTH_SHORT).show()
}


const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1
val listPermissionsNeeded: ArrayList<String> = ArrayList()

class MainActivity : AppCompatActivity() {

    // binding
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // setNavHost
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)
                    as NavHostFragment
        navController = navHostFragment.findNavController()
        binding.bottomNavigationView.setupWithNavController(navController)


        if (!hasReadWriteContactsPermission())
            requestPermission()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {

            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                Log.d(TAG, "requestPermission ..")

                val perms: MutableMap<String, Int> = HashMap()
                perms[Manifest.permission.READ_CONTACTS] = PackageManager.PERMISSION_GRANTED
                perms[Manifest.permission.WRITE_CONTACTS] = PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    var i = 0
                    while (i < permissions.size) {
                        perms[permissions[i]] = grantResults[i]
                        i++
                    }

                    if (perms[Manifest.permission.READ_CONTACTS] == PackageManager.PERMISSION_GRANTED
                        && perms[Manifest.permission.WRITE_CONTACTS] == PackageManager.PERMISSION_GRANTED
                    )
                        Log.d(TAG, "onRequestPermissionsResult: granted ..")
                    else
                        Log.d(TAG, "onRequestPermissionsResult: denied ..")

                } else Log.d(TAG, "onRequestPermissionsResult: grantResults is Empty")
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            listPermissionsNeeded.toTypedArray(),
            REQUEST_ID_MULTIPLE_PERMISSIONS
        )
    }


     fun hasReadWriteContactsPermission(): Boolean {
        val permissionReadContacts: Int = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_CONTACTS
        )
        val permissionWriteContacts =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_CONTACTS
            )

        if (permissionWriteContacts != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS)

        if (permissionReadContacts != PackageManager.PERMISSION_GRANTED)
            listPermissionsNeeded.add(Manifest.permission.WRITE_CONTACTS)

        if (listPermissionsNeeded.isNotEmpty())
            return false

        return true
    }


}
/*
* Content Provider !
* share data between apps
*


  InterProcess Communication
  data Request  ->  Content Provider
  data Response ->  Cursor

* */

/*
*   Expose App Data  ! ContentProvider
*   - data primaryKey
*   - implement ContentProvider
*   - define Proper Content-urls for data to be exposed !
*
*
*     Uri Constants
*   - Content - Authority - Path
*
* */