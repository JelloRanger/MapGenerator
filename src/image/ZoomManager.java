package image;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

import java.awt.image.BufferedImage;

public class ZoomManager {

    private BufferedImage mImage;

    private ImageView mImageView;

    private AnchorPane mAnchorPane;

    private boolean mousePressed = false;

    private double lastX;

    private double lastY;

    public ZoomManager(BufferedImage image, AnchorPane pane) {
        mImage = image;
        mAnchorPane = pane;
        WritableImage writableImage = new WritableImage(mImage.getWidth(), mImage.getHeight());
        writableImage = SwingFXUtils.toFXImage(mImage, writableImage);
        mImageView = new ImageView(writableImage);
    }

    public ImageView startZoom() {

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        mAnchorPane.setOnMousePressed(e -> {
            mousePressed = true;
            lastX = e.getX();
            lastY = e.getY();
        });

        mAnchorPane.setOnMouseReleased(e -> {
            mousePressed = false;
        });

        mAnchorPane.setOnMouseDragged(e -> {
            if (mousePressed) {
                double x = e.getX();
                double y = e.getY();

                double deltaX = x - lastX;
                double deltaY = y - lastY;
                mImageView.setTranslateX(mImageView.getTranslateX() + deltaX);
                mImageView.setTranslateY(mImageView.getTranslateY() + deltaY);

                lastX = x;
                lastY = y;
            }
        });

        mAnchorPane.addEventHandler(ScrollEvent.ANY, e -> {
            double deltaY = e.getDeltaY();
            double scale = 1;
            double screenX = e.getX();
            double screenY = e.getY();
            double width = mImageView.getLayoutBounds().getWidth();
            double height = mImageView.getLayoutBounds().getHeight();
            double imageX = (screenX - mImageView.getTranslateX() - width / 2) / mImageView.getScaleX() + width / 2;
            double imageY = (screenY - mImageView.getTranslateY() - height / 2) / mImageView.getScaleY() + height / 2;

            // Zoom based on scroll direction
            if (deltaY < 0) {
                scale = mImageView.getScaleX() * (0.9 + 0.1 * Math.exp(deltaY));
                mImageView.setScaleX(scale);
                mImageView.setScaleY(scale);
            } else if (deltaY > 0) {
                scale = mImageView.getScaleX() * (1.1 + 0.1 * Math.exp(-deltaY));
                mImageView.setScaleX(scale);
                mImageView.setScaleY(scale);
            }

            // Move imageview such that the zoom is centered on the mouse cursor
            double newTranslateX = screenX - (imageX - width / 2) * scale - width / 2;
            double newTranslateY = screenY - (imageY - height / 2) * scale - height / 2;
            mImageView.setTranslateX(newTranslateX);
            mImageView.setTranslateY(newTranslateY);

        });

        return mImageView;
    }

}
