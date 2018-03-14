package net.runelite.cache.definitions.providers;

import net.runelite.cache.definitions.SpriteDefinition;

public interface SpriteProvider
{
	SpriteDefinition provide(int spriteId, int frameId);
}
