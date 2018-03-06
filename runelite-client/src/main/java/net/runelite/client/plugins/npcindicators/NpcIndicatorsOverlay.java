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
package net.runelite.client.plugins.npcindicators;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;

import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.NPC;
import net.runelite.api.Varbits;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class NpcIndicatorsOverlay extends Overlay
{
	// Regex for splitting the hidden items in the config.
	private static final String DELIMITER_REGEX = "\\s*,\\s*";

	private final Client client;
	private final NpcIndicatorsConfig config;

	NpcIndicatorsOverlay(Client client, NpcIndicatorsConfig config)
	{
		this.config = config;
		this.client = client;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.HIGH);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
	}

	@Override
	public Dimension render(Graphics2D graphics, Point parent)
	{
		String configNpcs = config.getNpcToHighlight().toLowerCase();
		List<String> highlightedNpcs = Arrays.asList(configNpcs.split(DELIMITER_REGEX));
		
		for (NPC npc : client.getNpcs())
		{
			if (npc == null || npc.getName() == null)
				continue;
			//System.out.println(client.getSetting(Varbits.TEST));
			for (String highlight : highlightedNpcs)
			{
				highlight = highlight.replaceAll("\\*", ".*");
				if(npc.getName().toLowerCase().matches(highlight))
				{
					renderNpcOverlay(graphics, npc, config.getNpcColor());
				}
			}
		}
		
//		String debug = Stream.of(client.getMenuEntries()).map(m -> m.getOption() + ":" + m.getTarget()).collect(Collectors.joining(", "));
//		System.out.println(debug);
		
//		MenuEntry[] entryArr = client.getMenuEntries();
//		ArrayUtils.reverse(entryArr);
//		
//		List<MenuEntry> entries = new ArrayList<>();
//		for(MenuEntry entry : entryArr) {
//			if(entry.getOption().equals("Bank"))
//			{
//				entries.add(0, entry);
//			}
//			else
//				entries.add(entry);
//		}
//		
//		entryArr = entries.toArray(new MenuEntry[0]);
//		ArrayUtils.reverse(entryArr);
//		
//		client.setMenuEntries(entryArr);

		return null;
	}

	private void renderNpcOverlay(Graphics2D graphics, NPC actor, Color color)
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
		final String name = actor.getName().replace('\u00A0', ' ') + actor.getId();

		net.runelite.api.Point minimapLocation = actor.getMinimapLocation();
		if (minimapLocation != null)
		{
			OverlayUtil.renderMinimapLocation(graphics, minimapLocation, color.darker());
			OverlayUtil.renderTextLocation(graphics, minimapLocation, name, color);
		}

		net.runelite.api.Point textLocation = actor.getCanvasTextLocation(graphics, name,
				actor.getLogicalHeight() + 40);

		if (textLocation != null)
		{
			OverlayUtil.renderTextLocation(graphics, textLocation, name, color);
		}
	}
}
