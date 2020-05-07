package com.bguneys.app652020.info

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bguneys.app652020.R
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class InfoFragment : Fragment() {

    lateinit var mTextView : TextView
    var mList : List<User>? = null

    lateinit var compositeDisposible : CompositeDisposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view : View = inflater.inflate(R.layout.fragment_info, container, false)

        mTextView = view.findViewById(R.id.result_textView)

        compositeDisposible = CompositeDisposable()

        compositeDisposible.add(
            RetrofitObject.retrofitService.getUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response ->
                    mList = response

                    mTextView.setText(mList?.get(1)?.name)
                    //Toast.makeText(activity, response.toString(), Toast.LENGTH_SHORT).show()

                }, {failure ->
                    Toast.makeText(activity, "Error: " + failure.message, Toast.LENGTH_SHORT).show()
                })
        )


/* Plain Retrofit

        RetrofitObject.retrofitService.getUsers().enqueue(
            object: Callback<List<User>>{

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    Toast.makeText(activity, "Error: " + t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    mUserList = response.body()
                    mTextView.setText(mUserList?.get(1)?.name)
                }

            })

 */

        // Inflate the layout for this fragment
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        compositeDisposible.clear()
    }
}

