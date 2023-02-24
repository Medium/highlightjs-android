package com.pddstudio.highlightjs.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.medium.highlightjs.demo.databinding.ActivityEditBinding
import com.medium.highlightjs.models.Theme


class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding

    private val source = """public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, FilesListFragment.newInstance())
                .commit();
        val bool = true;
    }
}
"""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.hjsview.editMode = true
        binding.hjsview.setTheme(Theme.MEDIUM_LIGHT)
        binding.hjsview.setSource("\n")
    }
}