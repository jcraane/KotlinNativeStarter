package nl.jamiecraane.multiplatform.myapp.main

import Echo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import nl.jamiecraane.multiplatform.myapp.R
import nl.jamiecraane.multiplatform.myapp.databinding.ActivityMainBinding
import nl.jamiecraane.multiplatform.myapp.main.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        super.onCreate(savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        message.text = sample.hello()
        val builder = StringBuilder()
        viewModel.persons.observe(this, Observer {
            it.forEach {
                builder.append(it).append("\n")
                persons.text = builder.toString()
            }
        })
        val taskBuilder = StringBuilder()
        /*viewModel.tasks.observe(this, Observer {
            it.forEach {
                taskBuilder.append(it).append("\n")
                tasks.text = taskBuilder.toString()
            }
        })*/
        viewModel.errorText.observe(this, Observer {
            errorText.text = it
        })
        viewModel.message.observe(this, Observer {
            serverMessage.text = it
        })

        parallel.setOnClickListener {
            viewModel.parallel()
        }

        echoOutput.text = Echo.sayHelloEcho()
    }
}
