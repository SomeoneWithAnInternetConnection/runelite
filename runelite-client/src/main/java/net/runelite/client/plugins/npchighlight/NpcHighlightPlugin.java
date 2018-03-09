/*
 * Copyright (c) 2018, Tomas Slusny <slusnucky@gmail.com>
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

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;

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
	private static final String TAG = "Tag";

	@Inject
	private Client client;

	@Inject
	private NpcHighlightConfig config;

	NpcClickboxOverlay npcClickboxOverlay;

	NpcMinimapOverlay npcMinimapOverlay;

	@Provides
	NpcHighlightConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(NpcHighlightConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		npcClickboxOverlay = new NpcClickboxOverlay(client, config);

		npcMinimapOverlay = new NpcMinimapOverlay(client, config);
	}

	@Subscribe
	public void onMenuObjectClicked(MenuOptionClicked click)
	{
		if (click.getMenuOption().equals(TAG))
			npcClickboxOverlay.toggleTag(click.getId());
	}

	@Subscribe
	public void onGameTick(GameTick tick)
	{
		for (NPC npc : client.getNpcs())
		{
			if (npc.getCombatLevel() > 0)
			{
				NPCComposition comp = npc.getComposition();
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
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Override
	public Collection<Overlay> getOverlays()
	{
		return Arrays.asList(npcClickboxOverlay, npcMinimapOverlay);
	}
}
