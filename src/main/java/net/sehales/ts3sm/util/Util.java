package net.sehales.ts3sm.util;

import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

import javafx.scene.control.Tab;
import javafx.scene.paint.Color;

import org.controlsfx.glyphfont.FontAwesome.Glyph;
import org.controlsfx.glyphfont.GlyphFont;
import org.controlsfx.glyphfont.GlyphFontRegistry;

import net.sehales.ts3sm.controller.RefreshableTab;

public class Util {
    /**
     * max value possible used for download/upload quota and max upload/download bandwidth (same as -1)
     */
    public static final BigInteger GLOBAL_MAX_BYTES_VALUE = new BigInteger("18446744073709551615");
    private static GlyphFont       fa                     = GlyphFontRegistry.font("FontAwesome");

    private static NumberFormat    nf                     = NumberFormat.getInstance(Locale.ENGLISH);

    static {
        nf.setMaximumFractionDigits(0);
        nf.setGroupingUsed(false);
        nf.setRoundingMode(RoundingMode.HALF_EVEN);
    }

    /**
     * Creates a FontAwesome glyph
     * 
     * @param glyph
     * @return
     */
    public static org.controlsfx.glyphfont.Glyph getFANode(Glyph glyph) {
        return fa.create(glyph);
    }

    /**
     * Creates a FontAwesome glyph with size 24 and darkcyan color
     * 
     * @param glyph
     * @return
     */
    public static org.controlsfx.glyphfont.Glyph getTabFANode(Glyph glyph) {
        return fa.create(glyph).size(24).color(Color.DARKCYAN);
    }

    static Integer mapStringToInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tests the tab for being an instance of {@link RefreshableTab} and calls the {@link RefreshableTab#refresh}-method.
     * 
     * @param tab
     */
    public static void refreshTab(Tab tab) {
        if (tab instanceof RefreshableTab) {
            ((RefreshableTab) tab).refresh();
        }
    }

    public static String toDecimalNotation(double d) {
        return nf.format(d);
    }
}
