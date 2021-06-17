package hr.fer.zemris.java.webserver.workers;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Worker that makes a png image with dimennsions 200x200 and
 * with a single filled circle.
 *
 * @author MatejCubek
 * @project hw03-0036516398
 * @created 30/03/2021
 */
@SuppressWarnings("unused")
public class CircleWorker implements IWebWorker {
    @SuppressWarnings("RedundantThrows")
    @Override
    public void processRequest(RequestContext context) throws Exception {
        BufferedImage bim = new BufferedImage(200, 200, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2d = bim.createGraphics();

        Ellipse2D.Double circle = new Ellipse2D.Double(0, 0, 200, 200);
        g2d.setColor(Color.RED);
        g2d.fill(circle);

        g2d.dispose();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ImageIO.write(bim, "png", bos);
            context.setMimeType("image/png");
            context.write(bos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
