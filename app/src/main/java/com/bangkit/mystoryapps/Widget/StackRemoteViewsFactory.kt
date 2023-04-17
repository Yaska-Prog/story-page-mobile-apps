package com.bangkit.mystoryapps.Widget

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bangkit.mystoryapps.R
import com.bangkit.mystoryapps.UI.DetailStory.DetailStoryActivity
import com.bangkit.mystoryapps.data.remote.response.ListStoryItem
import java.net.URL

class StackRemoteViewsFactory(private val mContext: Context, private val listStoryItem: List<ListStoryItem>? = null) : RemoteViewsService.RemoteViewsFactory {
    override fun onCreate() {

    }

    override fun onDataSetChanged() {

    }

    override fun onDestroy() {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        return if(listStoryItem != null){
            listStoryItem.size
        } else{
            0
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        if(listStoryItem != null){
            val currentUrl = listStoryItem[position].photoUrl

            val imgBitmap = BitmapFactory.decodeStream(URL(currentUrl).openConnection().getInputStream())
            rv.setImageViewBitmap(R.id.imgStackWidget, imgBitmap)

            //start activity on click
            val intent = Intent(mContext, DetailStoryActivity::class.java)
            intent.putExtra(DetailStoryActivity.EXTRA_ID, listStoryItem[position].id)
            val pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            rv.setOnClickPendingIntent(R.id.imgStackWidget, pendingIntent)
        }
        return rv
    }

    override fun getLoadingView(): RemoteViews {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItemId(p0: Int): Long {
        TODO("Not yet implemented")
    }

    override fun hasStableIds(): Boolean {
        TODO("Not yet implemented")
    }
}