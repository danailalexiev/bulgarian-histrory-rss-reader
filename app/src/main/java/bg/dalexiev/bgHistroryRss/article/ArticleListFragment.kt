package bg.dalexiev.bgHistroryRss.article

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bg.dalexiev.bgHistroryRss.App
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleListBinding

class ArticleListFragment : Fragment() {

    private lateinit var mDataBinding: FragmentArticleListBinding

    private lateinit var mAdapter: ArticleListAdapter

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

        mAdapter = ArticleListAdapter()
        mDataBinding.articleList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemOffsetDecoration(resources.getDimensionPixelSize(R.dimen.article_list_item_margin)))
            adapter = mAdapter
        }

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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.loadArticles()
    }

    companion object {

        fun newInstance() = ArticleListFragment()

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