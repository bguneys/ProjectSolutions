package com.bguneys.app652020.info

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bguneys.app652020.R
import com.bguneys.app652020.databinding.FragmentInfoBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class InfoFragment : Fragment() {

    //ViewBinding backing property
    private var _binding : FragmentInfoBinding? = null
    private val binding
        get() = _binding!!

    var mList : List<InfoItem>? = null

    lateinit var compositeDisposible : CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentInfoBinding.inflate(inflater, container, false)

        val adapter = InfoListAdapter(InfoListAdapter.InfoClickListener{

            val url = it.url
            val uri = Uri.parse(url)
            val intent = Intent(Intent.ACTION_VIEW, uri)

            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            }

        })

        compositeDisposible = CompositeDisposable()

        compositeDisposible.add(
            RetrofitObject.retrofitService.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response ->
                    mList = response

                    adapter.submitList(mList)
                    binding.errorTextView.visibility = View.GONE

                }, {failure ->
                    binding.errorTextView.text = getString(R.string.error_text)
                    binding.errorTextView.visibility = View.VISIBLE
                    Toast.makeText(activity, getString(R.string.cant_connect_to_internet), Toast.LENGTH_SHORT).show()
                })
        )

        binding.infoRecyclerView.adapter = adapter
        binding.infoRecyclerView.layoutManager = LinearLayoutManager(activity)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposible.clear()
        _binding = null
    }
}

