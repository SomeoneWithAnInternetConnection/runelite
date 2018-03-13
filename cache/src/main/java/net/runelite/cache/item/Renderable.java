package net.runelite.cache.item;

public abstract class Renderable
{


	public int modelHeight;

	protected Renderable()
	{
		this.modelHeight = 1000;
	}


	//   public static IndexedSprite getSprite(IndexDataBase var0, String var1, String var2) {
//      int var3 = var0.getFile(var1);
//      int var4 = var0.getChild(var3, var2);
//      byte[] var7 = var0.getConfigData(var3, var4);
//      boolean var6;
//      if(var7 == null) {
//         var6 = false;
//      } else {
//         Widget.decodeSprite(var7);
//         var6 = true;
//      }
//
//      IndexedSprite var5;
//      if(!var6) {
//         var5 = null;
//      } else {
//         var5 = Ignore.method5387();
//      }
//
//      return var5;
//   }


}
