package net.runelite.cache.definitions.providers;

import java.io.IOException;
import net.runelite.cache.definitions.ModelDefinition;

public interface ModelProvider
{
	ModelDefinition provide(int modelId) throws IOException;
}
