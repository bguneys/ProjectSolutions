package com.everydaysolutions.bguneys.everydaysolutions.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.ShareCompat
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.everydaysolutions.bguneys.everydaysolutions.R


class SettingsFragment : PreferenceFragmentCompat() {
    /**
     * Called during [.onCreate] to supply the preferences for this fragment
     */
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

    override fun onPreferenceTreeClick(preference: Preference?): Boolean {

        when(preference?.key) {

            getString(R.string.preference_disclaimer) -> {
                findNavController().navigate(R.id.action_action_settings_to_disclaimerFragment)
            }

            getString(R.string.preference_about) -> {
                findNavController().navigate(R.id.action_action_settings_to_aboutFragment)
            }

            getString(R.string.share_app) -> {
                //share app Play Store page link
                ShareCompat.IntentBuilder
                    .from(requireActivity())
                    .setType("text/plain")
                    .setChooserTitle(getString(R.string.share_this_app_with))
                    .setText("https://play.google.com/store/apps/details?id=" + requireActivity().packageName)
                    .startChooser()
            }

            getString(R.string.rate_app) -> {
                //start app Play Store page for rating
                val url = "https://play.google.com/store/apps/details?id=" + requireActivity().packageName
                val uri = Uri.parse(url)
                val rateIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(rateIntent)
            }
        }

        return super.onPreferenceTreeClick(preference)
    }

}
