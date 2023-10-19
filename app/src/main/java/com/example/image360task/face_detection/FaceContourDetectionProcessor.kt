package com.example.image360task

import android.annotation.SuppressLint
import android.graphics.Rect
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.persistableBundleOf
import androidx.lifecycle.lifecycleScope


import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException


class FaceContourDetectionProcessor(private val view: GraphicOverlay, val context: MainActivity) :
    BaseImageAnalyzer<List<Face>>() {

    private val realTimeOpts = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()


    private val detector = FaceDetection.getClient(realTimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: IOException) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onSuccess(
        results: List<Face>,
        graphicOverlay: GraphicOverlay,
        rect: Rect
    ) {


        graphicOverlay.clear()
        results.forEach {

            val faceGraphic = Graphic(graphicOverlay, it, rect)
            graphicOverlay.add(faceGraphic)
            if (MainActivity.position < MainActivity.listrotateface.size) {
                Log.v("position", "position=" + MainActivity.position)
                try {


                    val facevalue = MainActivity.listrotateface.get(MainActivity.position).first


                    if (it.headEulerAngleY < 50 && it.headEulerAngleY > 10 && facevalue.equals("left")) {

                        MainActivity.storeAllfaceBitmap(context)


                    } else if (it.headEulerAngleY < -10 && it.headEulerAngleY > -50 && facevalue.equals(
                            "right"
                        )
                    ) {

                        MainActivity.storeAllfaceBitmap(context)
                        Log.v("position", "positionrr=" + MainActivity.store360img)


                    } else if (it.headEulerAngleX < 50 && it.headEulerAngleX > 10 && facevalue.equals(
                            "up"
                        )
                    ) {
                        //  MainActivity.binding.btnSwitch.visibility = View.VISIBLE
                        MainActivity.storeAllfaceBitmap(context)
                        // MainActivity.store360img++


                    } else if (it.headEulerAngleX > -50 && it.headEulerAngleX < -10 && facevalue.equals(
                            "down"
                        )
                    ) {

                        MainActivity.storeAllfaceBitmap(context)
                        Log.v("position", "positiondd=" + MainActivity.store360img)


                    } else if (facevalue.equals("smile") && it.headEulerAngleY > -4 && it.headEulerAngleY < 4) {

                        var prob = getSmileProbability(it)
                        if (prob != null) {
                            if (prob >= 0.5) {

                                MainActivity.storeAllfaceBitmap(context)


                            } else {
                                MainActivity.binding.btnSwitch.visibility = View.GONE
                            }
                        }

                    } else {
                        MainActivity.binding.btnSwitch.visibility = View.GONE


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }


        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {
        Log.w(TAG, "Face Detector failed.$e")
    }

    fun getSmileProbability(face: Face): Float? {
        return face.smilingProbability
    }

    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }

}