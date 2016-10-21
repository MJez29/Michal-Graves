/**
 * @(#)ImageTools.java
 *
 *
 * @Michal Jez 
 * @version 1.00 2016/3/27
 *
 *This class provides additional methods for image loading, modifying and transforming
 */

import java.awt.image.*;
import javax.imageio.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class ImageTools 
{

    public ImageTools() {}
    
    public static BufferedImage initializeImage(String str)
	{
		try
		{
			BufferedImage img = ImageIO.read(new File(str));
			System.out.println(img.getType() == BufferedImage.TYPE_INT_ARGB);
			return img;
		}
		catch (IOException e) { System.out.println(e); return null; }
	}
	
	public static BufferedImage[] initializeImages(String base, int n)
	{	//Returns an array of images that all have the base filename + n [0,n) + ".png"
		BufferedImage[] arr = new BufferedImage[n];
		for (int i = 0; i < n; i++)
		{
			arr[i] = initializeImage(base + i + ".png");
		}
		return arr;
	}
	
	private static BufferedImage scaled_bi;
	public static BufferedImage scale(BufferedImage bi, double xf, double yf)
	{
        
        BufferedImage sbi = null;
    	if(bi != null) {
	        sbi = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
	        Graphics2D g = sbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(xf, yf);
	        g.drawRenderedImage(bi, at);
	    }
	    return sbi;
	}
	
	public static BufferedImage copy(BufferedImage bi)
	{
		BufferedImage sbi = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
		Graphics2D g = sbi.createGraphics();
		g.drawImage(bi, 0, 0, null);
		return sbi;
	}
	
	public static BufferedImage flipHorizontally(BufferedImage img)
	{	//From javacodegeeks.com
		AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
		tx.translate(-img.getWidth(null), 0);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null);
	}
	
	public static BufferedImage[] flipHorizontally(BufferedImage[] imgs)
	{	//Flips every image in the array
		BufferedImage[] flipped = new BufferedImage[imgs.length];
		for (int i = 0; i < imgs.length; i++)
		{
			flipped[i] = flipHorizontally(imgs[i]);
		}
		return flipped;
	}
	
	public static BufferedImage flipVertically(BufferedImage img)
	{	//From javacodegeeks.com
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -img.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		return op.filter(img, null);
	}
	
	public static BufferedImage deepCopy(BufferedImage bi) 
	{	//From stack exchange
		//Returns a new copy of the image (changes to one will NOT affect the other
		ColorModel cm = bi.getColorModel();
		boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
		WritableRaster raster = bi.copyData(null);
		return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
	}
}