package com.mdshahsamir.familytracker

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class Util {

    companion object{
        private lateinit var alertDialog: AlertDialog

        fun showLoadingAnimation(context: Context) {
            val view = LayoutInflater.from(context).inflate(R.layout.loading_animation_dialog, null)
            val gifAnimation = view.findViewById<ImageView>(R.id.gifAnimation)
            val alert = AlertDialog.Builder(context)
            alert.setView(view)
            alertDialog = alert.create()
            alertDialog.setCanceledOnTouchOutside(false)
            alertDialog.show()

            Glide.with(context).asGif().load(R.drawable.loading_anim_gif).listener(object :
                    RequestListener<GifDrawable?> {
                override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<GifDrawable?>?,
                        isFirstResource: Boolean
                ): Boolean {
                    return false
                }

                override fun onResourceReady(
                        resource: GifDrawable?,
                        model: Any?,
                        target: Target<GifDrawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            }).into(gifAnimation)
        }

        fun closeLoadingAnim() {
            if (alertDialog != null) {
                alertDialog.dismiss()
            }
        }
    }
}