package com.example

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ide.ui.LafManager
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.editor.colors.EditorColorsScheme
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.diagnostic.Logger
import java.io.File

class DarkPinkThemePlugin : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val lafManager = LafManager.getInstance()
        val currentTheme = lafManager.currentLookAndFeel?.name
        val darkPinkThemeName = "Dark Pink Theme"
        val lightThemeName = "IntelliJ Light"
        val customColorsPath = "C:/Users/Сергей/IdeaProjects/demo/src/main/resources/colors/"
        val fileName = "DarkPinkTheme.icls"

        if (currentTheme == darkPinkThemeName) {
            val lightTheme = lafManager.installedLookAndFeels.firstOrNull { it.name == lightThemeName }
            if (lightTheme != null) {
                lafManager.setCurrentLookAndFeel(lightTheme)
                lafManager.updateUI()

                val editorColorsManager = EditorColorsManager.getInstance()
                val defaultScheme = editorColorsManager.allSchemes.firstOrNull { it.name == "Default" }
                if (defaultScheme != null) {
                    editorColorsManager.setGlobalScheme(defaultScheme)
                }
            }
        } else {
            val darkPinkTheme = lafManager.installedLookAndFeels.firstOrNull { it.name == darkPinkThemeName }
            if (darkPinkTheme != null) {
                lafManager.setCurrentLookAndFeel(darkPinkTheme)
                lafManager.updateUI()

                val editorColorsManager = EditorColorsManager.getInstance()


                val customColorScheme = loadCustomColorScheme(customColorsPath, fileName, darkPinkThemeName)
                if (customColorScheme != null) {
                    editorColorsManager.setGlobalScheme(customColorScheme)
                }
            } else {
                Messages.showMessageDialog(
                    "Темно-розовая тема не найдена.",
                    "Ошибка",
                    Messages.getErrorIcon()
                )
            }
        }
    }

    private fun loadCustomColorScheme(colorsPath: String, fileName: String, themeName: String): EditorColorsScheme? {
        try {
            val file = File(colorsPath, fileName)
            if (file.exists()) {
                val virtualFile = LocalFileSystem.getInstance().findFileByPath(file.absolutePath)
                if (virtualFile != null) {
                    val editorColorsManager = EditorColorsManager.getInstance()

                    val colorScheme = editorColorsManager.getScheme(themeName)
                    if (colorScheme != null) {
                        return colorScheme
                    }
                }
            }
        } catch (e: Exception) {
            Logger.getInstance(DarkPinkThemePlugin::class.java).warn("Ошибка при загрузке цветовой схемы", e)
        }
        return null
    }
}
