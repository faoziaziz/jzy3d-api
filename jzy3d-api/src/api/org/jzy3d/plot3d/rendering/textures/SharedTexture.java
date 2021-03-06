package org.jzy3d.plot3d.rendering.textures;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jzy3d.plot3d.primitives.IGLBindedResource;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class SharedTexture implements IGLBindedResource{
    protected SharedTexture() {
        this.texture = null;
    }
    
    public SharedTexture(String file) {
        this.texture = null;
        this.file = file;
    }

    public Texture getTexture(GL gl) {
        if (texture == null)
            mount(gl);
        else { // execute onmount even if we did not mount
               // if( action != null )
               // action.execute();
        }
        return texture;
    }

    /** A GL2 context MUST be current. */
    @Override
    public void mount(GL gl) {
        try {
            load(gl, file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        coords = texture.getImageTexCoords();
        halfWidth = texture.getWidth() / 2;
        halfHeight = texture.getHeight() / 2;
    }
    
    @Override
    public boolean hasMountedOnce(){
        return texture!=null;
    }

    protected void load(GL gl, String fileName) throws GLException, IOException {
        texture = TextureIO.newTexture(new File(fileName), useMipMap);
        texture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, textureMagnificationFilter);
        texture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, textureMinificationFilter);
    }
    
    protected void load(GL gl, BufferedImage image){
        
        texture = AWTTextureIO.newTexture(GLProfile.getDefault(), image, useMipMap);
        texture.setTexParameteri(gl, GL.GL_TEXTURE_MAG_FILTER, textureMagnificationFilter);
        texture.setTexParameteri(gl, GL.GL_TEXTURE_MIN_FILTER, textureMinificationFilter);
    }

    public String getFile() {
        return file;
    }

    public TextureCoords getCoords() {
        return coords;
    }

    public float getHalfWidth() {
        return halfWidth;
    }

    public float getHalfHeight() {
        return halfHeight;
    }
    
    public boolean isUseMipMap() {
        return useMipMap;
    }

    /**
     * Will apply if set before actually loading the texture.
     * 
     * @param useMipMap
     */
    public void setUseMipMap(boolean useMipMap) {
        this.useMipMap = useMipMap;
    }
    
    public int getTextureMagnificationFilter() {
        return textureMagnificationFilter;
    }

    /**
     * Will apply if set before actually loading the texture.
     * 
     * @param textureMagnificationFilter
     */
    public void setTextureMagnificationFilter(int textureMagnificationFilter) {
        this.textureMagnificationFilter = textureMagnificationFilter;
    }

    public int getTextureMinificationFilter() {
        return textureMinificationFilter;
    }

    /**
     * Will apply if set before actually loading the texture.
     * 
     * @param textureMinificationFilter
     */
    public void setTextureMinificationFilter(int textureMinificationFilter) {
        this.textureMinificationFilter = textureMinificationFilter;
    }

    protected Texture texture;
    protected String file;
    protected TextureCoords coords;
    protected float halfWidth;
    protected float halfHeight;
    protected boolean useMipMap = false;
    protected int textureMagnificationFilter = GL.GL_NEAREST;
    protected int textureMinificationFilter = GL.GL_NEAREST;
}
