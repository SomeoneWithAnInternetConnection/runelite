package net.runelite.mixins;

import net.runelite.api.events.NpcActionChanged;
import net.runelite.api.mixins.FieldHook;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import static net.runelite.client.callback.Hooks.eventBus;
import net.runelite.rs.api.RSNPCComposition;

@Mixin(RSNPCComposition.class)
public abstract class RSNpcCompositionMixin implements RSNPCComposition
{
	@FieldHook("actions")
	@Inject
	public void actionsHook(int idx)
	{
		NpcActionChanged npcActionChanged = new NpcActionChanged();
		npcActionChanged.setNpcComposition(this);
		npcActionChanged.setIdx(idx);
		eventBus.post(npcActionChanged);
	}
}
