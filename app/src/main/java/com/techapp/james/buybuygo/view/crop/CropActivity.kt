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
import timber.log.Timber
import android.provider.MediaStore
import android.graphics.Bitmap


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
        Timber.d(uri.toString())
        val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        cropImageView.setImageUriAsync(uri)
//        cropImageView.setImageBitmap(bitmap)
        cropImageView.setOnCropImageCompleteListener { view, result ->
            cropImageView.saveCroppedImageAsync(uri)
            cropImageView.setOnCropImageCompleteListener { view, result ->
                var back = Intent()
//                back.putExtra(CROP_RESULT_URI, uri);
                this.setResult(RESULT_OK, back);
                this.finish();
            }
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
                cropImageView.getCroppedImageAsync()
            }
        }
        return true
    }

    override fun onBackPressed() {
    }
}
