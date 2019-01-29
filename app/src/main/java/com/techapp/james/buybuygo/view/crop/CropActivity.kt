package com.techapp.james.buybuygo.view.crop

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.techapp.james.buybuygo.R
import kotlinx.android.synthetic.main.crop_activity_crop.*
import kotlinx.android.synthetic.main.crop_activity_crop.view.*

class CropActivity : AppCompatActivity() {
    companion object {
        val CROP_RESULT_CODE = 123
        val CROP_DATA = 565.toString()
        val CROP_RESULT_URI = ""
    }

    lateinit var uri: Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.crop_activity_crop)
        uri = Uri.parse(intent.getStringExtra(CROP_DATA))
        cropImageView.setImageUriAsync(uri)
cropImageView.setOnCropImageCompleteListener { view, result ->
    cropImageView.saveCroppedImageAsync(uri)
}
//        cropImageView.croppedImage
//        Intent back = new Intent();
//        back.putExtra("data", target.toString());
//        activity.setResult(RESULT_OK, back);
//        activity.finish();
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val inflater = this.menuInflater
        inflater.inflate(R.menu.crop_crop_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.order) {
            0 -> {
                var back = Intent()
//                back.putExtra(CROP_RESULT_URI, uri);
                this.setResult(RESULT_OK, back);
                this.finish();
            }
        }
        return true
    }

    override fun onBackPressed() {
    }
}
