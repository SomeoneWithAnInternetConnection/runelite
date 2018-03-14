package net.runelite.cache.item;

import net.runelite.cache.TextureManager;
import net.runelite.cache.definitions.TextureDefinition;

class TextureProvider
{


	static int menuX;


	TextureDefinition[] textures;
//	Texture[] textures;


//   Deque deque;


	int maxSize;


	int size;


	double brightness;


	int width;

	//new TextureProvider(class62.indexTextures, WorldMapRegion.indexSprites, 20, 0.8D, Client.lowMemory?64:128);
	public TextureProvider(TextureManager textureManager)
	{
		this.size = 0;
		this.brightness = 1.0D;
		this.width = 128;
//      this.sprites = var2;
		this.maxSize = 20;
		this.size = this.maxSize;
		this.brightness = 0.8D;
		this.width = 128;

		int max = -1;
		for (TextureDefinition textureDefinition : textureManager.getTextures())
		{
			if (textureDefinition.getId() > max)
			{
				max = textureDefinition.getId();
			}
		}

		textures = new TextureDefinition[max + 1];
		for (TextureDefinition textureDefinition : textureManager.getTextures())
		{
			textures[textureDefinition.getId()] = textureDefinition;
		}
//      Map<Integer, TextureDefinition> textures = textureManager.getTextures().stream()
//		  .collect(Collectors.toMap(t -> t.getId(),  t-> t));
//
//      int[] var7 = var1.getChilds(0); // files of archive 0
//      int var8 = var7.length;
//      this.textures = new Texture[var1.fileCount(0)];
//
//      for(int var9 = 0; var9 < var8; ++var9) {
//         Buffer var10 = new Buffer(var1.getConfigData(0, var7[var9]));
//         this.textures[var7[var9]] = new Texture(var10);
//      }
	}


//   IndexDataBase sprites;


//   public TextureProvider(IndexDataBase var1, IndexDataBase var2, int var3, double var4, int var6) {
//      this.deque = new Deque();
//      this.size = 0;
//      this.brightness = 1.0D;
//      this.width = 128;
//      this.sprites = var2;
//      this.maxSize = var3;
//      this.size = this.maxSize;
//      this.brightness = var4;
//      this.width = var6;
//      int[] var7 = var1.getChilds(0);
//      int var8 = var7.length;
//      this.textures = new Texture[var1.fileCount(0)];
//
//      for(int var9 = 0; var9 < var8; ++var9) {
//         Buffer var10 = new Buffer(var1.getConfigData(0, var7[var9]));
//         this.textures[var7[var9]] = new Texture(var10);
//      }
//
//   }


//   public int method2566() {
//      int var1 = 0;
//      int var2 = 0;
//      Texture[] var3 = this.textures;
//
//      for(int var4 = 0; var4 < var3.length; ++var4) {
//         Texture var5 = var3[var4];
//         if(var5 != null && var5.fileIds != null) {
//            var1 += var5.fileIds.length;
//            int[] var6 = var5.fileIds;
//
//            for(int var7 = 0; var7 < var6.length; ++var7) {
//               int var8 = var6[var7];
//               if(this.sprites.method4567(var8)) {
//                  ++var2;
//               }
//            }
//         }
//      }
//
//      if(var1 == 0) {
//         return 0;
//      } else {
//         return var2 * 100 / var1;
//      }
//   }


	public void brightness(double var1)
	{
		this.brightness = var1;
		this.reset();
	}


	public int[] load(int var1)
	{
		TextureDefinition var2 = this.textures[var1];
		if (var2 != null)
		{
			if (var2.pixels != null)
			{
//            this.deque.addTail(var2);
//            var2.loaded = true;
				return var2.pixels;
			}

			boolean var3 = var2.method2680(this.brightness, this.width, ItemSpriteFactory.spriteManager);
			return var2.pixels;
//         if(var3) {
//            if(this.size == 0) {
//               Texture var4 = (Texture)this.deque.popTail();
//               var4.resetPixels();
//            } else {
//               --this.size;
//            }
//
//            this.deque.addTail(var2);
//            var2.loaded = true;
//            return var2.pixels;
//         }
		}

		return null;
	}


	public int getAverageTextureRGB(int var1)
	{
//		if(true)return 42;
		return this.textures[var1] != null ? this.textures[var1].field1777 : 0;
	}


	public boolean vmethod3057(int var1)
	{
		return this.textures[var1].field1778;
	}


	public boolean vmethod3066(int var1)
	{
		return this.width == 64;
	}


	public void reset()
	{
//		for (int var1 = 0; var1 < this.textures.length; ++var1)
//		{
//			if (this.textures[var1] != null)
//			{
//				this.textures[var1].resetPixels();
//			}
//		}

		//  this.deque = new Deque();
		this.size = this.maxSize;
	}


	public void checkTextures(int var1)
	{
//		for (int var2 = 0; var2 < this.textures.length; ++var2)
//		{
//			Texture var3 = this.textures[var2];
//			if (var3 != null && var3.field1783 != 0 && var3.loaded)
//			{
//				var3.method2674(var1);
//				var3.loaded = false;
//			}
//		}

	}
}
