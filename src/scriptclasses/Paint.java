package scriptclasses;

import org.osbot.rs07.Bot;
import org.osbot.rs07.canvas.paint.Painter;
import org.osbot.rs07.script.Script;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Paint implements Painter {

    private static final Color GRAY = new Color(70, 61, 50, 156);
    private static final String IMG_PATH = "resources/overload.png";
    private BufferedImage polyImg;
    private Script script;
    private long startTime = System.currentTimeMillis();

    private Rectangle amtCombinedRect = new Rectangle(0, 244, 94, 47);
    private static int amtCombined;

    Paint(Bot bot) {
        bot.addPainter(this);
    }

    @Override
    public void onPaint(Graphics2D g) {
        drawRuntime(g);
        //drawMouse(g);
    }

    public static void incrementAmtCombined(Graphics2D g) {
        amtCombined++;
    }


    private void drawRuntime(Graphics2D g){
        final int X_ORIGIN = 404, Y_ORIGIN = 480, WIDTH = 111, HEIGHT = 22, TXT_X_OFFSET = 31, TXT_Y_OFFSET = 15;
        g.setColor(Color.RED);
        g.fillRect(X_ORIGIN, Y_ORIGIN, WIDTH, HEIGHT);
        g.setColor(Color.WHITE);
        String runtime = String.valueOf(formatTime(System.currentTimeMillis() - startTime));
        g.drawString(runtime, X_ORIGIN + TXT_X_OFFSET, Y_ORIGIN + TXT_Y_OFFSET);
    }

    private void drawMouse(Graphics2D g){
        Point mP = script.getMouse().getPosition();
        g.drawLine(mP.x - 5, mP.y + 5, mP.x + 5, mP.y - 5);
        g.drawLine(mP.x + 5, mP.y + 5, mP.x - 5, mP.y - 5);
    }

    private String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60;
        s %= 60; m %= 60; h %= 24;
        return String.format("%02d:%02d:%02d", h, m, s);
    }
}
