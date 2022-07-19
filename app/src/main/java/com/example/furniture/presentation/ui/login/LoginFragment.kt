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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONException
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
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 123


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
        mAuth = FirebaseAuth.getInstance()

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
                connectToFacebook()
            }

            btnGmail.setOnClickListener {
                openSignIn()
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
                            sessionManager.putString("typeSignIn", "normal")
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

        // for signIn facebook
        callbackManager = CallbackManager.Factory.create()
        LoginManager.getInstance()
            .registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult) {
                    Log.i(
                        "",
                        "getAccessTokenConnectToFacebook *************** ${result.accessToken.token}"
                    )

                    // App code
                    val request = GraphRequest.newMeRequest(
                        result.accessToken
                    ) { fbObject, response ->
                        if (response!!.error != null) {
                            Log.i("", "ERROR***************")
                        } else {
                            try {
                                Log.i("", "Success***************")

                                val fbObjectResult = fbObject.toString()
                                Log.i("", "fbObject Result************** $fbObjectResult")

                                val fbUserId = fbObject!!.optString("id")
                                val fbUserName = fbObject.optString("name")
                                val fbFirstName = fbObject.optString("first_name")
                                val fbLastName = fbObject.optString("last_name")
                                val email = fbObject.optString("email")
                                val userGender = fbObject.optString("gender")
                                val userBirthday = fbObject.optString("birthday")
                                val picture =
                                    fbObject.getJSONObject("picture").getJSONObject("data")
                                        .getString("url")
                                val fbUserProfilePics =
                                    "http://graph.facebook.com/$fbUserId/picture?type=large"
                                var profileURL = ""
                                if (Profile.getCurrentProfile() != null) {
                                    profileURL = ImageRequest.getProfilePictureUri(
                                        Profile.getCurrentProfile()?.id, 400, 400
                                    ).toString()
                                }

                                Log.i("", "fbUserId ************** $fbUserId")
                                Log.i("", "fbUserName ************** $fbUserName")
                                Log.i("", "fbFirstName ************** $fbFirstName")
                                Log.i("", "fbLastName ************** $fbLastName")
                                Log.i("", "email ************** $email")
                                Log.i("", "userGender ************** $userGender")
                                Log.i("", "userBirthday ************** $userBirthday")
                                Log.i("", "picture ************** $picture")
                                Log.i("", "profileURL ************** $profileURL")
                                Log.i("", "fbUserProfilePics ************** $fbUserProfilePics")

                                val profile: Profile? = Profile.getCurrentProfile()
                                if (profile != null) {
                                    Log.i("", "Login FirstName ************** ${profile.firstName}")
                                    Log.i(
                                        "",
                                        "Login MiddleName ************** ${profile.middleName}"
                                    )
                                    Log.i("", "Login lastName ************** ${profile.lastName}")
                                }

                                sessionManager.putString("typeSignIn", "facebook")
                                handleFacebookAccessToken(result.accessToken)

                            } //If no data has been retrieve throw some error
                            catch (e: JSONException) {

                            }
                        }
                        Log.i("", "FaceBook Response *************** $response")
                    }

                    // if you want use gender and birthday
                    // you should request it in logInWithReadPermissions ("user_gender", "user_birthday")
                    // and to do that you should request it in facebook developer
                    // select app review and select Permissions and Features and choose birthday and gender
                    val parameters = Bundle()
                    parameters.putString(
                        "fields",
                        "id, name, email,first_name, last_name, picture.type(large), gender, birthday"
                    )
                    request.parameters = parameters
                    request.executeAsync()
                }

                override fun onCancel() {
                    Toast.makeText(requireContext(), "Login Cancel", Toast.LENGTH_LONG).show()
                    Log.i("", "LoginCancel****************")
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(requireContext(), error.message, Toast.LENGTH_LONG).show()
                    Log.i("", "LoginError**************** = " + error.message)
                }
            })

        // for signOut facebook
        // use LoginManager.getInstance().logout()

        // for signIn google
        createRequestGmail()

        // for signOut google
        // use mAuth.signOut()

    }

    // you must make that to signIn facebook and google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }

        callbackManager.onActivityResult(requestCode, resultCode, data)

        Log.d("letsSee", "malsehnnnnnn: " + data)
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

    //facebook
    private fun connectToFacebook() {
        // if not use this not listen LoginManager
        LoginManager.getInstance().logInWithReadPermissions(
            this,
            listOf("email", "public_profile")
        )

    }

    fun handleFacebookAccessToken(token: AccessToken) {
        val credential = FacebookAuthProvider.getCredential(token.token)
        mAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                val user = mAuth.currentUser
                Log.i("", "user firebase************** $user")
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

    // google
    private fun openSignIn() {
        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun createRequestGmail() {
        // Configure google sign-in
        val serverClientId = getString(R.string.client_id)
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(serverClientId)
            .requestEmail()
            .requestProfile()
            .build()

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), googleSignInOptions)
        Log.i("createRequestGmail", "**********")
    }

    private fun handleGoogleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            Log.i("account auth", "********** =$account")
            Log.i("token gmail auth", "********** =" + account.idToken)
            Log.i("id gmail auth", "********** =" + account.id)
            Log.i("email  gmail auth", "********** =" + account.email)
            Log.i("family name gmail auth", "********** =" + account.familyName)
            Log.i("display name gmail auth", "********** =" + account.displayName)
            Log.i("given name gmail auth", "********** =" + account.givenName)
            Log.i("photo name gmail auth", "********** =" + account.photoUrl)

            firebaseAuthWithGoogleAccount(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.i("error in gmail auth", "signInResult:failed code=" + e.message)
            //            updateUI(null);
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount) {
        Log.i("", "firebaseAuthWithGoogleAccount********")

        val credential = GoogleAuthProvider.getCredential(account.idToken,null)

        mAuth.signInWithCredential(credential).addOnSuccessListener { authResult->

            Log.i("", "firebaseAuthWithGoogleAccount LoggedIn********")

            //get loggedIn user
            val firebaseUser = mAuth.currentUser
            //get user info
            val uid = firebaseUser?.uid
            val email = firebaseUser?.email

            Log.i("", "firebaseAuthWithGoogleAccount uid******** $uid")
            Log.i("", "firebaseAuthWithGoogleAccount email******** $email")

            if(authResult.additionalUserInfo!!.isNewUser){
                Log.i("", "firebaseAuthWithGoogleAccount account create******** $email")
                Toast.makeText(requireContext(), "account create", Toast.LENGTH_LONG).show()
            }else{
                Log.i("", "firebaseAuthWithGoogleAccount existing user******** $email")
                Toast.makeText(requireContext(), "existing user", Toast.LENGTH_LONG).show()
            }

            sessionManager.putString("typeSignIn", "google")
            Navigation.findNavController(requireView())
                .navigate(R.id.action_loginFragment_to_homeFragment)
        }.addOnFailureListener {
            Log.i("", "firebaseAuthWithGoogleAccount LoggedIn failed******** ${it.message}")
            Toast.makeText(requireContext(), "LoggedIn failed \n  ${it.message}", Toast.LENGTH_LONG).show()
        }
    }
}