package edu.skku.cs.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val ettID = findViewById<EditText>(R.id.ettID)
        val ettPWD = findViewById<EditText>(R.id.ettPWD)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val id = ettID.text.toString()
            val pw = ettPWD.text.toString()
            if(id==null || pw==null){
                val myToast = makeText(this.applicationContext, "Please Enter Correctly", Toast.LENGTH_SHORT)
                myToast.show()
                ettID.text.clear()
                ettPWD.text.clear()
                return@setOnClickListener
            }


            var userId: String? = null
            var userName: String? = null
            ServerClient.getUser(id, pw, object : Callback {
                override fun onFailure(call: Call, e: IOException) {

                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.code == 404){
                        runOnUiThread {
                            //textView.text = "User not found or incorrect password"
                            Toast.makeText(this@LoginActivity, "User not found or incorrect password", Toast.LENGTH_SHORT).show()
                        }
                    }
                    response.body?.let { responseBody ->
                        val res = responseBody.string()
                        val jsonObject = JSONObject(res)
                        val user = UserDTO(
                            id = jsonObject.getString("id"),
                            name = jsonObject.getString("name")
                        )

                        Log.i("testFlask", user.id)
                        Log.i("testFlask", user.name)

                        userId = user.id
                        userName = user.name

                        runOnUiThread {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                putExtra("USER_ID", user.id)
                                putExtra("USER_NAME", user.name)
                            }
                            startActivity(intent)
                        }
                    }
                }
            })
        }
    }
}