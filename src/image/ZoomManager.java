package image;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

public class ZoomManager {

    private BufferedImage mImage;

    private ImageView mImageView;

    public ZoomManager(BufferedImage image) {
        mImage = image;
        WritableImage writableImage = new WritableImage(mImage.getWidth(), mImage.getHeight());
        writableImage = SwingFXUtils.toFXImage(mImage, writableImage);
        mImageView = new ImageView(writableImage);
    }

    public ImageView startZoom() {

        mImageView.setOnScroll(e -> {

            System.out.println("iamgeview getx: " + mImageView.getX());
            System.out.println("imageView gety: " + mImageView.getY());



            double deltaY = e.getDeltaY();
            double scale = 1;
            double screenX = e.getX();
            double screenY = e.getY();
            double w = mImageView.getLayoutBounds().getWidth();
            double h = mImageView.getLayoutBounds().getHeight();
            double imageX = (screenX - mImageView.getTranslateX()-w/2)/mImageView.getScaleX() + w/2;
            double imageY = (screenY - mImageView.getTranslateY()-h/2)/mImageView.getScaleY() + w/2;
            if (deltaY < 0) {


                scale = mImageView.getScaleX() * (0.9 + 0.1 * Math.exp(deltaY));
                mImageView.setScaleX(scale);
                mImageView.setScaleY(scale);
            } else if (deltaY > 0) {
                scale = mImageView.getScaleX() * (1.1 + 0.1 * Math.exp(-deltaY));
                mImageView.setScaleX(scale);
                mImageView.setScaleY(scale);
            }

            double newTranslateX = screenX - (imageX - w/2)*scale -w/2;
            double newTranslateY = screenY - (imageY - h/2)*scale -h/2;
            mImageView.setScaleX(scale);
            mImageView.setScaleY(scale);
            mImageView.setTranslateX(newTranslateX);
            mImageView.setTranslateY(newTranslateY);

        });

        return mImageView;
    }

}
