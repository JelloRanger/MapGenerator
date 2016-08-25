package model;

public enum BiomeType {

    TROPICALRAINFOREST,
    SAVANNA,
    SUBTROPICALDESERT,
    TEMPERATERAINFOREST,
    TEMPERATESEASONALFOREST,
    SHRUBLAND,
    COLDDESERT,
    BOREALFOREST,
    TUNDRA;

    /*public static BiomeType getBiomeTypeByTempAndHumidity(double temperature, double humidity) {
        if (temperature > 0.75) {
            if (humidity > 0.66) {
                return TROPICALRAINFOREST;
            } else if (humidity > 0.3) {
                return SAVANNA;
            } else {
                return SUBTROPICALDESERT;
            }
        } else if (temperature > 0.5) {
            if (humidity > 0.25) {
                return TEMPERATERAINFOREST;
            } else if (humidity > 0.125) {
                return TEMPERATESEASONALFOREST;
            } else if (humidity > 0.0625) {
                return SHRUBLAND;
            } else {
                return COLDDESERT;
            }
        } else if (temperature > 0.25) {
            if (humidity > 0.125) {
                return BOREALFOREST;
            } else if (humidity > 0.9) {
                return SHRUBLAND;
            } else {
                return COLDDESERT;
            }
        } else {
            return TUNDRA;
        }
    }*/

    public static BiomeType getBiomeTypeByTempAndHumidity(double temperature, double humidity) {
        if (temperature > 0.65) {
            if (humidity > 0.5) {
                return TROPICALRAINFOREST;
            } else if (humidity > 0.25) {
                return SAVANNA;
            } else {
                return SUBTROPICALDESERT;
            }
        } else if (temperature > 0.25) {
            if (humidity > 0.25) {
                return TEMPERATERAINFOREST;
            } else if (humidity > 0.065) {
                return SHRUBLAND;
            } else {
                return COLDDESERT;
            }
        } else if (temperature > 0.125) {
            return BOREALFOREST;
        } else {
            return TUNDRA;
        }
    }

}
