package controller;

import image.ImageManager;
import image.ZoomManager;
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
        ZoomManager zoomManager = new ZoomManager(mImage, mCanvasAnchor);
        ImageView imageView = zoomManager.startZoom();
        imageView.setId("mapImage");
        mCanvasAnchor.getChildren().remove(mCanvasAnchor.lookup("#mapImage"));
        mCanvasAnchor.getChildren().add(imageView);
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
        setZoom();
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

                setZoom();
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
