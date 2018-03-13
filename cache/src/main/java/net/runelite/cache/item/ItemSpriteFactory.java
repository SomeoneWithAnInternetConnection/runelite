package net.runelite.cache.item;

import java.io.IOException;
import net.runelite.cache.IndexType;
import net.runelite.cache.SpriteManager;
import net.runelite.cache.definitions.ItemDefinition;
import net.runelite.cache.definitions.ModelDefinition;
import net.runelite.cache.definitions.loaders.ModelLoader;
import net.runelite.cache.fs.Archive;
import net.runelite.cache.fs.Index;
import net.runelite.cache.fs.Store;
import net.runelite.cache.models.FaceNormal;
import net.runelite.cache.models.VertexNormal;

public class ItemSpriteFactory
{
	public static SpriteManager spriteManager;

	public static final SpritePixels createSprite(Store store, ItemDefinition itemDefinition, int quantity, int border, int shadowColor, int stackable, boolean noted) throws IOException
	{
		if (quantity == -1)
		{
			stackable = 0;
		}
		else if (stackable == 2 && quantity != 1)
		{
			stackable = 1;
		}

//		long var6 = ((long)shadowColor << 42) + (long)itemId + ((long)quantity << 16) + ((long)border << 38) + ((long)stackable << 40);
//		SpritePixels var8;
//		if(!noted) {
//			var8 = (SpritePixels)ItemComposition.itemSpriteCache.get(var6);
//			if(var8 != null) {
//				return var8;
//			}
//		}

//		ItemDefinition itemDefinition = null;
//		ItemComposition itemDefinition = class81.getItemDefinition(itemId);
		if (quantity > 1 && itemDefinition.countObj != null)
		{
			int var10 = -1;

			for (int var11 = 0; var11 < 10; ++var11)
			{
				if (quantity >= itemDefinition.countCo[var11] && itemDefinition.countCo[var11] != 0)
				{
					var10 = itemDefinition.countObj[var11];
				}
			}
//
//			if(var10 != -1) {
//				itemDefinition = class81.getItemDefinition(var10);
//			}
		}

//		ModelDefinition itemModel = null;
		Index models = store.getIndex(IndexType.MODELS);
//		models.getArchive()
		Model itemModel = getModel(store, itemDefinition, 1); //itemDefinition.getModel(1);
		if (itemModel == null)
		{
			return null;
		}
		else
		{
			SpritePixels spritePixels = null;
//			if(itemDefinition.notedTemplate != -1) {
//				spritePixels = createSprite(itemDefinition.notedID, 10, 1, 0, 0, true);
//				if(spritePixels == null) {
//					return null;
//				}
//			} else if(itemDefinition.boughtTemplateId != -1) {
//				spritePixels = createSprite(itemDefinition.boughtId, quantity, border, shadowColor, 0, false);
//				if(spritePixels == null) {
//					return null;
//				}
//			} else if(itemDefinition.placeholderTemplateId != -1) {
//				spritePixels = createSprite(itemDefinition.placeholderId, quantity, 0, 0, 0, false);
//				if(spritePixels == null) {
//					return null;
//				}
//			}

			SpritePixels var8 = new SpritePixels(36, 32);
			Rasterizer2D.setRasterBuffer(var8.pixels, 36, 32);
			Rasterizer2D.reset();
			Graphics3D.Rasterizer3D_method1();
			Graphics3D.method2780(16, 16);
			Graphics3D.rasterGouraudLowRes = false;
			if (itemDefinition.placeholderTemplateId != -1)
			{
				spritePixels.drawAt(0, 0);
			}

			int var16 = itemDefinition.zoom2d;
			if (noted)
			{
				var16 = (int) ((double) var16 * 1.5D);
			}
			else if (border == 2)
			{
				var16 = (int) ((double) var16 * 1.04D);
			}

			int var17 = var16 * Graphics3D.SINE[itemDefinition.xan2d] >> 16;
			int var18 = var16 * Graphics3D.COSINE[itemDefinition.xan2d] >> 16;
			itemModel.calculateBoundsCylinder();
			itemModel.method2702(0, itemDefinition.yan2d, itemDefinition.zan2d, itemDefinition.xan2d, itemDefinition.xOffset2d, itemModel.modelHeight / 2 + var17 + itemDefinition.yOffset2d, var18 + itemDefinition.yOffset2d);
			if (itemDefinition.boughtTemplateId != -1)
			{
				spritePixels.drawAt(0, 0);
			}

			if (border >= 1)
			{
				var8.method5838(1);
			}

			if (border >= 2)
			{
				var8.method5838(16777215);
			}

			if (shadowColor != 0)
			{
				var8.method5886(shadowColor);
			}

			Rasterizer2D.setRasterBuffer(var8.pixels, 36, 32);
			if (itemDefinition.notedTemplate != -1)
			{
				spritePixels.drawAt(0, 0);
			}

//			if(stackable == 1 || stackable == 2 && itemDefinition.stackable == 1) {
//				Resampler.field1590.method5500(Client.method1645(quantity), 0, 9, 16776960, 1);
//			}

//			if(!noted) {
//				ItemComposition.itemSpriteCache.put(var8, var6);
//			}

			Rasterizer2D.setRasterBuffer(Rasterizer2D.graphicsPixels,
					Rasterizer2D.graphicsPixelsWidth,
					Rasterizer2D.graphicsPixelsHeight);

			int[] drawRegion = new int[4];
			Rasterizer2D.copyDrawRegion(drawRegion);
			Rasterizer2D.setDrawRegion(drawRegion);

			Graphics3D.Rasterizer3D_method1();
			Graphics3D.rasterGouraudLowRes = true;
			return var8;
		}
	}

