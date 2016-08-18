package controller;

import image.ImageManager;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.PerlinMap;
import model.Point;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UiController {

    private static final String TAG = UiController.class.getSimpleName();

    @FXML
    private AnchorPane mCanvasAnchor;

    @FXML
    private ProgressIndicator mLoadingCircle;

    @FXML
    private Button mGenerateButton;

    @FXML
    private Button mWidthDefaultButton;

    @FXML
    private Button mHeightDefaultButton;

    @FXML
    private Button mPersistenceDefaultButton;

    @FXML
    private Button mOctavesDefaultButton;

    @FXML
    private Button mLandGenDefaultButton;

    @FXML
    private Button mHillGenDefaultButton;

    @FXML
    private Button mMountainGenDefaultButton;

    @FXML
    private Button mCityGenDefaultButton;

    @FXML
    private Button mSaveImageButton;

    @FXML
    private TextField mSeedField;

    @FXML
    private TextField mWidthTextField;

    @FXML
    private TextField mHeightTextField;

    @FXML
    private TextField mPersistenceField;

    @FXML
    private TextField mOctavesField;

    @FXML
    private TextField mLandGenTextField;

    @FXML
    private TextField mHillGenTextField;

    @FXML
    private TextField mMountainGenTextField;

    @FXML
    private TextField mCityGenTextField;

    @FXML
    private CheckBox mLandGenCheckBox;

    @FXML
    private CheckBox mHillGenCheckBox;

    @FXML
    private CheckBox mMountainGenCheckBox;

    @FXML
    private CheckBox mRiverGenCheckBox;

    @FXML
    private CheckBox mCityGenCheckBox;

    @FXML
    private CheckBox mNameGenCheckBox;

    @FXML
    private CheckBox mContinentGenCheckBox;

    @FXML
    private CheckBox mTerritoryGenCheckBox;

    @FXML
    private CheckBox mPoliticalMapCheckBox;

    @FXML
    private MenuItem mMenuClose;

    private Stage mStage;

    private BufferedImage mImage;

    private ImageManager mImageManager;

    //private List<ImageView> mMapImageCache;

    private boolean mSeedEdited = false;

    private static final int WIDTH = 1000;

    private static final int HEIGHT = 1000;

    private static final double LANDGEN = 0;

    private static final double WATERGEN = -0.5;

    private static final double MOUNTAINGEN = 0.8;

    private static final double HILLGEN = 0.7;

    private static final double BEACHGEN = -0.0125;

    private static final double FORESTGEN = 0.2;

    private static final int CITYGEN = 50;

    private static final double PERSISTENCE = 0.5;

    private static final int OCTAVES = 8;



    private static final int MIN_PIXELS = 100;




    public void initialize() {

        mMenuClose.setOnAction(this::handleMenuClose);

        //mMapImageCache = new LinkedList<>();

        generateMap();

        mGenerateButton.setOnAction(this::handleGenerateButtonAction);
        mPersistenceDefaultButton.setOnAction(this::handlePersistenceDefaultButtonAction);
        mOctavesDefaultButton.setOnAction(this::handleOctavesDefaultButtonAction);
        mSaveImageButton.setOnAction(this::handleSaveImageButtonAction);
        mSeedField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                mSeedEdited = true;
            }
        }));

        // Default Buttons
        mWidthDefaultButton.setOnAction(this::handleWidthDefaultButton);
        mHeightDefaultButton.setOnAction(this::handleHeightDefaultButton);
        mLandGenDefaultButton.setOnAction(this::handleLandGenDefaultButton);
        mHillGenDefaultButton.setOnAction(this::handleHillGenDefaultButton);
        mMountainGenDefaultButton.setOnAction(this::handleMountainGenDefaultButton);
        mCityGenDefaultButton.setOnAction(this::handleCityGenDefaultButton);

        // CheckBox listeners
        mPoliticalMapCheckBox.setOnAction(this::handlePoliticalMapCheckBox);
    }

    private void setZoom() {
        ImageView imageView = new ImageView();
        DoubleProperty zoomProperty = new SimpleDoubleProperty(300);

        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("invalidated");
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 3);
            }
        });

        mCanvasAnchor.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                System.out.println("scroll scroll");
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });

        WritableImage writableImage = new WritableImage(mImage.getWidth(), mImage.getHeight());
        writableImage = SwingFXUtils.toFXImage(mImage, writableImage);

        imageView.setImage(writableImage);
        imageView.setId("mapImage");
        imageView.preserveRatioProperty().set(true);


        mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));
        System.out.println("wtf");
        mCanvasAnchor.getChildren().add(imageView);

        //mCanvasAnchor.setContent(imageView);
    }

    private void setZoom2() {
        WritableImage writableImage = new WritableImage(mImage.getWidth(), mImage.getHeight());
        writableImage = SwingFXUtils.toFXImage(mImage, writableImage);
        double width = writableImage.getWidth();
        double height = writableImage.getHeight();

        ImageView imageView = new ImageView(writableImage);
        imageView.setPreserveRatio(true);

        // ===== my stuff =====
        DoubleProperty zoomProperty = new SimpleDoubleProperty(300);
        imageView.setId("mapImage");

        mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));
        System.out.println("wtf");
        mCanvasAnchor.getChildren().add(imageView);


        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                System.out.println("invalidated");
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 4);
            }
        });

        // ===== end my stuff =====

        reset(imageView, writableImage.getWidth(), writableImage.getHeight());

        ObjectProperty<Point2D> mouseDown = new SimpleObjectProperty<>();

        imageView.setOnMousePressed(e -> {
            Point2D mousePress = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
            mouseDown.set(mousePress);
        });

        imageView.setOnMouseDragged(e -> {

            if (imageView.getFitWidth() < 1000 || imageView.getFitHeight() < 1000) {
                return;
            }

            Point2D dragPoint = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));
            shift(imageView, dragPoint.subtract(mouseDown.get()), zoomProperty);
            mouseDown.set(imageViewToImage(imageView, new Point2D(e.getX(), e.getY())));
        });

        imageView.setOnScroll(e -> {
            double delta = e.getDeltaY();
            Rectangle2D viewport = imageView.getViewport();

            double scale = clamp(Math.pow(1.01, delta),

                    // don't scale so we're zoomed in fewer thjan MIN_PIXELS in any direction
                    Math.min(MIN_PIXELS / viewport.getWidth(), MIN_PIXELS / viewport.getHeight()),

                    // don't scale so that we're bigger than image dimensions
                    Math.max(width / viewport.getWidth(), height / viewport.getHeight()));


            if (scale == 1.0 || viewport.getWidth() == 1000 && viewport.getHeight() == 1000) {

                if (e.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (e.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
                return;
            }

            Point2D mouse = imageViewToImage(imageView, new Point2D(e.getX(), e.getY()));

            double newWidth = viewport.getWidth() * scale;
            double newHeight = viewport.getHeight() * scale;

            // To keep the visual point under the mouse from moving, we need
            // (x - newViewportMinX) / (x - currentViewportMinX) = scale
            // where x is the mouse X coordinate int he image

            // solving this for newViewportMinX gives

            // newViewportMinX = x - (x - currentViewportMinX) * scale

            // we then clamp this value so the image never scrolls out of the imageView

            double newMinX = clamp(mouse.getX() - (mouse.getX() - viewport.getMinX()) * scale, 0, width - newWidth);
            double newMinY = clamp(mouse.getY() - (mouse.getY() - viewport.getMinY()) * scale, 0, height - newHeight);

            imageView.setViewport(new Rectangle2D(newMinX, newMinY, newWidth, newHeight));
        });

        /*imageView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                reset(imageView, width, height);
            }
        });*/
    }

    // reset to the top left
    private void reset(ImageView imageView, double width, double height) {
        imageView.setViewport(new Rectangle2D(0, 0, width, height));
    }

    // shift the viewport of the imageView by the specified delta,
    // clamping so the viewport does not move off the actual image
    private void shift(ImageView imageView, Point2D delta, DoubleProperty zoomProperty) {
        Rectangle2D viewport = imageView.getViewport();

        /*double width = zoomProperty.get() * 4;//imageView.getImage().getWidth();
        double height = zoomProperty.get() * 4;//imageView.getImage().getHeight();

        System.out.println("width: " + width);
        System.out.println("height: " + height);*/

        double maxX = imageView.getImage().getWidth()/*zoomProperty.get() * 4*/ - viewport.getWidth();//width - viewport.getWidth();
        double maxY = imageView.getFitHeight()/*zoomProperty.get() * 4*/ - viewport.getHeight();//height - viewport.getHeight();

        System.out.println("viewport.getWidth(): " + viewport.getWidth());
        System.out.println("viewport.getHeight(): " + viewport.getHeight());

        System.out.println("viewport.getMinX(): " + viewport.getMinX());
        System.out.println("viewport.getMinY(): " + viewport.getMinY());

        double minX = clamp(viewport.getMinX() - delta.getX(), 0, maxX);
        double minY = clamp(viewport.getMinY() - delta.getY(), 0, maxY);

        imageView.setViewport(new Rectangle2D(minX, minY,
                viewport.getWidth(),
                viewport.getHeight()));
    }

    private double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        }
        else if (value > max) {
            return max;
        }
        return value;
    }

    // convert mouse coordinates in the imageView to coordinates in the actual image
    private Point2D imageViewToImage(ImageView imageView, Point2D imageViewCoordinates) {
        double xProportion = imageViewCoordinates.getX() / imageView.getBoundsInLocal().getWidth();
        double yProportion = imageViewCoordinates.getY() / imageView.getBoundsInLocal().getHeight();

        Rectangle2D viewport = imageView.getViewport();
        return new Point2D(
                viewport.getMinX() + xProportion * viewport.getWidth(),
                viewport.getMinY() + yProportion * viewport.getHeight());
    }

    public void setStageAndSetupListeners(Stage stage) {
        mStage = stage;
    }

    private void handleMenuClose(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void handleGenerateButtonAction(ActionEvent event) {
        generateMap();
    }

    // Default buttons

    private void handlePersistenceDefaultButtonAction(ActionEvent event) {
        mPersistenceField.setText(String.valueOf(PERSISTENCE));
    }

    private void handleOctavesDefaultButtonAction(ActionEvent event) {
        mOctavesField.setText(String.valueOf(OCTAVES));
    }

    private void handleWidthDefaultButton(ActionEvent event) {
        mWidthTextField.setText(String.valueOf(WIDTH));
    }

    private void handleHeightDefaultButton(ActionEvent event) {
        mHeightTextField.setText(String.valueOf(HEIGHT));
    }

    private void handleLandGenDefaultButton(ActionEvent event) {
        mLandGenTextField.setText(String.valueOf(LANDGEN));
        mLandGenCheckBox.setSelected(true);
    }

    private void handleHillGenDefaultButton(ActionEvent event) {
        mHillGenTextField.setText(String.valueOf(HILLGEN));
        mHillGenCheckBox.setSelected(true);
    }

    private void handleMountainGenDefaultButton(ActionEvent event) {
        mMountainGenTextField.setText(String.valueOf(MOUNTAINGEN));
        mMountainGenCheckBox.setSelected(true);
    }

    private void handleCityGenDefaultButton(ActionEvent event) {
        mCityGenTextField.setText(String.valueOf(CITYGEN));
        mCityGenCheckBox.setSelected(true);
    }

    // CheckBox listeners

    private void handlePoliticalMapCheckBox(ActionEvent event) {
        if (mPoliticalMapCheckBox.isSelected()) {
            mImageManager.colorPoliticalMap();
        } else {
            mImageManager.colorTerrain();
        }

        mImage = mImageManager.getImage();
        ImageView imageView = new ImageView();
        imageView.setImage(SwingFXUtils.toFXImage(mImage, null));
        imageView.setId("mapImage");
        mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));
        mCanvasAnchor.getChildren().add(imageView);
    }

    private void handleSaveImageButtonAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(mSaveImageButton.getText());
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Pictures"));
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("PNG", "*.png");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(mStage);
        if (file != null) {
            try {
                WritableImage writableImage = new WritableImage(mImage.getWidth(),
                        mImage.getHeight());
                SwingFXUtils.toFXImage(mImage, writableImage);
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException ioe) {
                Logger.getLogger(TAG).log(Level.SEVERE, null, ioe);
            }
        }
    }

    private void generateMap() {

        mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));

        verifyFields();

        /*if (mMapImageCache.size() > 0) {
            ImageView imageView = mMapImageCache.remove(0);
            mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));
            mCanvasAnchor.getChildren().add(imageView);
            setupCache(1);
            return;
        }*/

        // disable generate button while map is being computed
        mGenerateButton.setDisable(true);
        mLoadingCircle.setVisible(true);

        // run map generation in a background thread (so UI doesn't freeze)
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        MapTask mapTask = new MapTask();
        executor.execute(mapTask);

        // listen to result of UI thread, display the map and reenable the generate button
        mapTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                //ImageView imageView = new ImageView();
                //imageView.setImage(SwingFXUtils.toFXImage(mapTask.getValue(), null));
                //mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));
                mCanvasAnchor.getChildren().add(mapTask.getValue());
                mGenerateButton.setDisable(false);
                mLoadingCircle.setVisible(false);
                mSeedEdited = false;
                //setupCache(3);
                //setZoom();

                if (mTerritoryGenCheckBox.isSelected()) {
                    mPoliticalMapCheckBox.setDisable(false);
                } else {
                    mPoliticalMapCheckBox.setDisable(true);
                }

                setZoom2();
            }
        });
    }

    // Cache n maps to be generated in a background thread
    /*private void setupCache(int n) {

        verifyFields();

        // run map generation cache in a background thread
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        MapTask mapTask = new MapTask();
        executor.execute(mapTask);

        mapTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                mMapImageCache.add(mapTask.getValue());
                if (n - 1 > 0) {
                    setupCache(n - 1);
                }
            }
        });
    }*/

    private void verifyFields() {
        if (!mWidthTextField.getText().matches("^-?\\d+$")) {
            mWidthTextField.setText(String.valueOf(WIDTH));
        }
        if (!mHeightTextField.getText().matches("^-?\\d+$")) {
            mHeightTextField.setText(String.valueOf(HEIGHT));
        }
        if (!isDouble(mPersistenceField.getText())) {
            mPersistenceField.setText(String.valueOf(PERSISTENCE));
        }
        if (!mOctavesField.getText().matches("^-?\\d+$")) { // if not integer
            mOctavesField.setText(String.valueOf(OCTAVES));
        }
        if (!isDouble(mLandGenTextField.getText())) {
            mLandGenTextField.setText(String.valueOf(LANDGEN));
        }
        if (!isDouble(mHillGenTextField.getText())) {
            mHillGenTextField.setText(String.valueOf(HILLGEN));
        }
        if (!isDouble(mMountainGenTextField.getText())) {
            mMountainGenTextField.setText(String.valueOf(MOUNTAINGEN));
        }
        if (!mCityGenTextField.getText().matches("^-?\\d+$")) {
            mCityGenTextField.setText(String.valueOf(CITYGEN));
        }
        mPoliticalMapCheckBox.setSelected(false);
    }

    private double determineSeed() {
        double seed;
        String seedField = mSeedField.getText();
        if (!seedField.isEmpty() && isDouble(seedField) && mSeedEdited) {
            seed = Double.parseDouble(seedField);
        } else {
            seed = Math.random();
            mSeedField.setText(String.valueOf(seed));
        }
        return seed;
    }

    private boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private class MapTask extends Task<ImageView> {

        private PerlinMap map;

        MapTask() {
            this.map = new PerlinMap(Integer.parseInt(mWidthTextField.getText()),
                    Integer.parseInt(mHeightTextField.getText()),
                    determineSeed(),
                    Math.random(),
                    Double.parseDouble(mLandGenTextField.getText()),
                    WATERGEN,
                    Double.parseDouble(mMountainGenTextField.getText()),
                    Double.parseDouble(mHillGenTextField.getText()),
                    BEACHGEN,
                    FORESTGEN,
                    Integer.parseInt(mCityGenTextField.getText()),
                    Double.parseDouble(mPersistenceField.getText()),
                    Integer.parseInt(mOctavesField.getText()),
                    mLandGenCheckBox.isSelected(),
                    mHillGenCheckBox.isSelected(),
                    mMountainGenCheckBox.isSelected(),
                    mRiverGenCheckBox.isSelected(),
                    mCityGenCheckBox.isSelected(),
                    mNameGenCheckBox.isSelected(),
                    mContinentGenCheckBox.isSelected(),
                    mTerritoryGenCheckBox.isSelected());
        }

        @Override
        protected ImageView call() {
            try {
                mImageManager = new ImageManager(map);
                mImageManager.generateImage();
                mImage = mImageManager.getImage();
                ImageView imageView = new ImageView();
                imageView.setImage(SwingFXUtils.toFXImage(mImage, null));
                imageView.setId("mapImage");
                return imageView;
            } catch (Exception e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "test", e);
            }
            return null;
        }
    }
}
