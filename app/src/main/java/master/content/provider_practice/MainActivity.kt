package master.content.provider_practice

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import master.content.provider_practice.databinding.ActivityMainBinding


var PERMISSIONS_REQUEST_READ_CONTACTS: Int = 20

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (!isReadContactPermissionGranted())
            requestPermission()

    }

    fun isReadContactPermissionGranted(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this, "" +
                            "permission granted", Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Until you grant the permission, we cannot display the names",
                    Toast.LENGTH_SHORT
                ).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /*
    * Content Provider !
    * share data between apps
    *


      InterProcess Communication
      data Request  ->  Content Provider
      data Response ->  Cursor

    * */


}