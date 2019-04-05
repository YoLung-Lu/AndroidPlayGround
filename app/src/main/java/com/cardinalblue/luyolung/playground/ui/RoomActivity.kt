package com.cardinalblue.luyolung.playground.ui

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.cardinalblue.luyolung.playground.R
import com.cardinalblue.luyolung.playground.model.Word
import com.cardinalblue.luyolung.playground.ui.adapter.WordListAdapter
import com.cardinalblue.luyolung.playground.viewmodel.WordViewModel
import com.cardinalblue.luyolung.util.subscribeUntil
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.activity_room.*

/**
 * Reference:
 */
class RoomActivity : AppCompatActivity() {

    private val lifeCycle = CompletableSubject.create()

    private val newWordActivityRequestCode = 1
    private lateinit var wordViewModel: WordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val wordList = findViewById<RecyclerView>(R.id.wordList)

        val adapter = WordListAdapter(this)
        wordList.adapter = adapter
        wordList.layoutManager = LinearLayoutManager(this)

        // Get a new or existing ViewModel from the ViewModelProvider.
        wordViewModel = ViewModelProviders.of(this).get(WordViewModel::class.java)

        // Add an observer on the LiveData returned by getAlphabetizedWords.
        // The onChanged() method fires when the observed data changes and the activity is
        // in the foreground.
        wordViewModel.allWords.observe(this, Observer { words ->
            // Update the cached copy of the words in the adapter.
            words?.let { adapter.setWords(it) }
        })

        RxView.clicks(function_new_word_btn)
            .subscribeUntil(lifeCycle) {
                val intent = Intent(this@RoomActivity, NewWordActivity::class.java)
                startActivityForResult(intent, newWordActivityRequestCode)
            }

//        RxView.clicks(function_progress_btn)
//            .subscribeUntil(lifeCycle) {
//                sendProgressNotification()
//            }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifeCycle.onComplete()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val word = Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY))
                wordViewModel.insert(word)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

}