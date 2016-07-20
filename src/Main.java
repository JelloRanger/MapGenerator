import graphics.ColorMapDialog;
import graphics.SmoothColorMapDialog;
import graphics.BasicNoiseDialog;
import map.*;
import noise.Noise;
import noise.PerlinNoise;
import noise.RandomNoise;
import noise.SmoothNoise;

public class Main {

	private final static int WIDTH = 1000;

	private final static int HEIGHT = 1000;

	private final static double LANDGEN = 0.1;

	private final static double WATERGEN = -0.5;

	private final static double MOUNTAINGEN = 0.5;

	private final static double BEACHGEN = -0.0125;

	private final static double FORESTGEN = 0.2;

	public static void main(String[] args) {

		double seed = Math.random();
		double seedForest = Math.random();

		System.out.println("seed: " + seed);
		System.out.println("forest seed: " + seedForest);

		//displayRandomMap(WIDTH, HEIGHT, LANDGEN, WATERGEN);
		//displaySmoothMap(WIDTH, HEIGHT, DEGREES, LANDGEN, WATERGEN);
		//displayRevisedSmoothMap(WIDTH, HEIGHT, LANDGEN, WATERGEN);
		//displayRandomNoise(WIDTH, HEIGHT);
		//displaySmoothNoise(WIDTH, HEIGHT, 5);
		//displaySmoothNoise(WIDTH, HEIGHT, 10);
		//displayPerlinNoise(WIDTH, HEIGHT, randomSeed);
		//displayPerlinMap(WIDTH, HEIGHT, randomSeed, LANDGEN, WATERGEN);
		displayPerlinMapWithTexturing(WIDTH, HEIGHT, seed, seedForest, LANDGEN, WATERGEN, MOUNTAINGEN, BEACHGEN, FORESTGEN);
	}
	
	// DIALOGS
	private static void openBasicNoiseDialog(Noise noise) {
		BasicNoiseDialog.plotNoise(noise);
	}
	
	private static void openColorMapDialog(Map map) {
		ColorMapDialog dialog = new ColorMapDialog();
		dialog.plotMap(map);
	}
	
	private static void openSmoothColorMapDialog(Map map) {
		SmoothColorMapDialog dialog = new SmoothColorMapDialog();
		dialog.plotMap(map);
	}
	
	// MAPS
	private static void displayRandomMap(int width,
										 int height,
										 double seed,
										 double seedForest,
										 double landGen,
										 double waterGen,
										 double mountainGen,
										 double beachGen,
										 double forestGen) {

		RandomMap randomMap = new RandomMap(width,
				height,
				seed,
				seedForest,
				landGen,
				waterGen,
				mountainGen,
				beachGen,
				forestGen);

		randomMap.generateMap();
		openColorMapDialog(randomMap);
	}
	
	private static void displaySmoothMap(int width,
										 int height,
										 int degrees,
										 double seed,
										 double seedForest,
										 double landGen,
										 double waterGen,
										 double mountainGen,
										 double beachGen,
										 double forestGen) {

		SmoothMap smoothMap = new SmoothMap(width,
				height,
				seed,
				seedForest,
				degrees,
				landGen,
				waterGen,
				mountainGen,
				beachGen,
				forestGen);

		smoothMap.generateMap();
		openColorMapDialog(smoothMap);
		//openSmoothColorMapDialog(smoothMap);
	}

	private static void displayRevisedSmoothMap(int width,
												int height,
												double seed,
												double seedForest,
												double landGen,
												double waterGen,
												double mountainGen,
												double beachGen,
												double forestGen) {

		RevisedSmoothMap revisedSmoothMap = new RevisedSmoothMap(width,
				height,
				seed,
				seedForest,
				landGen,
				waterGen,
				mountainGen,
				beachGen,
				forestGen);

		revisedSmoothMap.generateMap();
		openColorMapDialog(revisedSmoothMap);
		//openSmoothColorMapDialog(revisedSmoothMap);
	}

	private static void displayPerlinMap(int width,
										 int height,
										 double seed,
										 double seedForest,
										 double landGen,
										 double waterGen,
										 double mountainGen,
										 double beachGen,
										 double forestGen) {

		PerlinMap perlinMap = new PerlinMap(width,
				height,
				seed,
				seedForest,
				landGen,
				waterGen,
				mountainGen,
				beachGen,
				forestGen);

		perlinMap.generateMap();
		openColorMapDialog(perlinMap);
	}

	// MAPS WITH TEXTURING

	private static void displayPerlinMapWithTexturing(int width,
													  int height,
													  double seed,
													  double seedForest,
													  double landGen,
													  double waterGen,
													  double mountainGen,
													  double beachGen,
													  double forestGen) {

		PerlinMap perlinMap = new PerlinMap(width,
				height,
				seed,
				seedForest,
				landGen,
				waterGen,
				mountainGen,
				beachGen,
				forestGen);

		perlinMap.generateMap();
		openSmoothColorMapDialog(perlinMap);
	}
	
	// NOISES
	private static void displayRandomNoise(int width, int height, double seed) {
		RandomNoise randomNoise = new RandomNoise(width, height, seed);
		randomNoise.initializeNoiseGrid();
		openBasicNoiseDialog(randomNoise);
	}
	
	private static void displaySmoothNoise(int width, int height, double seed, int degrees) {
		SmoothNoise smoothNoise = new SmoothNoise(width, height, seed, degrees);
		smoothNoise.initializeNoiseGrid();
		openBasicNoiseDialog(smoothNoise);
	}
	
	private static void displayPerlinNoise(int width, int height, double seed) {
		PerlinNoise perlinNoise = new PerlinNoise(width, height, seed, 0.5, 8);
		perlinNoise.initializeNoiseGrid();
		openBasicNoiseDialog(perlinNoise);
	}

}
