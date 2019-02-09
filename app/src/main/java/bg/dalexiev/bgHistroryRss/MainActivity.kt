package bg.dalexiev.bgHistroryRss

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import bg.dalexiev.bgHistroryRss.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var mDataBinding: ActivityMainBinding
    private lateinit var mNavController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(mDataBinding.toolbar)

        mNavController = findNavController(R.id.nav_host_fragment)
        val appBarConfig = AppBarConfiguration(mNavController.graph)
        mDataBinding.toolbar.setupWithNavController(mNavController, appBarConfig)
    }

    override fun onSupportNavigateUp() = mNavController.navigateUp()
}