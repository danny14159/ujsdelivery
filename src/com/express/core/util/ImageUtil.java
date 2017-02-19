package com.express.core.util;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;


public class ImageUtil {
	
	/**将原图（大图）按比例裁剪成缩略图
	 * @param src
	 * @param dx
	 * @param dy
	 * @return
	 */
	public static BufferedImage fillImage(BufferedImage src,int dx,int dy){
		BufferedImage tag = null;
		
		int sx = src.getWidth();
		int sy = src.getHeight();
		boolean flag = true;
		double srcScale = (double)sx/sy;
		double dstScale = (double)dx/dy;
		
		
		if(flag){//缩小
			if(srcScale > dstScale){
				double scale = (double)dy/sy;
				int width = (int) (sx * scale);
				int height = (int) (sy * scale);
				Image image = src.getScaledInstance(width, height,
						Image.SCALE_DEFAULT);
				
				tag = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(image, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				//切割
				int x = (width - dx)/2;
				ImageFilter cropFilter = new CropImageFilter(x, 0, dx,
						dy);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(), cropFilter));
				tag = new BufferedImage(dx, dy,
						BufferedImage.TYPE_INT_RGB);
				g = tag.getGraphics();
				g.drawImage(img, 0, 0, dx, dy, null); // 绘制切割后的图
				g.dispose();
				
			}else{
				double scale = (double)dx/sx;
				int width = (int) (sx * scale);
				int height = (int) (sy * scale);
				Image image = src.getScaledInstance(width, height,
						Image.SCALE_DEFAULT);
				tag = new BufferedImage(width, height,
						BufferedImage.TYPE_INT_RGB);
				Graphics g = tag.getGraphics();
				g.drawImage(image, 0, 0, null); // 绘制缩小后的图
				g.dispose();
				//切割
				int y = (height - dy)/2;
				ImageFilter cropFilter = new CropImageFilter(0, y, dx,
						dy);
				Image img = Toolkit.getDefaultToolkit().createImage(
						new FilteredImageSource(image.getSource(), cropFilter));
				tag = new BufferedImage(dx, dy,
						BufferedImage.TYPE_INT_RGB);
				g = tag.getGraphics();
				g.drawImage(img, 0, 0, dx, dy, null); // 绘制切割后的图
				g.dispose();
				
				/*try {
					ImageIO.write(tag, "JPEG", new File("e:/abc.jpg"));
				} catch (IOException e) {
					e.printStackTrace();
				}// 输出到文件流
*/			}
			
		}else{//放大
			
		}
		
		return tag;
	}

}
