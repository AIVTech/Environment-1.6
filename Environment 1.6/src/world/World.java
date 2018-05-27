package world;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import entities.Entity;
import light.Light;
import loaders.StaticLoader;
import loaders.TerrainLoader;
import terrain.Terrain;

public class World {
	
	public ModelManager models;
	
	public List<Entity> entities = new ArrayList<Entity>();
	public List<Terrain> terrains = new ArrayList<Terrain>();
	public Terrain[][] terrainGrid;
	public List<Light> lights = new ArrayList<Light>();
	
	public World(StaticLoader loader) {
		models = new ModelManager(loader);
		init(loader);
	}
	
	private void init(StaticLoader loader) {
		loadTerrains(loader);
		loadEntities();
		loadLights();
	}
	
	private void loadTerrains(StaticLoader loader) {
		terrains = TerrainLoader.loadFromFile("Assets/Terrains/forestTown.ter", loader);
		this.terrainGrid = TerrainLoader.getTerrainGrid(terrains);
	}
	
	private void loadEntities() {
		
	}
	
	private void loadLights() {
		Light sun = new Light(new Vector3f(1000, 100000, 1000), new Vector3f(0.9f, 0.9f, 0.8f));
		lights.add(sun);
	}
	
	public float getHeightAtWorldPoint(float x, float z) {
		Terrain terr = getTerrain(x, z);
		return terr.getHeightOfTerrain(x, z);
	}
	
	private Terrain getTerrain(float worldX, float worldZ) {
		int x = (int) (worldX / Terrain.SIZE);
		int z = (int) (worldZ / Terrain.SIZE);
		if (z < 0 || x < 0) {
			return null;
		}
		return terrainGrid[x][z];
	}
}
