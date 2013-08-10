package com.remote.remote2d;

import com.remote.remote2d.logic.Vector2;

public abstract class Remote2DGame {
	
	/**
	 * This is called when the Remote2D instance starts up.  Put any Remote2D-based initialization code here.
	 */
	public abstract void initGame();
	
	/**
	 * Supplies the local path to any icons.  These are shown on the window and in the taskbar in windows/linux, and in the dock on mac.
	 * 
	 * For full support, you need at least:<ul><li>
	 * One 16x16 and one 32x32 icon for full windows support</li><li>
	 * One 32x32 icon for full Linux (and similar platforms) support</li><li>
	 * One 128x128 icon for full Mac support</li></ul>
	 * @return A string array with icons to use, or null for the default LWJGL icon.
	 */
	public String[] getIconPath()
	{
		return null;
	}
	
}
