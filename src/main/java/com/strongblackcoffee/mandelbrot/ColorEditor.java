package com.strongblackcoffee.mandelbrot;

import java.awt.Color;

/**
 *
 */
public class ColorEditor implements ColorProvider {
    
    @Override
    public int getColor(int n) {
        if (n == 0) return 0;
        if (n == 1) return 0xFFFFFF;
        if (n == 2) return 0xF8F8F8;
        if (n == 3) return 0xEFEFEF;
        if (n == 4) return 0xE8E8E8;
        if (n == 5) return 0xDFDFDF;
        if (n == 6) return 0xD8D8D8;
        if (n == 7) return 0xCFCFCF;

        if (n == 8) return 0xFFFFFF;
        if (n < 10) return 0xFFEFEF;
        if (n < 12) return 0xFFDFDF;
        if (n < 14) return 0xFFCFCF;
        if (n < 16) return 0xFFBFBF;
        if (n < 18) return 0xFFAFAF;
        if (n < 20) return 0xFF9F9F;
        if (n < 22) return 0xFF8F8F;
        if (n < 24) return 0xFF7F7F;
        if (n < 26) return 0xFF6F6F;
        if (n < 28) return 0xFF5F5F;
        if (n < 30) return 0xFF4F4F;
        if (n < 32) return 0xFF3F3F;
        if (n < 34) return 0xFF2F2F;
        if (n < 36) return 0xFF1F1F;
        if (n < 38) return 0xFF0F0F;

        if (n < 40)  return 0xFFFFFF;
        if (n < 44)  return 0xDFFFDF;
        if (n < 48)  return 0xBFFFBF;
        if (n < 52)  return 0x9FFF9F;
        if (n < 56)  return 0x7FFF7F;
        if (n < 60)  return 0x5FFF5F;
        if (n < 64)  return 0x3FFF3F;
        if (n < 68)  return 0x1FFF1F;
        if (n < 72)  return 0xFFFFFF;
        if (n < 76)  return 0xDFDFFF;
        if (n < 80)  return 0xBFBFFF;
        if (n < 84)  return 0x9F9FFF;
        if (n < 88)  return 0x7F7FFF;
        if (n < 92)  return 0x5F5FFF;
        if (n < 96)  return 0x3F3FFF;
        if (n < 100) return 0x1F1FFF;
        
        return 0xFFFFFF;
    }
    
    
    
}
