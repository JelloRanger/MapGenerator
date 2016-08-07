package controller;

import image.ImageManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import map.PerlinMap;

public class UiController {

    @FXML
    private AnchorPane mRootAnchor;

    @FXML
    private AnchorPane mCanvasAnchor;

    @FXML
    private Button mGenerateButton;

    @FXML
    private Button mPersistenceDefaultButton;

    @FXML
    private Button mOctavesDefaultButton;

    @FXML
    private TextField mSeedField;

    @FXML
    private TextField mPersistenceField;

    @FXML
    private TextField mOctavesField;

    @FXML
    private MenuItem mMenuClose;

    private ImageManager mImageManager;

    private boolean mSeedEdited = false;

    private final static int WIDTH = 1000;

    private final static int HEIGHT = 1000;

    private final static double LANDGEN = 0;

    private final static double WATERGEN = -0.5;

    private final static double MOUNTAINGEN = 0.5;

    private final static double HILLGEN = 0.4;

    private final static double BEACHGEN = -0.0125;

    private final static double FORESTGEN = 0.2;

    protected static final double PERSISTENCE = 0.5;

    protected static final int OCTAVES = 8;

    public void initialize() {

        mMenuClose.setOnAction(this::handleMenuClose);

        mImageManager = new ImageManager(WIDTH,HEIGHT);
        mPersistenceField.setText(String.valueOf(PERSISTENCE));
        mOctavesField.setText(String.valueOf(OCTAVES));

        generateMap();
        mImageManager.getCanvas().setId("mCanvasMap");
        mCanvasAnchor.getChildren().add(mImageManager.getCanvas());

        mGenerateButton.setOnAction(this::handleGenerateButtonAction);
        mPersistenceDefaultButton.setOnAction(this::handlePersistenceDefaultButtonAction);
        mOctavesDefaultButton.setOnAction(this::handleOctavesDefaultButtonAction);
        mSeedField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                mSeedEdited = true;
            }
        }));
    }

    private void handleMenuClose(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    private void handleGenerateButtonAction(ActionEvent event) {
        generateMap();
    }

    private void handlePersistenceDefaultButtonAction(ActionEvent event) {
        mPersistenceField.setText(String.valueOf(PERSISTENCE));
    }

    private void handleOctavesDefaultButtonAction(ActionEvent event) {
        mOctavesField.setText(String.valueOf(OCTAVES));
    }

    private void generateMap() {

        verifyFields();
        PerlinMap map = new PerlinMap(WIDTH, HEIGHT, determineSeed(), Math.random(), LANDGEN, WATERGEN, MOUNTAINGEN,
                HILLGEN, BEACHGEN, FORESTGEN, Double.parseDouble(mPersistenceField.getText()),
                Integer.parseInt(mOctavesField.getText()));
        map.generateMap();
        mImageManager.setMap(map);
        mImageManager.generateImage();
        mSeedEdited = false;
    }

    private void verifyFields() {
        if (!isDouble(mPersistenceField.getText())) {
            mPersistenceField.setText(String.valueOf(PERSISTENCE));
        }
        if (!mOctavesField.getText().matches("^-?\\d+$")) { // if not integer
            mOctavesField.setText(String.valueOf(OCTAVES));
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
}
