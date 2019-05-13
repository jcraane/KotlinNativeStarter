package nl.jamiecraane.multiplatform.myapp.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import nl.jamiecraane.multiplatform.myapp.R
import nl.jamiecraane.multiplatform.myapp.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        message.text = sample.hello()
        val builder = StringBuilder()
        viewModel.persons.observe(this, Observer {
            it.forEach {
                builder.append(it).append("\n")
                persons.text = builder.toString()
            }
        })
        viewModel.errorText.observe(this, Observer {
            errorText.text = it
        })
    }
}
