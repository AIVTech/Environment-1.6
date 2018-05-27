package display;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.PixelFormat;

import world.WorldTime;

public class DisplayManager {

	public static int DISPLAY_WIDTH = 1920;
	public static int DISPLAY_HEIGHT = 1080;
	private static int preferedFPS_CAP = 500;

	private static long lastFrameTime;
	private static float delta;
	private static long timeElapsed = 0;
	private static long startTime = 0;
	private static boolean firstTimeLaunching = true;

	public static void setDisplayMode(int width, int height, boolean fullscreen) {
		 
	    // return if requested DisplayMode is already set
	    if ((Display.getDisplayMode().getWidth() == width) && 
	        (Display.getDisplayMode().getHeight() == height) && 
	    (Display.isFullscreen() == fullscreen)) {
	        return;
	    }
	 
	    try {
	        DisplayMode targetDisplayMode = null;
	         
	    if (fullscreen) {
	        DisplayMode[] modes = Display.getAvailableDisplayModes();
	        int freq = 0;
	                 
	        for (int i=0;i<modes.length;i++) {
	            DisplayMode current = modes[i];
	                     
	        if ((current.getWidth() == width) && (current.getHeight() == height)) {
	            if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
	                if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
	                targetDisplayMode = current;
	                freq = targetDisplayMode.getFrequency();
	                        }
	                    }
	 
	            // if we've found a match for bpp and frequence against the 
	            // original display mode then it's probably best to go for this one
	            // since it's most likely compatible with the monitor
	            if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
	                        (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
	                            targetDisplayMode = current;
	                            break;
	                    }
	                }
	            }
	        } else {
	            targetDisplayMode = new DisplayMode(width,height);
	        }
	 
	        if (targetDisplayMode == null) {
	            System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
	            return;
	        }
	 
	        Display.setDisplayMode(targetDisplayMode);
	        Display.setFullscreen(fullscreen);
	             
	    } catch (LWJGLException e) {
	        System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
	    }
	}

	
	public static void createDisplay() {

		ContextAttribs attribs = new ContextAttribs(4, 4).withForwardCompatible(true).withProfileCore(true);

		try {
			Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
			//setDisplayMode(1920, 1080, true);
			Display.setLocation(0, 0);
			Display.create(new PixelFormat().withSamples(8).withDepthBits(24), attribs); // setting the format
			Display.setTitle("Environment 1.6"); // setting the title
			Mouse.setGrabbed(true); // Capture the cursor
			GL11.glEnable(GL13.GL_MULTISAMPLE);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}

		// telling OpenGL where to setup the display
		GL11.glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		lastFrameTime = getCurrentTime();
	}

	public static void updateDisplay() {
		Display.sync(preferedFPS_CAP); // lets it sync with a constant fps to avoid lag
		Display.update();
		long currentFrameTime = getCurrentTime();
		delta = (currentFrameTime - lastFrameTime) / 1000f; // this will give us the time it took the frame to render in
															// seconds.
		lastFrameTime = currentFrameTime;

		if (firstTimeLaunching) {
			startTime = System.currentTimeMillis();
			firstTimeLaunching = false;
		}

		updateWorldTimer();
	}

	public static float getFrameTimeSeconds() {
		return delta;
	}

	public static void closeDisplay() {
		Display.destroy();
		// any additional closing statements

	}

	private static void updateWorldTimer() {
		long currentTime = System.currentTimeMillis();
		timeElapsed = currentTime - startTime;
		long elapsedSeconds = timeElapsed / 1000;
		long elapsedMinutes = elapsedSeconds / 60;
		long elapsedHours = elapsedMinutes / 60;
		
		WorldTime.elapsedSeconds = (int) elapsedSeconds;
		WorldTime.elapsedMinutes = (int) elapsedMinutes;
		WorldTime.elapsedHours = (int) elapsedHours;

		WorldTime.inGameSeconds += elapsedSeconds;
		WorldTime.inGameMinutes += elapsedMinutes;
		WorldTime.inGameHours += elapsedHours;

	}

	private static long getCurrentTime() {
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}

}
