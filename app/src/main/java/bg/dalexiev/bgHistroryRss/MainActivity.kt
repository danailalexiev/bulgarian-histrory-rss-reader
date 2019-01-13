package bg.dalexiev.bgHistroryRss

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import bg.dalexiev.bgHistroryRss.article.ArticleListFragment
import bg.dalexiev.bgHistroryRss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding

    private lateinit var mDrawerToggle: ActionBarDrawerToggle;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(mDataBinding.toolbar)

        mDrawerToggle = ActionBarDrawerToggle(
            this,
            mDataBinding.drawerLayout,
            mDataBinding.toolbar,
            R.string.open_navigation,
            R.string.close_navigation
        )
            .apply { setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px) }
            .also { mDataBinding.drawerLayout.addDrawerListener(it) }
            .apply { syncState() }

        mDataBinding.navView.setNavigationItemSelectedListener { item: MenuItem ->
            item.isChecked = true
            mDataBinding.drawerLayout.closeDrawers()
            true
        }

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ArticleListFragment.newInstance())
                .commitNow()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)

        mDrawerToggle.syncState()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)

        mDrawerToggle.onConfigurationChanged(newConfig)
    }
}