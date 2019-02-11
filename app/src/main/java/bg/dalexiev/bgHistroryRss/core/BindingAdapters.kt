package bg.dalexiev.bgHistroryRss.core

import android.view.Gravity
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

@BindingAdapter(value = ["android:imageUrl", "android:imageCallback"], requireAll = false)
fun ImageView.bindImageUrl(imageUrl: String?, callback: (() -> Unit)?) {
    Picasso.get()
        .load(imageUrl)
        .fit()
        .centerCrop(Gravity.TOP)
        .into(this, object: Callback {
            override fun onSuccess() {
                callback?.let { it() }
            }

            override fun onError(e: Exception?) {
                callback?.let { it() }
            }

        })
}

