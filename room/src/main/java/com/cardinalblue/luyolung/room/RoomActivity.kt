package com.cardinalblue.luyolung.room

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.cardinalblue.luyolung.playground.db.Word
import com.cardinalblue.luyolung.playground.db.WordViewModel
import com.cardinalblue.luyolung.util.subscribeUntil
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.CompletableSubject
import kotlinx.android.synthetic.main.activity_room.*

/**
 * Reference:
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view-kotlin/#0
 */
class RoomActivity : AppCompatActivity() {

    private val lifeCycle = CompletableSubject.create()

    private val STRING_LENGTH = 10
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

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

        RxView.clicks(function_remove_btn)
            .subscribeUntil(lifeCycle) {
                wordViewModel.allWords.value?.firstOrNull()?.let {
                    wordViewModel.delete(it)
                }
            }

        RxView.clicks(random_btn)
            .subscribeUntil(lifeCycle) {
                val word = Word(randomString())
                wordViewModel.insert(word)
            }
    }

    private fun randomString(): String =
        (1..STRING_LENGTH)
            .map { kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")

    override fun onDestroy() {
        super.onDestroy()
        lifeCycle.onComplete()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newWordActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val word =
                    Word(data.getStringExtra(NewWordActivity.EXTRA_REPLY))
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