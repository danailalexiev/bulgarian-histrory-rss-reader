package bg.dalexiev.bgHistroryRss.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.transition.ChangeBounds
import androidx.transition.ChangeTransform
import androidx.transition.TransitionSet
import bg.dalexiev.bgHistroryRss.App
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleDetailsBinding

class ArticleDetailsFragment : Fragment() {

    companion object {

        const val EXTRA_ARTICLE_GUID = "extra_article_guid"

    }

    private lateinit var mDataBinding: FragmentArticleDetailsBinding

    private val mViewModel by lazy {
        ViewModelProviders.of(activity!!, (context!!.applicationContext as App).viewModelFactory)
            .get(ArticleDetailsViewModel::class.java)
    }

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

        mViewModel.article.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is State.Success -> {
                    mDataBinding.article = state.value
                    mDataBinding.executePendingBindings()
                }
                is Error -> {
                    Toast.makeText(
                        this@ArticleDetailsFragment.context,
                        "Error while loading article",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        mViewModel.imageLoaded.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { scheduleStartPostponedEnterTransition() }
        })
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getString(EXTRA_ARTICLE_GUID)?.let {
            mViewModel.init(it)
        }
    }
}