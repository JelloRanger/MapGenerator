package map;


import noise.RevisedSmoothNoise;

public class RevisedSmoothMap extends RandomMap {

    public RevisedSmoothMap(int width, int height, double seed, double seedForest, double landGen, double waterGen,
                            double mountainGen, double hillGen, double beachGen, double forestGen, int cityGen) {
        super(width, height, seed, seedForest, landGen, waterGen, mountainGen, hillGen, beachGen, forestGen, cityGen);
    }

    @Override
    public void generateMap() {
        mNoise = new RevisedSmoothNoise(mWidth, mHeight, mSeed);
        mNoise.initializeMapGrid();
    }

}
