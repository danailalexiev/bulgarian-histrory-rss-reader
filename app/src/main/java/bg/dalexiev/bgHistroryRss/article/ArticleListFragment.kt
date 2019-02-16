package bg.dalexiev.bgHistroryRss.article

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.Fade
import androidx.transition.TransitionSet
import bg.dalexiev.bgHistroryRss.App
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleListBinding

class ArticleListFragment : Fragment() {

    private lateinit var mDataBinding: FragmentArticleListBinding

    private lateinit var mAdapter: ArticleListAdapter

    private lateinit var mNavController: NavController

    private val mViewModel by lazy {
        ViewModelProviders.of(this, (context!!.applicationContext as App).viewModelFactory)
            .get(ArticleListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        reenterTransition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())

        exitTransition = Fade(Fade.OUT)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<FragmentArticleListBinding>(inflater, R.layout.fragment_article_list, container, false)
            .also { mDataBinding = it }
            .let { it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = ArticleListAdapter(
            onShareItemClickListener = mViewModel::onShareArticleClicked,
            onFavouriteItemClickListener = mViewModel::onToggleArticleClicked,
            onItemClickListener = mViewModel::onArticleClicked
        )
        mDataBinding.articleList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(ItemOffsetDecoration(resources.getDimensionPixelSize(R.dimen.article_list_item_margin)))
            adapter = mAdapter
        }

        mDataBinding.swipeRefresh.setOnRefreshListener { mViewModel.sync() }

        mViewModel.articles.observe(viewLifecycleOwner, Observer { articlePreviews ->
            articlePreviews?.let {
                mAdapter.submitList(it)
            }
        })

        mViewModel.error.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { errorMessageResId ->
                Toast.makeText(context, errorMessageResId, Toast.LENGTH_SHORT).show()
            }
        })

        mViewModel.sharedArticle.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { link ->
                with(
                    createShareIntent(link)
                ) {
                    resolveActivity(activity!!.packageManager)?.let { startActivity(this) }
                }
            }
        })

        mViewModel.selectedArticle.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { positionArticlePair ->
                val args = Bundle().apply {
                    putString(
                        ArticleDetailsFragment.EXTRA_ARTICLE_GUID,
                        positionArticlePair.second.guid
                    )
                }
                val extras = mDataBinding.articleList.findViewHolderForAdapterPosition(positionArticlePair.first)
                    ?.let { viewHolder ->
                        FragmentNavigatorExtras(viewHolder.itemView to ViewCompat.getTransitionName(viewHolder.itemView)!!)
                    }
                mNavController
                    .navigate(R.id.action_articleListFragment_to_articleDetailsFragment, args, null, extras)
            }
        })
    }

    private fun createShareIntent(link: String) = ShareCompat.IntentBuilder.from(activity)
        .setType("texp/plain")
        .setText(link)
        .intent

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mNavController = Navigation.findNavController(activity!!, R.id.nav_host_fragment);
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