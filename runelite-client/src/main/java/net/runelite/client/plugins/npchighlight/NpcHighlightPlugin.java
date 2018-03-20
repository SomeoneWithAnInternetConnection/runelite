/*
 * Copyright (c) 2018, James Swindle <wilingua@gmail.com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.npchighlight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import java.util.Set;
import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;

import lombok.AccessLevel;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;

@PluginDescriptor(name = "NPC Highlight")
public class NpcHighlightPlugin extends Plugin
{
	// Option added to NPC menu
	private static final String TAG = "Tag";

	// Regex for splitting the hidden items in the config.
	private static final String DELIMITER_REGEX = "\\s*,\\s*";

	@Inject
	private Client client;

	@Inject
	private NpcHighlightConfig config;

	NpcClickboxOverlay npcClickboxOverlay;

	NpcMinimapOverlay npcMinimapOverlay;

	@Getter(AccessLevel.PACKAGE)
	private final Set<Integer> npcTags = new HashSet<>();

	@Getter(AccessLevel.PACKAGE)
	private final List<NPC> taggedNpcs = new ArrayList<>();

	private void toggleTag(int npcId) {
		boolean removed = npcTags.remove(npcId);
		if (!removed)
			npcTags.add(npcId);
	}

	@Provides
	NpcHighlightConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NpcHighlightConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		npcClickboxOverlay = new NpcClickboxOverlay(client, config, this);
		npcMinimapOverlay = new NpcMinimapOverlay(config, this);
	}

	@Subscribe
	public void onMenuObjectClicked(MenuOptionClicked click)
	{
		if (click.getMenuOption().equals(TAG))
			toggleTag(click.getId());
	}

	@Subscribe
	// add a 'Tag' option to all NPCs with a combat level
	public void onGameTick(GameTick tick)
	{
		for (NPC npc : client.getNpcs())
		{
			if (npc.getCombatLevel() > 0)
			{
				NPCComposition comp = getComposition(npc);
				if (comp == null)
					continue;

				for (int i = comp.getActions().length - 1; i >= 0; i--)
				{
					if ((comp.getActions()[i] == null || comp.getActions()[i].isEmpty()) && config.isTagEnabled())
					{
						comp.getActions()[i] = TAG;
						break;
					}
					if (comp.getActions()[i] != null && comp.getActions()[i].equals(TAG))
					{
						if (!config.isTagEnabled())
							comp.getActions()[i] = null;
						break;
					}
				}
			}
		}
		if (npcTags.isEmpty()) return;
		taggedNpcs.clear();
		for (NPC npc : client.getNpcs())
		{
			if (npcTags.contains(npc.getIndex()))
			{
				NPCComposition composition = getComposition(npc);
				if (composition == null || composition.getName() == null)
					continue;

				taggedNpcs.add(npc);
			}
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		npcTags.clear();
		taggedNpcs.clear();
	}

	@Override
	public Collection<Overlay> getOverlays()
	{
		return Arrays.asList(npcClickboxOverlay, npcMinimapOverlay);
	}

	protected Map<NPC, String> getNpcsToHighlight()
	{
		Map<NPC, String> npcMap = new HashMap<>();

		String configNpcs = config.getNpcToHighlight().toLowerCase();
		if (configNpcs.isEmpty())
			return npcMap;

		List<String> highlightedNpcs = Arrays.asList(configNpcs.split(DELIMITER_REGEX));

		for (NPC npc : client.getNpcs())
		{
			NPCComposition composition = getComposition(npc);

			if (npc == null || composition == null || composition.getName() == null)
				continue;

			for (String highlight : highlightedNpcs)
			{
				String name = composition.getName().replace('\u00A0', ' ');
				highlight = highlight.replaceAll("\\*", ".*");
				if (name.toLowerCase().matches(highlight))
				{
					npcMap.put(npc, name);
				}
			}
		}

		return npcMap;
	}

	/**
	 * Get npc composition, account for imposters
	 * @param npc
	 * @return
	 */
	protected NPCComposition getComposition(NPC npc)
	{
		if (npc == null)
			return null;

		NPCComposition composition = npc.getComposition();
		if (composition != null && composition.getConfigs() != null && composition.transform() != null)
		{
			composition = composition.transform();
		}

		return composition;
	}
}
