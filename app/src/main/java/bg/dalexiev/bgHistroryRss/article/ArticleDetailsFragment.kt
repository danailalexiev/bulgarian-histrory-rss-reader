package bg.dalexiev.bgHistroryRss.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
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
import bg.dalexiev.bgHistroryRss.data.entity.Article
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {

    companion object {

        const val EXTRA_ARTICLE_GUID = "extra_article_guid"

    }

    private lateinit var mDataBinding: FragmentArticleDetailsBinding

    private val mViewModel by lazy { getViewModel<ArticleDetailsViewModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionSet()
            .setOrdering(TransitionSet.ORDERING_TOGETHER)
            .addTransition(ChangeBounds())
            .addTransition(ChangeTransform())

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
        }

        arguments?.getString(EXTRA_ARTICLE_GUID)?.let {
            mViewModel.init(it)
        }
    }

    private fun onArticleChanged() = Observer<Article> { article ->
        article?.let {
            mDataBinding.article = it
            mDataBinding.executePendingBindings()
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
}