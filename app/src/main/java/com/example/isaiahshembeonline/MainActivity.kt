package com.example.isaiahshembeonline

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.credentials.IdentityProviders
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var providers: List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int=7117  // ANY NUMBER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //INIT
        providers = Arrays.asList<AuthUI.IdpConfig>(

            AuthUI.IdpConfig.EmailBuilder().build(),//email Login
            AuthUI.IdpConfig.FacebookBuilder().build(),//facebook Login
            AuthUI.IdpConfig.GoogleBuilder().build(),//google Login
            AuthUI.IdpConfig.PhoneBuilder().build()//phone Login
        )


        showSignInOptions()
        //event
        btn_Sign_Out.setOnClickListener {
            //signout

            //sign Out
            AuthUI.getInstance().signOut(this@MainActivity)
                .addOnCompleteListener {

                    btn_Sign_Out.isEnabled=false
                    showSignInOptions()
                }

                .addOnFailureListener {
                        e-> Toast.makeText(this@MainActivity, e.message,Toast.LENGTH_SHORT).show()
                }
        }



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==MY_REQUEST_CODE)
        {
            val response= IdpResponse.fromResultIntent(data)
            if(resultCode==Activity.RESULT_OK)
            {
                val user = FirebaseAuth.getInstance().currentUser // get current user

                Toast.makeText(this,""+user!!.email,Toast.LENGTH_SHORT).show()
                btn_Sign_Out.isEnabled=true

            }
            else{
                Toast.makeText(this,""+response!!.error!!.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions() {
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setTheme(R.style.MyTheme)
            .build(),MY_REQUEST_CODE)
    }
}
