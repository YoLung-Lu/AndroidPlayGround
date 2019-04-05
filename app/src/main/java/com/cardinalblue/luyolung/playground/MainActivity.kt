package com.cardinalblue.luyolung.playground

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.cardinalblue.luyolung.playground.ui.*
import com.cardinalblue.luyolung.room.RoomActivity
import com.cardinalblue.luyolung.util.subscribeUntil
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.CompletableSubject

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // State.
    private val lifeCycle = CompletableSubject.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        RxView.clicks(function_service_btn)
            .subscribeUntil(lifeCycle) {
                val intent = Intent(this, NotificationServiceView::class.java)
                startService(intent)
            }

        RxView.clicks(function_notification_btn)
            .subscribeUntil(lifeCycle) {
                val intent = Intent(this, NotificationActivity::class.java)
                this.startActivity(intent)
            }

        RxView.clicks(function_room_btn)
            .subscribeUntil(lifeCycle) {
                val intent = Intent(this, RoomActivity::class.java)
                this.startActivity(intent)
            }

        RxView.clicks(function_content_provider_btn)
            .subscribeUntil(lifeCycle) {
                val intent = Intent(this, ContentProviderActivity::class.java)
                this.startActivity(intent)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifeCycle.onComplete()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