	private static Model getModel(Store store, ItemDefinition item, int var1) throws IOException
	{
//		if(item.countObj != null && var1 > 1) {
//			int var2 = -1;
//
//			for(int var3 = 0; var3 < 10; ++var3) {
//				if(var1 >= item.countCo[var3] && item.countCo[var3] != 0) {
//					var2 = item.countObj[var3];
//				}
//			}
//
//			if(var2 != -1) {
//				return class81.getItemDefinition(var2).getModel(1);
//			}
//		}
//
//		Model var5 = (Model)itemModelCache.get((long)this.id);
//		if(var5 != null) {
//			return var5;
//		} else {
		Index models = store.getIndex(IndexType.MODELS);
		Archive archive = models.getArchive(item.inventoryModel);

		byte[] data = archive.decompress(store.getStorage().loadArchive(archive));

		Model var5;
		ModelDefinition var6 = new ModelLoader().load(item.inventoryModel, data);
//			ModelData var6 = ModelData.method2594(ItemDefinition_modelIndexCache, item.inventoryModel, 0);
		if (var6 == null)
		{
			return null;
		}
		else
		{
			if (item.resizeX != 128 || item.resizeY != 128 || item.resizeZ != 128)
			{
				var6.method2610(item.resizeX, item.resizeY, item.resizeZ);
			}

			int var4;
			if (item.colorFind != null)
			{
				for (var4 = 0; var4 < item.colorFind.length; ++var4)
				{
					var6.recolor(item.colorFind[var4], item.colorReplace[var4]);
				}
			}

//				if(item.textureFind != null) {
//					for(var4 = 0; var4 < item.textureFind.length; ++var4) {
//						var6.method2609(item.textureFind[var4], item.textureReplace[var4]);
//					}
//				}

			var5 = light(var6, item.ambient + 64, item.contrast + 768, -50, -10, -50);
			var5.field1849 = true;
			//itemModelCache.put(var5, (long)this.id);
			return var5;
		}
		//}
	}

