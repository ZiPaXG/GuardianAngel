package com.guardianangel.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {
    private static BitmapFont defaultFont;

    public static void loadFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("UI/10 Font/CyberpunkCraftpixPixel.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 32;
        parameter.color = Color.WHITE;
        defaultFont = generator.generateFont(parameter);
        generator.dispose();
    }

    public static BitmapFont getDefaultFont() {
        return defaultFont;
    }

    public static void dispose() {
        if (defaultFont != null) {
            defaultFont.dispose();
        }
    }
}
