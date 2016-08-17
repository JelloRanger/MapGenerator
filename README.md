# MapMaker
Generate realistic fictional maps

![MapMaker v0](http://i.imgur.com/PZYG53P.png "MapMaker v0")

MapMaker is a multiplatform program written in Java that generates maps using random and procedural generation to create realistic fictional worlds. Users can customize their maps via a variety of parameters:
* Persistence - The greater the persistence, the more "broken" up the map is and vice versa
* Octaves - The greater the number of octaves, the more detailed the map is and vice versa
* Land/Hill/Mountain Gen - The lower the number, the more terrain of that type and vice versa

You can also view political maps for your world by toggling the political map check box.

![Political Map View](http://i.imgur.com/cHDWbal.png?1 "Political Map View")

The underlying algorithm behind MapMaker is Perlin Noise, which efficiently and randomly generates a two dimensional height map. 

[Read more about the creation of this application here](https://medium.com/@JelloRanger/random-and-procedural-map-generation-part-1-noise-and-maps-cc78fc776172)
