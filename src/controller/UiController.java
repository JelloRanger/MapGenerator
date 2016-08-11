package controller;

import image.Superman;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import map.PerlinMap;

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
    private Button mRiverGenDefaultButton;

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
    private TextField mRiverGenTextField;

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
    private MenuItem mMenuClose;

    private Stage mStage;

    private BufferedImage mImage;

    private boolean mSeedEdited = false;

    private final static int WIDTH = 1000;

    private final static int HEIGHT = 1000;

    private final static double LANDGEN = 0;

    private final static double WATERGEN = -0.5;

    private final static double MOUNTAINGEN = 0.5;

    private final static double HILLGEN = 0.4;

    private final static double BEACHGEN = -0.0125;

    private final static double FORESTGEN = 0.2;

    private static final double PERSISTENCE = 0.5;

    private static final int OCTAVES = 8;

    public void initialize() {

        mMenuClose.setOnAction(this::handleMenuClose);

        mPersistenceField.setText(String.valueOf(PERSISTENCE));
        mOctavesField.setText(String.valueOf(OCTAVES));

        generateMap();

        //setZoom();

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
        mRiverGenDefaultButton.setOnAction(this::handleRiverGenDefaultButton);
        mCityGenDefaultButton.setOnAction(this::handleCityGenDefaultButton);
    }

    /*private void setZoom() {
        ImageView imageView = new ImageView();
        DoubleProperty zoomProperty = new SimpleDoubleProperty(200);

        zoomProperty.addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                imageView.setFitWidth(zoomProperty.get() * 4);
                imageView.setFitHeight(zoomProperty.get() * 3);
            }
        });

        mCanvasAnchor.addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaY() > 0) {
                    zoomProperty.set(zoomProperty.get() * 1.1);
                } else if (event.getDeltaY() < 0) {
                    zoomProperty.set(zoomProperty.get() / 1.1);
                }
            }
        });

        WritableImage writableImage = new WritableImage((int) mImageManager.getCanvas().getWidth(),
                (int) mImageManager.getCanvas().getHeight());
        mImageManager.getCanvas().snapshot(null, writableImage);
        imageView.setImage(writableImage);
        imageView.preserveRatioProperty().set(true);
        //mCanvasAnchor.setContent(imageView);
    }*/

    public void setStageAndSetupListeners(Stage stage) {
        mStage = stage;
    }

    private void handleMenuClose(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void handleGenerateButtonAction(ActionEvent event) {
        generateMap();
        //setZoom();
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
    }

    private void handleHillGenDefaultButton(ActionEvent event) {
        mHillGenTextField.setText(String.valueOf(HILLGEN));
    }

    private void handleMountainGenDefaultButton(ActionEvent event) {
        mMountainGenTextField.setText(String.valueOf(MOUNTAINGEN));
    }

    private void handleRiverGenDefaultButton(ActionEvent event) {
        mRiverGenTextField.setText(String.valueOf(""));
    }

    private void handleCityGenDefaultButton(ActionEvent event) {
        mCityGenTextField.setText(String.valueOf(""));
    }


    // TODO: get save image working again without deprecated ImageManager
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

        verifyFields();

        // disable generate button while map is being computed
        mGenerateButton.setDisable(true);

        // run map generation in a background thread (so UI doesn't freeze)
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        MapTask mapTask = new MapTask();
        executor.execute(mapTask);

        // listen to result of UI thread, display the map and reenable the generate button
        mapTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                ImageView imageView = new ImageView();
                imageView.setImage(SwingFXUtils.toFXImage(mapTask.getValue(), null));
                mCanvasAnchor.getChildren().clear();
                mCanvasAnchor.getChildren().add(imageView);
                mGenerateButton.setDisable(false);
                mSeedEdited = false;
            }
        });
    }

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

    private class MapTask extends Task<BufferedImage> {

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
                    Double.parseDouble(mPersistenceField.getText()),
                    Integer.parseInt(mOctavesField.getText()),
                    mLandGenCheckBox.isSelected(),
                    mHillGenCheckBox.isSelected(),
                    mMountainGenCheckBox.isSelected(),
                    mRiverGenCheckBox.isSelected(),
                    mCityGenCheckBox.isSelected(),
                    mNameGenCheckBox.isSelected());
        }

        @Override
        protected BufferedImage call() {
            try {
                Superman superman = new Superman(map);
                superman.generateImage();
                mImage = superman.getImage();
                return mImage;
            } catch (Exception e) {
                Logger.getLogger(TAG).log(Level.SEVERE, "test", e);
            }
            return null;
        }
    }
}
