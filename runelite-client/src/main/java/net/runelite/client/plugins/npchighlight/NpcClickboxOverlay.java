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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.Arrays;
import java.util.List;

import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NPCComposition;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

public class NpcClickboxOverlay extends Overlay
{
	// Regex for splitting the hidden items in the config.
	private static final String DELIMITER_REGEX = "\\s*,\\s*";

	private final Client client;
	private final NpcHighlightConfig config;

	NpcClickboxOverlay(Client client, NpcHighlightConfig config)
	{
		this.config = config;
		this.client = client;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		String configNpcs = config.getNpcToHighlight().toLowerCase();
		List<String> highlightedNpcs = Arrays.asList(configNpcs.split(DELIMITER_REGEX));

		for (NPC npc : client.getNpcs())
		{
			NPCComposition composition = npc.getComposition();
			//composition.get
			if (composition.getConfigs() != null && composition.transform() != null)
			{
				composition = composition.transform();
			}
			if (npc == null || composition == null || composition.getName() == null)
				continue;
			
			// System.out.println(client.getSetting(Varbits.TEST));
			for (String highlight : highlightedNpcs)
			{
				String name = composition.getName().replace('\u00A0', ' ');
				highlight = highlight.replaceAll("\\*", ".*");
				if (name.toLowerCase().matches(highlight))
				{
					renderNpcOverlay(graphics, npc, name, config.getNpcColor());
				}
			}
		}

		// String debug = Stream.of(client.getMenuEntries()).map(m ->
		// m.getOption() + ":" + m.getTarget()).collect(Collectors.joining(",
		// "));
		// System.out.println(debug);

		// MenuEntry[] entryArr = client.getMenuEntries();
		// ArrayUtils.reverse(entryArr);
		//
		// List<MenuEntry> entries = new ArrayList<>();
		// for(MenuEntry entry : entryArr) {
		// if(entry.getOption().equals("Bank"))
		// {
		// entries.add(0, entry);
		// }
		// else
		// entries.add(entry);
		// }
		//
		// entryArr = entries.toArray(new MenuEntry[0]);
		// ArrayUtils.reverse(entryArr);
		//
		// client.setMenuEntries(entryArr);

		return null;
	}

	private void renderNpcOverlay(Graphics2D graphics, NPC actor, String name, Color color)
	{
		Polygon objectClickbox = ConvexHull.convexHull(actor.getClickbox());
		if (objectClickbox != null)
		{
			graphics.setColor(color);
			graphics.setStroke(new BasicStroke(2));
			graphics.draw(objectClickbox);
			graphics.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
			graphics.fill(objectClickbox);
		}

		net.runelite.api.Point textLocation = actor.getCanvasTextLocation(graphics, name,
				actor.getLogicalHeight() + 40);

		if (textLocation != null)
		{
			OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
		}
	}
}
