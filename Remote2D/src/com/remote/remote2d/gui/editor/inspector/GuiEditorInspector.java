package com.remote.remote2d.gui.editor.inspector;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.esotericsoftware.minlog.Log;
import com.remote.remote2d.Remote2D;
import com.remote.remote2d.art.Renderer;
import com.remote.remote2d.entity.EditorObject;
import com.remote.remote2d.entity.Entity;
import com.remote.remote2d.gui.Gui;
import com.remote.remote2d.gui.GuiButton;
import com.remote.remote2d.gui.GuiMenu;
import com.remote.remote2d.gui.editor.GuiEditor;
import com.remote.remote2d.gui.editor.GuiWindowInsertComponent;
import com.remote.remote2d.gui.editor.operation.OperationEditEntity;
import com.remote.remote2d.logic.Interpolator;
import com.remote.remote2d.logic.Vector2;
import com.remote.remote2d.logic.Vector2;

public class GuiEditorInspector extends GuiMenu {
		
	public EditorObject currentEntity;
	private ArrayList<EditorObjectWizard> wizards;
	private GuiButton button;
	private GuiEditor editor;
	public float offset = 0;
	private float lastOffset = 0;
	public Vector2 pos;
	public Vector2 dim;

	public GuiEditorInspector(Vector2 pos, Vector2 dim, GuiEditor editor)
	{
		super();
		this.pos = pos;
		this.dim = dim;
		this.editor = editor;
		wizards = new ArrayList<EditorObjectWizard>();
		initGui();
	}
	
	@Override
	public void initGui()
	{
		buttonList.clear();
		if(pos != null && dim != null)
		{
			button = new GuiButton(0,new Vector2(pos.x+dim.x/2-100,pos.y+dim.y-20),new Vector2(200,20),"Apply");
			button.setDisabled(currentEntity == null);
			buttonList.add(button);
		}
		
		
	}
	
	@Override
	public void tick(int i, int j, int k) {
		super.tick(i, j, k);
		
		lastOffset = offset;
		
		if(pos.getColliderWithDim(dim).isPointInside(new Vector2(i,j)))
		{
			if(Remote2D.getInstance().getDeltaWheel() < 0)
				offset += 20;
			if(Remote2D.getInstance().getDeltaWheel() > 0)
				offset -= 20;
			
			if(offset > getTotalComponentHeight()-dim.y+20)
				offset = getTotalComponentHeight()-dim.y+20;
			if(offset < 0)
				offset = 0;
		}
		
		for(int x=0;x<wizards.size();x++)
		{
			wizards.get(x).tick(i,(int)(j+offset),k);
			
			//int changed = wizards.get(x).hasFieldBeenChanged();
			//if(changed != -1)
			//{
			//	wizards.get(x).setComponentField(changed);
			//	if(currentEntity instanceof Entity)
			//		editor.replaceSelectedEntity((Entity)currentEntity);
			//}
		}
	}
	
	@Override
	public void renderBackground(float interpolation)
	{
		Renderer.drawRect(pos, dim, 0x000000, 0.5f);
	}
	
	@Override
	public void render(float interpolation) {
		super.render(interpolation);
		
		GL11.glPushMatrix();
		Renderer.startScissor(new Vector2(pos.x,pos.y+20), new Vector2(dim.x,dim.y-20));
		GL11.glTranslatef(0, -(float)Interpolator.linearInterpolate(lastOffset, offset, interpolation), 0);
		
		for(int x=0;x<wizards.size();x++)
		{
			wizards.get(x).render(interpolation);
		}
		Renderer.endScissor();
		GL11.glPopMatrix();
	}
	
	public void setCurrentEntity(EditorObject o)
	{
		this.currentEntity = o;
		wizards.clear();
		button.setDisabled(currentEntity == null);
		if(currentEntity==null)
			return;
		//Log.debug("Setting Inspector Entity:"+e.name);
		Vector2 currentPos = pos.copy();
		EditorObjectWizard ew = new EditorObjectWizard(currentEntity,currentPos,(int)dim.x);
		wizards.add(ew);
		currentPos.y += ew.getHeight();
		if(o instanceof Entity)
		{
			Entity e = (Entity)o;
			
			for(int x=0;x<e.getComponents().size();x++)
			{
				EditorObjectWizard cw = new EditorObjectWizard(e.getComponents().get(x),currentPos,(int)dim.x);
				wizards.add(cw);
				currentPos.y += cw.getHeight();
			}
		}
		
		//Log.debug("Component Count: "+components.size());
	}
	
	public void apply()
	{
		EditorObject clone = null;
		if(currentEntity instanceof Entity)
			clone = ((Entity)currentEntity).clone();
		for(int x=0;x<wizards.size();x++)
		{
			wizards.get(x).setComponentFields();
		}
		if(currentEntity instanceof Entity)
		{
			Entity before = (Entity)clone;
			Entity after = ((Entity)currentEntity).clone();
			editor.executeOperation(new OperationEditEntity(editor,before,after));
		}
	}
	
	public boolean isTyping()
	{
		for(int x=0;x<wizards.size();x++)
			if(wizards.get(x).isFieldSelected())
				return true;
		return false;
	}
	
	public int getTotalComponentHeight()
	{
		int height = 0;
		for(int x=0;x<wizards.size();x++)
		{
			height += wizards.get(x).getHeight();
		}
		return height;
	}
	
	@Override
	public void actionPerformed(GuiButton button)
	{
		if(button.id == 0)
		{
			apply();
		}
	}

}