	public static Model light(ModelDefinition def, int var1, int var2, int var3, int var4, int var5)
	{
		def.computeNormals();
		int var6 = (int) Math.sqrt((double) (var5 * var5 + var3 * var3 + var4 * var4));
		int var7 = var6 * var2 >> 8;
		Model var8 = new Model();
		var8.field1856 = new int[def.faceCount];
		var8.field1854 = new int[def.faceCount];
		var8.field1823 = new int[def.faceCount];
		if (def.textureTriangleCount > 0 && def.textureCoordinates != null)
		{
			int[] var9 = new int[def.textureTriangleCount];

			int var10;
			for (var10 = 0; var10 < def.faceCount; ++var10)
			{
				if (def.textureCoordinates[var10] != -1)
				{
					++var9[def.textureCoordinates[var10] & 255];
				}
			}

			var8.field1852 = 0;

			for (var10 = 0; var10 < def.textureTriangleCount; ++var10)
			{
				if (var9[var10] > 0 && def.textureRenderTypes[var10] == 0)
				{
					++var8.field1852;
				}
			}

			var8.field1844 = new int[var8.field1852];
			var8.field1865 = new int[var8.field1852];
			var8.field1846 = new int[var8.field1852];
			var10 = 0;

			int var11;
			for (var11 = 0; var11 < def.textureTriangleCount; ++var11)
			{
				if (var9[var11] > 0 && def.textureRenderTypes[var11] == 0)
				{
					var8.field1844[var10] = def.textureTriangleVertexIndices1[var11] & '\uffff';
					var8.field1865[var10] = def.textureTriangleVertexIndices2[var11] & '\uffff';
					var8.field1846[var10] = def.textureTriangleVertexIndices3[var11] & '\uffff';
					var9[var11] = var10++;
				}
				else
				{
					var9[var11] = -1;
				}
			}

			var8.field1840 = new byte[def.faceCount];

			for (var11 = 0; var11 < def.faceCount; ++var11)
			{
				if (def.textureCoordinates[var11] != -1)
				{
					var8.field1840[var11] = (byte) var9[def.textureCoordinates[var11] & 255];
				}
				else
				{
					var8.field1840[var11] = -1;
				}
			}
		}

		for (int var16 = 0; var16 < def.faceCount; ++var16)
		{
			byte var17;
			if (def.faceRenderTypes == null)
			{
				var17 = 0;
			}
			else
			{
				var17 = def.faceRenderTypes[var16];
			}

			byte var18;
			if (def.faceAlphas == null)
			{
				var18 = 0;
			}
			else
			{
				var18 = def.faceAlphas[var16];
			}

			short var12;
			if (def.faceTextures == null)
			{
				var12 = -1;
			}
			else
			{
				var12 = def.faceTextures[var16];
			}

			if (var18 == -2)
			{
				var17 = 3;
			}

			if (var18 == -1)
			{
				var17 = 2;
			}

			VertexNormal var13;
			int var14;
			FaceNormal var19;
			if (var12 == -1)
			{
				if (var17 != 0)
				{
					if (var17 == 1)
					{
						var19 = def.faceNormals[var16];
						var14 = (var4 * var19.y + var5 * var19.z + var3 * var19.x) / (var7 / 2 + var7) + var1;
						var8.field1856[var16] = method2608(def.faceColors[var16] & '\uffff', var14);
						var8.field1823[var16] = -1;
					}
					else if (var17 == 3)
					{
						var8.field1856[var16] = 128;
						var8.field1823[var16] = -1;
					}
					else
					{
						var8.field1823[var16] = -2;
					}
				}
				else
				{
					int var15 = def.faceColors[var16] & '\uffff';
//					if(def.field1741 != null && def.field1741[def.faceVertexIndices1[var16]] != null) {
//						var13 = def.field1741[def.faceVertexIndices1[var16]];
//					} else {
					var13 = def.vertexNormals[def.faceVertexIndices1[var16]];
					//}

					var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1;
					var8.field1856[var16] = method2608(var15, var14);
//					if(def.field1741 != null && def.field1741[def.faceVertexIndices2[var16]] != null) {
//						var13 = def.field1741[def.faceVertexIndices2[var16]];
//					} else {
					var13 = def.vertexNormals[def.faceVertexIndices2[var16]];
					//	}

					var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1;
					var8.field1854[var16] = method2608(var15, var14);
//					if(def.field1741 != null && def.field1741[def.faceVertexIndices3[var16]] != null) {
//						var13 = def.field1741[def.faceVertexIndices3[var16]];
//					} else {
					var13 = def.vertexNormals[def.faceVertexIndices3[var16]];
					//}

					var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1;
					var8.field1823[var16] = method2608(var15, var14);
				}
			}
			else if (var17 != 0)
			{
				if (var17 == 1)
				{
					var19 = def.faceNormals[var16];
					var14 = (var4 * var19.y + var5 * var19.z + var3 * var19.x) / (var7 / 2 + var7) + var1;
					var8.field1856[var16] = method2617(var14);
					var8.field1823[var16] = -1;
				}
				else
				{
					var8.field1823[var16] = -2;
				}
			}
			else
			{
//				if(def.field1741 != null && def.field1741[def.faceVertexIndices1[var16]] != null) {
//					var13 = def.field1741[def.faceVertexIndices1[var16]];
//				} else {
				var13 = def.vertexNormals[def.faceVertexIndices1[var16]];
				//}

				var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1;
				var8.field1856[var16] = method2617(var14);
//				if(def.field1741 != null && def.field1741[def.faceVertexIndices2[var16]] != null) {
//					var13 = def.field1741[def.faceVertexIndices2[var16]];
//				} else {
				var13 = def.vertexNormals[def.faceVertexIndices2[var16]];
				//}

				var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1;
				var8.field1854[var16] = method2617(var14);
//				if(def.field1741 != null && def.field1741[def.faceVertexIndices3[var16]] != null) {
//					var13 = def.field1741[def.faceVertexIndices3[var16]];
//				} else {
				var13 = def.vertexNormals[def.faceVertexIndices3[var16]];
				//	}

				var14 = (var4 * var13.y + var5 * var13.z + var3 * var13.x) / (var7 * var13.magnitude) + var1;
				var8.field1823[var16] = method2617(var14);
			}
		}

//		def.computeAnimationTables();
		var8.verticesCount = def.vertexCount;
		var8.verticesX = def.vertexPositionsX;
		var8.verticesY = def.vertexPositionsY;
		var8.verticesZ = def.vertexPositionsZ;
		var8.indicesCount = def.faceCount;
		var8.indices1 = def.faceVertexIndices1;
		var8.indices2 = def.faceVertexIndices2;
		var8.indices3 = def.faceVertexIndices3;
		var8.field1838 = def.faceRenderPriorities;
		var8.field1882 = def.faceAlphas;
		var8.field1842 = def.priority;
//		var8.field1847 = def.field1725;
//		var8.field1848 = def.field1726;
		var8.field1841 = def.faceTextures;
		return var8;
	}

	static final int method2608(int var0, int var1)
	{
		var1 = (var0 & 127) * var1 >> 7;
		if (var1 < 2)
		{
			var1 = 2;
		}
		else if (var1 > 126)
		{
			var1 = 126;
		}

		return (var0 & 65408) + var1;
	}

	static final int method2617(int var0)
	{
		if (var0 < 2)
		{
			var0 = 2;
		}
		else if (var0 > 126)
		{
			var0 = 126;
		}

		return var0;
	}
}
