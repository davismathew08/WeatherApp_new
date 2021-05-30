package com.example.weatherappkotlin.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.weatherappkotlin.R
import io.realm.Realm
import io.realm.RealmResults
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {
    private lateinit var realm: Realm

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        realm = Realm.getDefaultInstance()
        settingBtn.setOnClickListener {
            realm.beginTransaction()
            realm.deleteAll()
            realm.commitTransaction()

            Toast.makeText(requireContext(),"Deleted Successfully", Toast.LENGTH_LONG).show()

        }
    }

}