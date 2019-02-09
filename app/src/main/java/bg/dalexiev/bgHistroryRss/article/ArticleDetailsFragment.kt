package bg.dalexiev.bgHistroryRss.article

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import bg.dalexiev.bgHistroryRss.App
import bg.dalexiev.bgHistroryRss.R
import bg.dalexiev.bgHistroryRss.core.State
import bg.dalexiev.bgHistroryRss.databinding.FragmentArticleDetailsBinding

private const val EXTRA_ARTICLE_GUID = "extra_article_guid"

class ArticleDetailsFragment : Fragment() {

    private lateinit var mDataBinding: FragmentArticleDetailsBinding

    private val mViewModel by lazy {
        ViewModelProviders.of(activity!!, (context!!.applicationContext as App).viewModelFactory)
            .get(ArticleDetailsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        DataBindingUtil.inflate<FragmentArticleDetailsBinding>(
            inflater,
            R.layout.fragment_article_details,
            container,
            false
        )
            .also { mDataBinding = it }
            .let { it.root }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel.article.observe(this, Observer { state ->
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
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.getString(EXTRA_ARTICLE_GUID)?.let {
            mViewModel.init(it)
        }
    }

    companion object {

        fun newInstance(articleGuid: String) = ArticleDetailsFragment().apply {
            val args = Bundle()
            args.putString(EXTRA_ARTICLE_GUID, articleGuid)
            arguments = args
        }

    }

}