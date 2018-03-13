package net.runelite.cache.item;

public abstract class Renderable
{


	public int modelHeight;

	protected Renderable()
	{
		this.modelHeight = 1000;
	}


	protected Model getModel()
	{
		return null;
	}


	void draw(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9)
	{
		Model var10 = this.getModel();
		if (var10 != null)
		{
			this.modelHeight = var10.modelHeight;
			var10.draw(var1, var2, var3, var4, var5, var6, var7, var8, var9);
		}

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


	public static boolean method3047(int var0)
	{
		return (var0 >> 31 & 1) != 0;
	}
}
