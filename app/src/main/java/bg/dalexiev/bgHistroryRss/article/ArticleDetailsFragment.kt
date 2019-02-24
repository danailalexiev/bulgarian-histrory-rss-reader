package bg.dalexiev.bgHistroryRss.article

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.Event
import bg.dalexiev.bgHistroryRss.core.getViewModel
import bg.dalexiev.bgHistroryRss.core.shareText
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {

    companion object {

        const val EXTRA_ARTICLE_GUID = "extra_article_guid"

    }

    private lateinit var mDataBinding: FragmentArticleDetailsBinding

    private val mViewModel by lazy { getViewModel<ArticleDetailsViewModel>() }

    private var mFavouriteMenuIcon: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        postponeEnterTransition()

        return DataBindingUtil.inflate<FragmentArticleDetailsBinding>(
            inflater,
            R.layout.fragment_article_details,
            container,
            false
        )
            .also {
                mDataBinding = it
                mDataBinding.viewModel = mViewModel
            }
            .let { it.root }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ViewCompat.setTransitionName(mDataBinding.root, "article_${arguments?.getString(EXTRA_ARTICLE_GUID)}")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mViewModel.apply {
            article.observe(viewLifecycleOwner, onArticleChanged())
            imageLoaded.observe(viewLifecycleOwner, onArticleImageLoadedFailed())
            sharedLink.observe(viewLifecycleOwner, onSharedLinkChanged())
        }

        arguments?.getString(EXTRA_ARTICLE_GUID)?.let {
            mViewModel.init(it)
        }
    }

    private fun onArticleChanged() = Observer<Article> { article ->
        article?.let {
            updateFavouriteMenuItemState(it)
            mDataBinding.article = it
            mDataBinding.executePendingBindings()
        }
    }

    private fun updateFavouriteMenuItemState(article: Article) {
        mFavouriteMenuIcon?.apply {
            title = if (article.isFavourite) {
                getString(R.string.article_details_remove_favourite_label)
            } else {
                getString(R.string.article_details_add_favourite_label)
            }
            icon = if (article.isFavourite) {
                ContextCompat.getDrawable(context!!, R.drawable.ic_favourite_24px)
            } else {
                ContextCompat.getDrawable(context!!, R.drawable.ic_not_favourite_24px)
            }
        }
    }

    private fun onArticleImageLoadedFailed() = Observer<Event<Unit>> {
        it?.getContentIfNotHandled()?.let { scheduleStartPostponedEnterTransition() }
    }

    private fun scheduleStartPostponedEnterTransition() {
        with(mDataBinding.articleImage.viewTreeObserver) {
            addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    removeOnPreDrawListener(this)

                    startPostponedEnterTransition()
                    return true
                }
            })
        }
    }

    private fun onSharedLinkChanged() = Observer<Event<String>> {
        it?.getContentIfNotHandled()?.let { link -> shareText(link) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_article_details, menu)
        mFavouriteMenuIcon = menu.findItem(R.id.article_details_favourite)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        mViewModel.article.value?.let {
            updateFavouriteMenuItemState(it)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.article_details_favourite -> {
            mViewModel.onToggleFavouriteClicked()
            true
        }
        R.id.article_details_share -> {
            mViewModel.onShareLinkClicked()
            true
        }
        R.id.article_details_mark_as_unread -> {
            mViewModel.onMarkAsUnreadClicked()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}