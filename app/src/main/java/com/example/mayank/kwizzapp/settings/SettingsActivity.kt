package com.example.mayank.kwizzapp.settings

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.example.mayank.kwizzapp.R
import com.example.mayank.kwizzapp.bankdetail.BankDetailFragment
import com.example.mayank.kwizzapp.bankdetail.EditBankDetailsFragment
import com.example.mayank.kwizzapp.policies.PoliciesFragment
import com.example.mayank.kwizzapp.profile.EditProfileFragment
import com.example.mayank.kwizzapp.profile.ProfileFragment
import com.example.mayank.kwizzapp.settings.menusettings.SettingMenuFragment
import net.rmitsolutions.mfexpert.lms.helpers.logD
import net.rmitsolutions.mfexpert.lms.helpers.switchToFragment
import org.jetbrains.anko.find
import org.jetbrains.anko.startActivity

class SettingsActivity : AppCompatActivity(), SettingMenuFragment.OnFragmentInteractionListener,
        ProfileFragment.OnFragmentInteractionListener, EditProfileFragment.OnFragmentInteractionListener,
        BankDetailFragment.OnFragmentInteractionListener, EditBankDetailsFragment.OnFragmentInteractionListener,
        PoliciesFragment.OnFragmentInteractionListener {

    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolBar = find(R.id.toolbar)
        setSupportActionBar(toolBar)

        val menuFragment = SettingMenuFragment()
        switchToFragment(menuFragment)
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        logD("Fragment Count - $count")
        when (count) {
            0 -> super.onBackPressed()
            1 -> supportFragmentManager.popBackStack()
            else -> {
                finish()
                startActivity<SettingsActivity>()
            }
        }
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.mShare -> {
                val i = Intent(Intent.ACTION_SEND)
                i.type = "text/plain"
                i.putExtra(android.content.Intent.EXTRA_TEXT, "KwizzAppp is pretty cool. Check it out on Google Play Games. See if you can beat my score!\n" +
                        "\n" +
                        "https://play.google.com/store/apps/details?id=com.kwizzapp&pcampaignid=GPG_shareGame")
                startActivity(Intent.createChooser(i, "Share Via"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
