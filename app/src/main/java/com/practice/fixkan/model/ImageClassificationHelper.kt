package com.practice.fixkan.model

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder

class ImageClassificationHelper(private val context: Context) {
    private var tfLite: Interpreter?=null


    init {
        downloadModel()
    }

    private fun downloadModel() {
        val conditions = CustomModelDownloadConditions.Builder().build()

        FirebaseModelDownloader
            .getInstance()
            .getModel("fixkan-model", DownloadType.LATEST_MODEL, conditions)
            .addOnSuccessListener { mymodel ->
                val modelFile = mymodel.file
                if (modelFile != null) {
                    tfLite = Interpreter(modelFile)
                    Toast.makeText(context, "Model AI Siap Digunakan!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Model Gagal Diunduh!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                Log.e("ImageClassifierHelper", "Model Download Failed", it)
            }
    }

    private fun preprocessImage(bitmap: Bitmap): ByteBuffer {
        val inputSize = 224
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputSize, inputSize, true)

        val byteBuffer = ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4)
        byteBuffer.order(ByteOrder.nativeOrder())

        val intValues = IntArray(inputSize * inputSize)
        resizedBitmap.getPixels(intValues, 0, inputSize, 0, 0, inputSize, inputSize)

        var pixelIndex = 0
        for (i in 0 until inputSize) {
            for (j in 0 until inputSize) {
                val pixelValue = intValues[pixelIndex++]
                byteBuffer.putFloat(((pixelValue shr 16 and 0xFF) / 255.0f)) // Red
                byteBuffer.putFloat(((pixelValue shr 8 and 0xFF) / 255.0f))  // Green
                byteBuffer.putFloat(((pixelValue and 0xFF) / 255.0f))        // Blue
            }
        }
        return byteBuffer
    }

    fun classifyImage(bitmap: Bitmap): String {
        if (tfLite == null) {
            return "Model Belum Siap!"
        }

        // Ubah gambar ke format yang sesuai dengan model
        val inputBuffer = preprocessImage(bitmap)

        // Pastikan output sesuai model
        val outputShape = intArrayOf(1, 7) // Sesuaikan dengan jumlah kelas
        val outputBuffer = Array(1) { FloatArray(7) } // Gunakan FloatArray jika model float32

        // Jalankan inferensi
        tfLite!!.run(inputBuffer, outputBuffer)

        // Ambil hasil prediksi
        val outputArray = outputBuffer[0]
        val maxIndex = outputArray.indices.maxByOrNull { outputArray[it] } ?: return "Prediksi: Unknown"

        // Daftar label kelas
        val classLabels = arrayOf("Bangunan Normal", "Bangunan Runtuh", "Bangunan Retak", "Jalan Normal", "Jalan Rusak", "Lingkungan Bersih", "Sampah Berserakan")

        return if (maxIndex in classLabels.indices) {
            classLabels[maxIndex]
        } else {
            "Unknown"
        }
    }
}