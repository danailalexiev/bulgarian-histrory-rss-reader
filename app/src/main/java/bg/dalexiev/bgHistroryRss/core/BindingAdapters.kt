package bg.dalexiev.bgHistroryRss.core

import android.view.Gravity
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("android:imageUrl")
fun ImageView.bindImageUrl(imageUrl: String?) {
    Picasso.get()
        .load(imageUrl)
        .fit()
        .centerCrop(Gravity.TOP)
        .into(this)
}