package com.nudriin.mysharedpreferences

import android.app.Instrumentation.ActivityResult
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.nudriin.mysharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mUserPreference: UserPreference
    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel


    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result ->
        if (result.data != null && result.resultCode == FormUserActivity.RESULT_CODE) {
            userModel = result.data?.getParcelableExtra<UserModel>(FormUserActivity.EXTRA_RESULT) as UserModel
            populateView(userModel)
            checkForm(userModel)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "My User Preference"
        mUserPreference = UserPreference(this)
        showExistingPreference()

        binding.btnSave.setOnClickListener(this)
    }

    private fun showExistingPreference() {
        userModel = mUserPreference.getUser()
        populateView(userModel)
        checkForm(userModel)
    }
    private fun populateView(userModel: UserModel) {
        binding.tvName.text = if (userModel.name.toString().isEmpty()) "Tidak Ada" else userModel.name
        binding.tvAge.text = if (userModel.age.toString().isEmpty()) "Tidak Ada" else userModel.age.toString()
        binding.tvIsLoveMu.text = if (userModel.isLove) "Ya" else "Tidak"
        binding.tvEmail.text = if (userModel.email.toString().isEmpty()) "Tidak Ada" else userModel.email
        binding.tvPhone.text = if (userModel.phoneNumber.toString().isEmpty()) "Tidak Ada" else userModel.phoneNumber
    }
    private fun checkForm(userModel: UserModel) {
        when {
            userModel.name.toString().isNotEmpty() -> {
                binding.btnSave.text = getString(R.string.change)
                isPreferenceEmpty = false
            }
            else -> {
                binding.btnSave.text = getString(R.string.save)
                isPreferenceEmpty = true
            }
        }
    }

    override fun onClick(view: View) {
        if (view.id == R.id.btn_save) {
            val intent = Intent(this@MainActivity, FormUserActivity::class.java)
            when {
                isPreferenceEmpty -> {
                    intent.putExtra(
                        FormUserActivity.EXTRA_TYPE_FORM,
                        FormUserActivity.TYPE_ADD
                    )
                    intent.putExtra("USER", userModel)
                }
                else -> {
                    intent.putExtra(
                        FormUserActivity.EXTRA_TYPE_FORM,
                        FormUserActivity.TYPE_EDIT
                    )
                    intent.putExtra("USER", userModel)
                }
            }
            resultLauncher.launch(intent)
        }
    }
}