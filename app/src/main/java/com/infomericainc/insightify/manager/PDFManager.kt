package com.infomericainc.insightify.manager

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.infomericainc.insightify.R
import com.infomericainc.insightify.extension.makeToast
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PDFManager(private val context: Context) {

    private val pdfWidth = 595
    private val pdfHeight = 842

    private val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.company_logo)
    private val scaledBitmap = Bitmap.createScaledBitmap(bitmap, 119, 55, true)

    private val pdfDocument by lazy {
        PdfDocument()
    }

    private val pageOneInfo by lazy {
        PdfDocument.PageInfo.Builder(pdfWidth, pdfHeight, 0).create()
    }


    private val pageOne by lazy {
        pdfDocument.startPage(pageOneInfo)
    }

    private val pdfPageCanvas by lazy {
        pageOne.canvas
    }

    private val imagePaint by lazy {
        Paint()
    }
    private val textPaint by lazy {
        Paint()
    }
    private var textYPosition = 196f
    private val borderPaint by lazy {
        Paint()
    }

    fun savePdf(topic: String, title: String, content: String, resultURI: (Uri) -> Unit) {

        val modifiedContent = content.split("\n")

        val maxTextWidth = pdfWidth - 50
        borderPaint.color = ContextCompat.getColor(context, R.color.red)
        borderPaint.strokeWidth = 2f
        pdfPageCanvas.drawLine(40f, 0f, 40f, pdfHeight.toFloat(), borderPaint)
        pdfPageCanvas.drawLine(0f, 801f, pdfWidth.toFloat(), 801f, borderPaint)



        pdfPageCanvas.drawBitmap(scaledBitmap, 439f, 43f, imagePaint)
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(context, R.color.red)
        textPaint.textAlign = Paint.Align.LEFT
        pdfPageCanvas.drawText(title, 57f, 143f, textPaint)

        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        textPaint.textSize = 16f
        textPaint.color = ContextCompat.getColor(context, R.color.black)
        for (line in modifiedContent) {
            val textWidth = textPaint.measureText(line)
            if (textWidth > maxTextWidth) {
                val endIndex =
                    textPaint.breakText(line, true, maxTextWidth.toFloat(), floatArrayOf(530f))
                pdfPageCanvas.drawText(
                    line.substring(0, endIndex),
                    57f,
                    textYPosition,
                    textPaint
                )
                line.substring(endIndex);
            }
            pdfPageCanvas.drawText(
                line,
                57f,
                textYPosition,
                textPaint
            )
            textYPosition += textPaint.textSize + 20 // Adjust as needed
        }
        pdfDocument.finishPage(pageOne)
        try {
            val file = File.createTempFile(
                topic,
                ".pdf",
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            )
            FileOutputStream(file).use {
                pdfDocument.writeTo(it)
            }
            val uri = file.toUri()
            uri.takeIf { it.path?.isNotEmpty() == true }?.let(resultURI)
            context.makeToast("Pdf Saved successfully.")
        } catch (e: IOException) {
            context.makeToast("error")
            e.printStackTrace()
        }
        pdfDocument.close()
    }
}
