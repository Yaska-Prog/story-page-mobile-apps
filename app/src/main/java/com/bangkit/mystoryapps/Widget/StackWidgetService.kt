package com.bangkit.mystoryapps.Widget

import android.content.Intent
import android.widget.RemoteViewsService
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bangkit.mystoryapps.data.local.SharedPreferenceManager
import com.bangkit.mystoryapps.data.viewmodels.StoryViewModel
import com.bangkit.mystoryapps.data.viewmodels.ViewModelFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(p0: Intent?): RemoteViewsFactory {
        val sharedPref = SharedPreferenceManager(this)
        if(sharedPref.getUser() != null){
//            val factory = ViewModelFactory.getStoryInstance(this)
//            val viewModel: StoryViewModel = ViewModelProvider((ViewModelStoreOwner), factory).get(StoryViewModel::class.java)
            return StackRemoteViewsFactory(this.applicationContext)
        }
        else{
            return StackRemoteViewsFactory(this.applicationContext)
        }
    }
}