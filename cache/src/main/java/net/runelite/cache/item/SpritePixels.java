package net.runelite.cache.item;

import java.awt.image.BufferedImage;

public final class SpritePixels extends Rasterizer2D
{


	public int[] pixels;


	public int width;


	public int height;


	public int offsetX;


	int offsetY;


	public SpritePixels(int[] var1, int var2, int var3)
	{
		this.pixels = var1;
		this.width = var2;
		this.height = var3;
		this.offsetY = 0;
		this.offsetX = 0;
	}

	public SpritePixels(int var1, int var2)
	{
		this(new int[var2 * var1], var1, var2);
	}


	public void drawBorder(int color)
	{
		int[] newPixels = new int[this.width * this.height];
		int pixelIndex = 0;

		for (int y = 0; y < this.height; ++y)
		{
			for (int x = 0; x < this.width; ++x)
			{
				int pixel = this.pixels[pixelIndex];
				if (pixel == 0)
				{
					// W
					if (x > 0 && this.pixels[pixelIndex - 1] != 0)
					{
						pixel = color;
					}
					// N
					else if (y > 0 && this.pixels[pixelIndex - this.width] != 0)
					{
						pixel = color;
					}
					// E
					else if (x < this.width - 1 && this.pixels[pixelIndex + 1] != 0)
					{
						pixel = color;
					}
					// S
					else if (y < this.height - 1 && this.pixels[pixelIndex + this.width] != 0)
					{
						pixel = color;
					}
				}

				newPixels[pixelIndex++] = pixel;
			}
		}

		this.pixels = newPixels;
	}


	public void drawShadow(int color)
	{
		for (int y = this.height - 1; y > 0; --y)
		{
			int rowOffset = y * this.width;

			for (int x = this.width - 1; x > 0; --x)
			{
				// if *this* pixel is black/unset AND the pixel to the NW isn't black/unset
				if (this.pixels[x + rowOffset] == 0 && this.pixels[x + rowOffset - 1 - this.width] != 0)
				{
					this.pixels[x + rowOffset] = color;
				}
			}
		}

	}


	public void drawAt(int var1, int var2)
	{
		var1 += this.offsetX;
		var2 += this.offsetY;
		int var3 = var1 + var2 * Rasterizer2D.graphicsPixelsWidth;
		int var4 = 0;
		int var5 = this.height;
		int var6 = this.width;
		int var7 = Rasterizer2D.graphicsPixelsWidth - var6;
		int var8 = 0;
		int var9;
		if (var2 < Rasterizer2D.drawingAreaTop)
		{
			var9 = Rasterizer2D.drawingAreaTop - var2;
			var5 -= var9;
			var2 = Rasterizer2D.drawingAreaTop;
			var4 += var9 * var6;
			var3 += var9 * Rasterizer2D.graphicsPixelsWidth;
		}

		if (var5 + var2 > Rasterizer2D.drawingAreaRight)
		{
			var5 -= var5 + var2 - Rasterizer2D.drawingAreaRight;
		}

		if (var1 < Rasterizer2D.draw_region_x)
		{
			var9 = Rasterizer2D.draw_region_x - var1;
			var6 -= var9;
			var1 = Rasterizer2D.draw_region_x;
			var4 += var9;
			var3 += var9;
			var8 += var9;
			var7 += var9;
		}

		if (var6 + var1 > Rasterizer2D.drawingAreaBottom)
		{
			var9 = var6 + var1 - Rasterizer2D.drawingAreaBottom;
			var6 -= var9;
			var8 += var9;
			var7 += var9;
		}

		if (var6 > 0 && var5 > 0)
		{
			method5843(Rasterizer2D.graphicsPixels, this.pixels, 0, var4, var3, var6, var5, var7, var8);
		}
	}


	static void method5843(int[] var0, int[] var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8)
	{
		int var9 = -(var5 >> 2);
		var5 = -(var5 & 3);

		for (int var10 = -var6; var10 < 0; ++var10)
		{
			int var11;
			for (var11 = var9; var11 < 0; ++var11)
			{
				var2 = var1[var3++];
				if (var2 != 0)
				{
					var0[var4++] = var2;
				}
				else
				{
					++var4;
				}

				var2 = var1[var3++];
				if (var2 != 0)
				{
					var0[var4++] = var2;
				}
				else
				{
					++var4;
				}

				var2 = var1[var3++];
				if (var2 != 0)
				{
					var0[var4++] = var2;
				}
				else
				{
					++var4;
				}

				var2 = var1[var3++];
				if (var2 != 0)
				{
					var0[var4++] = var2;
				}
				else
				{
					++var4;
				}
			}

			for (var11 = var5; var11 < 0; ++var11)
			{
				var2 = var1[var3++];
				if (var2 != 0)
				{
					var0[var4++] = var2;
				}
				else
				{
					++var4;
				}
			}

			var4 += var7;
			var3 += var8;
		}

	}


	public BufferedImage toBufferedImage()
	{
//      int width = this.width;
//      int height = getHeight();
//      int[] pixels = getPixels();
		int[] transPixels = new int[pixels.length];
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		for (int i = 0; i < pixels.length; i++)
		{
			if (pixels[i] != 0)
			{
				transPixels[i] = pixels[i] | 0xff000000;
			}
		}

		img.setRGB(0, 0, width, height, transPixels, 0, width);
		return img;
	}

}
