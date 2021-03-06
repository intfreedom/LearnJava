package com.example.jbutler.mymou

import java.io.File
import java.io.IOException
import java.io.FileWriter

/**
 * LogEvent Writes String into CURRENT_DATE.txt using a runnable
 *
 * TODO: convert from thread+runnable into a coroutine
 *
 * @property message - message to log
 */
internal class LogEvent(private val message: String) : Runnable {
    override fun run() {
        val appFolder = MainMenu.folderManager.getFolder()
        val fileName = "${MainMenu.folderManager.getBaseDate()}.txt"
        val saveFile = File(appFolder, fileName)
        try {
            FileWriter(saveFile, true).apply {
                write("$message\n")
                close()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}