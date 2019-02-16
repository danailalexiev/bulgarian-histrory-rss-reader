package bg.dalexiev.bgHistroryRss.article

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview
import bg.dalexiev.bgHistroryRss.databinding.ListItemArticleBinding

class ArticleListAdapter(
    private val onShareItemClickListener: (ArticlePreview) -> Unit,
    private val onFavouriteItemClickListener: (ArticlePreview) -> Unit,
    private val onItemClickListener: (Int, ArticlePreview) -> Unit
) :
    RecyclerView.Adapter<ArticleListAdapter.ArticlePreviewViewHolder>() {

    private val differ = AsyncListDiffer<ArticlePreview>(this, DIFF_CALLBACK)
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

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ArticlePreviewViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    fun submitList(list: List<ArticlePreview>) {
        differ.submitList(list)
    }

    class ArticlePreviewViewHolder(
        itemView: View,
        private val onShareItemClickListener: (ArticlePreview) -> Unit,
        private val onFavouriteItemClickListener: (ArticlePreview) -> Unit,
        private val onItemClickListener: (Int, ArticlePreview) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val mDataBinding = DataBindingUtil.bind<ListItemArticleBinding>(itemView);

        fun bind(item: ArticlePreview) {
            ViewCompat.setTransitionName(itemView, "article_${item.guid}")
            mDataBinding?.apply {
                this.item = item
                articleActionShare.setOnClickListener { onShareItemClickListener(item) }
                articleActionFavourite.setOnClickListener { onFavouriteItemClickListener(item) }
                root.setOnClickListener { onItemClickListener(adapterPosition, item) }
            }
                ?.executePendingBindings()
        }
    }

    private object DIFF_CALLBACK : DiffUtil.ItemCallback<ArticlePreview>() {

        override fun areItemsTheSame(oldItem: ArticlePreview, newItem: ArticlePreview) = oldItem.guid == newItem.guid


        override fun areContentsTheSame(oldItem: ArticlePreview, newItem: ArticlePreview) = oldItem == newItem

    }
}
