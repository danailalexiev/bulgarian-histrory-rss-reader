package bg.dalexiev.bgHistroryRss.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.CoroutineDispatchers
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview
import bg.dalexiev.bgHistroryRss.databinding.ListItemArticleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArticleListAdapter(
    private val onShareItemClickListener: (ArticlePreview) -> Unit,
    private val onFavouriteItemClickListener: (Int, ArticlePreview) -> Unit,
    private val onItemClickListener: (ArticlePreview) -> Unit
) :
    RecyclerView.Adapter<ArticleListAdapter.ArticlePreviewViewHolder>() {

    private val data = mutableListOf<ArticlePreview>()
    private val scope = CoroutineScope(CoroutineDispatchers.main)

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlePreviewViewHolder {
        if (!::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }
        return ArticlePreviewViewHolder(
            inflater.inflate(R.layout.list_item_article, parent, false),
            onShareItemClickListener,
            onFavouriteItemClickListener,
            onItemClickListener
        )
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ArticlePreviewViewHolder, position: Int) =
        holder.bind(position, data[position])

    fun submitList(list: List<ArticlePreview>) {
        scope.launch {
            val result = withContext(CoroutineDispatchers.default) { DiffUtil.calculateDiff(DiffCallback(data, list)) }
            data.clear()
            data.addAll(list)
            result.dispatchUpdatesTo(this@ArticleListAdapter)
        }
    }

    fun updateItem(position: Int, article: ArticlePreview) {
        data.removeAt(position)
        data.add(position, article)
        notifyItemChanged(position)
    }

    class ArticlePreviewViewHolder(
        itemView: View,
        private val onShareItemClickListener: (ArticlePreview) -> Unit,
        private val onFavouriteItemClickListener: (Int, ArticlePreview) -> Unit,
        private val onItemClickListener: (ArticlePreview) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val mDataBinding = DataBindingUtil.bind<ListItemArticleBinding>(itemView);

        fun bind(position: Int, item: ArticlePreview) {
            mDataBinding?.apply {
                this.item = item
                articleActionShare.setOnClickListener { onShareItemClickListener(item) }
                articleActionFavourite.setOnClickListener { onFavouriteItemClickListener(position, item) }
                root.setOnClickListener { onItemClickListener(item) }
            }
                ?.executePendingBindings()
        }
    }

    private class DiffCallback(private val oldList: List<ArticlePreview>, private val newList: List<ArticlePreview>) :
        DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].guid == newList[newItemPosition].guid

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

    }
}
