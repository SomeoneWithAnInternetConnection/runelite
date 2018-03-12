/*
 * Copyright (c) 2018, SomeoneWithAnInternetConnection
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

package net.runelite.client.plugins.aggrowatch;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;

@Slf4j
public class AggroOverlay extends Overlay
{
	private final Client client;

	@Inject
	public AggroOverlay(Client client)
	{
		setPosition(OverlayPosition.DYNAMIC);
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics, java.awt.Point point)
	{
		final Graphics2D g = (Graphics2D)graphics.create();
		if (client.getGameState() != GameState.LOGGED_IN)
		{
			return null;
		}

		Set<Actor> actors = new HashSet<>();
		actors.addAll(client.getNpcs());
		actors.addAll(client.getPlayers());

		int plane = client.getPlane();
		if (actors.size() > 0)
		{
			for (Actor actor : actors)
			{
				Actor target = actor.getInteracting();
				if (target == null)
				{
					continue;
				}
				LocalPoint pos = actor.getLocalLocation();
				Point actorCentre = Perspective.worldToCanvas(client, pos.getX(), pos.getY(), plane, actor.getModelHeight() / 2);
				pos = target.getLocalLocation();

				Point targetCentre = Perspective.worldToCanvas(client, pos.getX(), pos.getY(), plane, target.getModelHeight() / 2);
				if (actor instanceof Player)
				{
					g.setColor(Color.GREEN);
					g.setStroke(new BasicStroke(3));
				}
				else
				{
					g.setColor(Color.RED);
					g.setStroke(new BasicStroke(1));
				}
				drawArrow(g, actorCentre, targetCentre);
			}
		}

		g.dispose();


		return null;
	}

	private void drawArrow(Graphics2D g, Point start, Point end)
	{
		g.drawLine(start.getX(), start.getY(), end.getX(), end.getY());

	}

}
