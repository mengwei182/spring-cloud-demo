package org.example.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

public class ImageCaptchaUtils {
    private static final Random RANDOM = new Random();
    // 使用到Algerian字体，系统里没有的话需要安装字体，去掉了1、0、i、l、o几个容易混淆的字符
    private static final String VERIFY_CODES = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";

    private ImageCaptchaUtils() {
    }

    public static String generateCaptcha(int captchaSize) {
        return generateCaptcha(captchaSize, VERIFY_CODES);
    }

    public static String generateCaptcha(int captchaSize, String sources) {
        if (sources == null || sources.isEmpty()) {
            sources = VERIFY_CODES;
        }
        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder captcha = new StringBuilder(captchaSize);
        for (int i = 0; i < captchaSize; i++) {
            captcha.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }
        return captcha.toString();
    }

    public static String outputCaptchaImage(int width, int height, File outputFile, int captchaSize) throws IOException {
        String captcha = generateCaptcha(captchaSize);
        outputImage(width, height, outputFile, captcha);
        return captcha;
    }

    public static String outputCaptchaImage(int width, int height, OutputStream outputStream, int captchaSize) throws IOException {
        String captcha = generateCaptcha(captchaSize);
        outputImage(width, height, outputStream, captcha);
        return captcha;
    }

    private static void outputImage(int width, int height, File outputFile, String code) throws IOException {
        if (outputFile == null) {
            return;
        }
        File dir = outputFile.getParentFile();
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                return;
            }
        }
        if (!outputFile.createNewFile()) {
            return;
        }
        FileOutputStream fos = new FileOutputStream(outputFile);
        outputImage(width, height, fos, code);
        fos.close();
    }

    private static void outputImage(int width, int height, OutputStream outputStream, String code) throws IOException {
        int captchaSize = code.length();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random rand = new Random();
        Graphics2D graphics2D = image.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 设置边框色
        graphics2D.setColor(Color.GRAY);
        graphics2D.fillRect(0, 0, width, height);
        Color c = getRandColor(200, 250);
        // 设置背景色
        graphics2D.setColor(c);
        graphics2D.fillRect(0, 2, width, height - 4);
        // 绘制干扰线
        Random random = new Random();
        // 设置线条的颜色
        graphics2D.setColor(getRandColor(160, 200));
        for (int i = 0; i < 20; i++) {
            int x = random.nextInt(width - 1);
            int y = random.nextInt(height - 1);
            int xl = random.nextInt(6) + 1;
            int yl = random.nextInt(12) + 1;
            graphics2D.drawLine(x, y, x + xl + 40, y + yl + 20);
        }
        // 添加噪点的噪声率
        float yawpRate = 0.05f;
        int area = (int) (yawpRate * width * height);
        for (int i = 0; i < area; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int rgb = getRandomIntColor();
            image.setRGB(x, y, rgb);
        }
        // 使图片扭曲
        shear(graphics2D, width, height, c);
        graphics2D.setColor(getRandColor(100, 160));
        int fontSize = height - 4;
        Font font = new Font("Algerian", Font.ITALIC, fontSize);
        graphics2D.setFont(font);
        char[] chars = code.toCharArray();
        for (int i = 0; i < captchaSize; i++) {
            AffineTransform affine = new AffineTransform();
            affine.setToRotation(Math.PI / 4 * rand.nextDouble() * (rand.nextBoolean() ? 1 : -1), (double) (width / captchaSize) * i + (double) fontSize / 2, (double) height / 2);
            graphics2D.setTransform(affine);
            graphics2D.drawChars(chars, i, 1, ((width - 10) / captchaSize) * i + 5, height / 2 + fontSize / 2 - 10);
        }
        graphics2D.dispose();
        ImageIO.write(image, "jpg", outputStream);
    }

    private static Color getRandColor(int fc, int bc) {
        fc = Math.min(fc, 255);
        bc = Math.min(bc, 255);
        int r = fc + RANDOM.nextInt(bc - fc);
        int graphics = fc + RANDOM.nextInt(bc - fc);
        int b = fc + RANDOM.nextInt(bc - fc);
        return new Color(r, graphics, b);
    }

    private static int getRandomIntColor() {
        int[] rgb = getRandomRgb();
        int color = 0;
        for (int c : rgb) {
            color = color << 8;
            color = color | c;
        }
        return color;
    }

    private static int[] getRandomRgb() {
        int[] rgb = new int[3];
        for (int i = 0; i < 3; i++) {
            rgb[i] = RANDOM.nextInt(255);
        }
        return rgb;
    }

    private static void shear(Graphics graphics, int w1, int h1, Color color) {
        shearX(graphics, w1, h1, color);
        shearY(graphics, w1, h1, color);
    }

    private static void shearX(Graphics graphics, int w1, int h1, Color color) {
        int frames = 1;
        int phase = RANDOM.nextInt(2);
        for (int i = 0; i < h1; i++) {
            double d = (6.2831853071795862D * (double) phase) / (double) frames;
            graphics.copyArea(0, i, w1, 1, (int) d, 0);
            graphics.setColor(color);
            graphics.drawLine((int) d, i, 0, i);
            graphics.drawLine((int) d + w1, i, w1, i);
        }
    }

    private static void shearY(Graphics graphics, int w1, int h1, Color color) {
        int period = RANDOM.nextInt(40) + 10;
        int frames = 20;
        int phase = 7;
        for (int i = 0; i < w1; i++) {
            double d = (double) (period >> 1) * Math.sin((double) i / (double) period + (6.2831853071795862D * (double) phase) / (double) frames);
            graphics.copyArea(i, 0, 1, h1, 0, (int) d);
            graphics.setColor(color);
            graphics.drawLine(i, (int) d, i, 0);
            graphics.drawLine(i, (int) d + h1, i, h1);
        }
    }
}