package net.runelite.cache.item;

import com.google.common.graph.GraphBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.runelite.cache.ItemManager;
import net.runelite.cache.SpriteManager;
import net.runelite.cache.StoreLocation;
import net.runelite.cache.TextureManager;
import net.runelite.cache.definitions.ItemDefinition;
import net.runelite.cache.fs.Store;
import org.junit.Test;

public class ItemSpriteFactoryTest
{
	@Test
	public void test() throws IOException
	{
		File base = StoreLocation.LOCATION;
//			outDir = folder.newFolder();

		int count = 0;

		try (Store store = new Store(base)) {
			store.load();

			ItemManager itemManager = new ItemManager(store);
			itemManager.load();

//			ItemDefinition def = itemManager.getItem(6570);
			ItemDefinition def = itemManager.getItem(6570);
			Graphics3D.Rasterizer3D_zoom = 512; // you don't actually need to set this
			Graphics3D.setBrightness(0.6D); // .6 - .9

			TextureManager textureManager = new TextureManager(store);
			textureManager.load();

			Graphics3D.textureLoader = new TextureProvider(textureManager);
			ItemSpriteFactory.spriteManager = new SpriteManager(store);
			ItemSpriteFactory.spriteManager.load();

			SpritePixels sprite = ItemSpriteFactory.createSprite(store, def, 1, 1, 3153952, 0, false);

			File out = new File("C:\\rs\\temp\\4153.png");
			BufferedImage img = sprite.toBufferedImage();
			ImageIO.write(img, "PNG", out);

//			Storage storage = store.getStorage();
//			Index index = store.getIndex(IndexType.CONFIGS);
//			Archive archive = index.getArchive(ConfigType.ITEM.getId());
//
//			byte[] archiveData = storage.loadArchive(archive);
//			ArchiveFiles files = archive.getFiles(archiveData);
//
//			for (FSFile f : files.getFiles())
//			{
//				ItemDefinition def = loader.load(f.getFileId(), f.getContents());
		}
	}
}