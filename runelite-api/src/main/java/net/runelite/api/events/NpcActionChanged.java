package net.runelite.api.events;

import lombok.Data;
import net.runelite.api.NPCComposition;

@Data
public class NpcActionChanged
{
	private NPCComposition npcComposition;
	private int idx;
}
