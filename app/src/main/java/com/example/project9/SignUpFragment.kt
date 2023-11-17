package com.example.project9

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth


class SignUpFragment : Fragment() {
    private val TAG = "LoginFragment"

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        val auth = FirebaseAuth.getInstance()

        val btnSignUp = view.findViewById<Button>(R.id.nextBtn)
        val loginBtn = view.findViewById<TextView>(R.id.textViewSignIn)

        // on click listeners to sign up and navigate login screen
        loginBtn.setOnClickListener{
            goToLoginScreen()
        }

        btnSignUp.setOnClickListener {
            btnSignUp.isEnabled = false
            val etEmail = view.findViewById<TextView>(R.id.emailEt)
            val etPassword = view.findViewById<TextView>(R.id.passEt)
            val verifyEtPassword = view.findViewById<TextView>(R.id.verifyPassEt)
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            val verifyPassword = verifyEtPassword.text.toString()
            if (email.isBlank() || password.isBlank() || verifyPassword.isBlank()) {
                Toast.makeText(this.context, "Email/password/verify-password cannot be empty", Toast.LENGTH_SHORT).show()
                btnSignUp.isEnabled = true
                return@setOnClickListener
            }
            // Firebase authentication check
            else{
                if(password == verifyPassword){
                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this.context, "Account Created Successfully!", Toast.LENGTH_SHORT).show()
                            goToLoginScreen()
                        }
                        else{
                            Log.e(TAG, "createUser failed", task.exception)
                            Toast.makeText(this.context, "Create User Failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Log.e(TAG, "Password != Verify Password")
                    Toast.makeText(this.context, "Password != Verify Password", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return view
    }

    private fun goToLoginScreen() {
        this.findNavController().navigate(R.id.action_signUpFragment_to_loginFragment)
    }
}