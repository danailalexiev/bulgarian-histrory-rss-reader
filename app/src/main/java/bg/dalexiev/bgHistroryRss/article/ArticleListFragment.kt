package bg.dalexiev.bgHistroryRss.article

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dalexiev.bgHistroryRss.App
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.data.entity.ArticlePreview
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleListBinding

class ArticleListFragment : Fragment() {

    private lateinit var mDataBinding: FragmentArticleListBinding

    private lateinit var mAdapter: ArticleListAdapter

    private lateinit var mNavController: NavController

    private val mViewModel by lazy {
        ViewModelProviders.of(activity!!, (context!!.applicationContext as App).viewModelFactory)
            .get(ArticleListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<FragmentArticleListBinding>(inflater, R.layout.fragment_article_list, container, false)
            .also { mDataBinding = it }
            .let { it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ArticleListAdapter(
            onShareItemClickListener = { mViewModel.onShareArticleClicked(this@ArticleListFragment.activity!!, it) },
            onFavouriteItemClickListener = { position: Int, article: ArticlePreview ->
                mViewModel.onToggleArticleClicked(
                    position,
                    article
                )
            },
            onItemClickListener = { mViewModel.onArticleClicked(it) }
        )
        mDataBinding.articleList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemOffsetDecoration(resources.getDimensionPixelSize(R.dimen.article_list_item_margin)))
            adapter = mAdapter
        }

        mDataBinding.swipeRefresh.setOnRefreshListener { mViewModel.sync() }

        mViewModel.articles.observe(this, Observer { state ->
            when (state) {
                is State.Loading -> mDataBinding.swipeRefresh.isRefreshing = true
                is State.Success -> {
                    mDataBinding.swipeRefresh.isRefreshing = false
                    mAdapter.submitList(state.value)
                }
                is State.Failure -> {
                    mDataBinding.swipeRefresh.isRefreshing = false
                    Toast.makeText(this@ArticleListFragment.context, "Error while loading articles", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })

        mViewModel.sharedArticle.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { intent ->
                intent.resolveActivity(this@ArticleListFragment.activity!!.packageManager)?.let {
                    startActivity(intent)
                }
            }
        })

        mViewModel.updatedArticle.observe(this, Observer { state ->
            when (state) {
                is State.Success -> mAdapter.updateItem(state.value.first, state.value.second)
                is State.Failure -> Toast.makeText(
                    this@ArticleListFragment.context,
                    "Could not update article",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

        mViewModel.selectedArticle.observe(this, Observer {
            it?.getContentIfNotHandled()?.let { articlePreview ->
                val args = Bundle().apply { putString(ArticleDetailsFragment.EXTRA_ARTICLE_GUID, articlePreview.guid) }
                mNavController
                    .navigate(R.id.action_articleListFragment_to_articleDetailsFragment, args)
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mNavController = Navigation.findNavController(activity!!, R.id.nav_host_fragment);

        mViewModel.loadArticles()
    }

    private class ItemOffsetDecoration(private val marginInPixels: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            val position = parent.getChildAdapterPosition(view)
            if (0 == position) {
                return
            }

            outRect.top = marginInPixels
        }
    }
}