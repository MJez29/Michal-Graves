//Michal Jez
//09/05/2016
//This class provides methods for performing basic operations to images
//It can load 1 or more images based on the parameters passed in
//It can also flip images horizontally and vertically
//As well as create new copies of the image
//And lastly it can scale images to different sizes using a variety of algorithms

import java.awt.image.*;
import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.io.*;
import java.awt.geom.*;

public class ImageTools 
{
    public ImageTools() {}
    
    public static BufferedImage initializeImage(String str)
	{	//Initializes an image given a filename
		try
		{
			BufferedImage img = ImageIO.read(new File(str));
			return img;
		}
		catch (IOException e) { return null; }
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

	public static HashMap<String, BufferedImage> initializeImagesAsHashMap(String base, int n)
	{	//Returns a HashMap of image that all have the same base filename
		HashMap<String, BufferedImage> hm = new HashMap<String, BufferedImage> (n);
		for (int i = 0; i < n; i++)
		{
			hm.put(base + i, initializeImage(base + i + ".png"));
		}
		return hm;
	}

	public static BufferedImage scale(BufferedImage bi, double xf, double yf)
	{
        BufferedImage sbi = null;
    	if(bi != null) {
	        sbi = new BufferedImage((int) (bi.getWidth() * xf), (int) (bi.getHeight() * yf), bi.getType());
	        Graphics2D g = sbi.createGraphics();
	        AffineTransform at = AffineTransform.getScaleInstance(xf, yf);
	        g.drawRenderedImage(bi, at);
	    }
	    return sbi;
	}

	public static BufferedImage scaleImage(BufferedImage bi, double xf, double yf)
	{	//For later use to speed up scaling
		BufferedImage sbi = null;
		if (bi != null)
		{
			int nw = (int) (bi.getWidth() * xf);
			int nh = (int) (bi.getHeight() * yf);
			sbi = new BufferedImage(nw, nh, bi.getType());
			Graphics2D g = sbi.createGraphics();
			//temp = new ImageIcon().getImage();
			//Graphics2D g2d = (Graphics2D) temp.getGraphics();
			g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g.drawImage(bi, 0, 0, nw, nh, null);
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

	public static BufferedImage[] invertOrder(BufferedImage[] bis)
	{	//Returns a new inverted array of the array passed in
		BufferedImage[] arr = new BufferedImage[bis.length];

		int halfLength = bis.length / 2;

		//Goes through every item
		//Flips through 2 objects at a time
		int n;
		for (int i = 0; i < halfLength; i++)
		{
			n = bis.length - 1 - i;
			arr[i] = bis[n];
			arr[n] = bis[i];
		}

		//If there is an odd number of images the middle one remains the same in both arrays
		if (bis.length % 2 > 0)
		{
			arr[halfLength] = bis[halfLength];
		}
		return arr;
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