package com.example.furniture.presentation.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.furniture.R
import com.example.furniture.data.api.SessionManager
import com.example.furniture.data.util.Resource
import com.example.furniture.databinding.FragmentLoginBinding
import com.facebook.*
import com.facebook.internal.ImageRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class LoginFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: LoginViewModelFactory

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding
    private lateinit var sessionManager: SessionManager
    private lateinit var callbackManager: CallbackManager
    private lateinit var mAuth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        sessionManager = SessionManager(requireContext())
        sessionManager.setView(requireActivity())

        binding.apply {

            btnLogin.setOnClickListener {
                login()
            }

            TIEPhoneEmailLogin.addTextChangedListener {
                checkEmailOrPhone()
            }

            TIEPassLogin.addTextChangedListener {
                checkPassword()
            }

            txtRegister.setOnClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_registerFragment)
            }

            btnFacebook.setOnClickListener {
                callbackManager = CallbackManager.Factory.create()
                connectToFacebook()
            }

        }

        //post login
        viewModel.loginItem.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        Log.i("Success", "********* ${it.data}")
                        if (it.status) {
                            sessionManager.createLoginSession(it.data.token)
                            Navigation.findNavController(view)
                                .navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(
                            requireContext(),
                            "An error occurred $it",
                            Toast.LENGTH_SHORT
                        ).show()

                        Log.i("erooor", "********* $it")
                    }
                }
                is Resource.Loading -> {}
            }
        }

    }

    private fun connectToFacebook() {
        LoginManager.getInstance().logInWithReadPermissions(
            requireActivity(),
            Arrays.asList("email", "user_gender", "user_birthday", "public_profile")
        )
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.i("getAccessToken", "connectToFacebook = " + result.accessToken.token)

                    // App code
                    val request = GraphRequest.newMeRequest(
                        result.accessToken
                    ) { fbObject, response ->
                        if (response!!.error != null) {
                            print("ERROR")
                        } else {
                            try {
                                print("Success***************")

                                val fbObjectResult = fbObject.toString()
                                print("fbObject Result************** $fbObjectResult")

                                val fbUserId = fbObject!!.optString("id")
                                val fbUserName = fbObject.optString("name")
                                val fbFirstName = fbObject.optString("first_name")
                                val fbLastName = fbObject.optString("last_name")
                                val email = fbObject.optString("email")
                                val userGender = fbObject.optString("gender")
                                val userBirthday = fbObject.optString("birthday")
                                val fbUserProfilePics =
                                    "http://graph.facebook.com/$fbUserId/picture?type=large"
                                var profileURL = ""
                                if (Profile.getCurrentProfile() != null) {
                                    profileURL = ImageRequest.getProfilePictureUri(
                                        Profile.getCurrentProfile()?.id, 400, 400
                                    ).toString()
                                }

                                print("fbUserId ************** $fbUserId")
                                print("fbUserName ************** $fbUserName")
                                print("fbFirstName ************** $fbFirstName")
                                print("fbLastName ************** $fbLastName")
                                print("email ************** $email")
                                print("userGender ************** $userGender")
                                print("userBirthday ************** $userBirthday")
                                print("profileURL ************** $profileURL")
                                print("fbUserProfilePics ************** $fbUserProfilePics")

                                sessionManager.createLoginSession(result.accessToken.token)

                                val profile: Profile? = Profile.getCurrentProfile()
                                if (profile != null) {
                                    print("Login FirstName ************** ${profile.firstName}")
                                    print("Login MiddleName ************** ${profile.middleName}")
                                    print("Login LastName ************** ${profile.lastName}")
                                }

                                handleFacebookAccessToken(result.accessToken)

                            } //If no data has been retrieve throw some error
                            catch (e: JSONException) {

                            }
                        }
                        Log.v("FaceBook Response :", response.toString())
                    }
                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id, name, email, first_name, last_name, gender, birthday"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "Login Cancel", Toast.LENGTH_LONG).show()
                    Log.i("Login Cancel", "****************")
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    Log.i("Login onError", "**************** = " + error.message)
                }
            })
    }


    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = mAuth.currentUser
                print("user firebase************** $user")
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Authentication Facebook failed = ${it.exception}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun login() {
        if (checkEmailOrPhone() && checkPassword()) {
            val map = HashMap<String, String>()
            map["username"] = binding.TIEPhoneEmailLogin.text.toString()
            map["password"] = binding.TIEPassLogin.text.toString()
            viewModel.getLogin(map)

        }
    }

    private fun checkEmailOrPhone(): Boolean {
        if (binding.TIEPhoneEmailLogin.text?.isEmpty()!!) {
            binding.TILPhoneEmailLogin.error = null
            binding.TILPhoneEmailLogin.isErrorEnabled = false
            binding.TILPhoneEmailLogin.error = "يجب ادخال رقم البريد الالكتروني الهاتف اولا.."
            requestFocus(binding.TIEPhoneEmailLogin)
            return false
        } else {
            binding.TILPhoneEmailLogin.error = null
            binding.TILPhoneEmailLogin.isErrorEnabled = true
        }
        return true
    }

    private fun checkPassword(): Boolean {
        if (binding.TIEPassLogin.text?.isEmpty()!!) {
            binding.TILPassLogin.error = null
            binding.TILPassLogin.isErrorEnabled = true
            binding.TILPassLogin.error = "يجب ادخال كلمة المرور اولا.."
            requestFocus(binding.TIEPassLogin)
            return false
        } else {
            binding.TILPassLogin.error = null
            binding.TILPassLogin.isErrorEnabled = true
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            requireActivity().window
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }
}