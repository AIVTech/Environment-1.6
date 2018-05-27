package engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import camera.FpsCamera;
import display.DisplayManager;
import entities.Player;
import loaders.StaticLoader;
import rendering.CoreRenderer;
import world.World;

public class Engine {

	public static void main(String[] args) {
		DisplayManager.createDisplay();

		/*****************************************************/
		// ================= PREPARATIONS ====================//

		StaticLoader loader = new StaticLoader();
		CoreRenderer renderer = new CoreRenderer();
		
		World world = new World(loader);

		FpsCamera camera = new FpsCamera(world.terrainGrid);
		
		Player player = new Player(world.models.human, new Vector3f(600, world.getHeightAtWorldPoint(600, 500), 500), 0, 80, 0, 0.6f);
		world.entities.add(player);
		
		// ***************************************************//

		while (!Display.isCloseRequested()) {

			// Logic
			camera.update();

			// Rendering
			renderer.prepare();
			renderer.renderScene(world.entities, world.terrains, camera, world.lights);

			// Processing
			ProcessWindowInput();

			DisplayManager.updateDisplay();
		}

		// Clean Up
		renderer.cleanUp();
		loader.cleanUp();

		Display.destroy();
	}

	private static void ProcessWindowInput() {
		// Close window on escape
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
			System.exit(0);
	}

}
