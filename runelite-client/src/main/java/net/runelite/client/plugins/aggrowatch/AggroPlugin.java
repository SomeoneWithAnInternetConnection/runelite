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

import com.google.common.eventbus.Subscribe;
import com.google.inject.Binder;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.Overlay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@PluginDescriptor(
	name = "Aggro tracking pluggin"
)

@Slf4j
public class AggroPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AggroOverlay overlay;

	@Override
	public void configure(Binder binder)
	{
		binder.bind(AggroOverlay.class);
	}

	@Override
	public Overlay getOverlay()
	{
		return overlay;
	}

	/*
	@Subscribe
	public void onNPCUpdate(GameTick tick)
	{
		interactingActors = client.getNpcs().stream()
				.filter(n -> n.getInteracting() != null)
				.collect(Collectors.toList());
		interactingActors.stream()
				.map(n -> new Object(){Actor actor = n; Actor target = n.getInteracting();})
				.forEach(n -> log.info("{}({},{}) -> {}({},{})", n.actor.getName(),
						n.actor.getLocalLocation().getX(),
						n.actor.getLocalLocation().getY(),
						n.target.getName(),
						n.target.getLocalLocation().getX(),
						n.target.getLocalLocation().getY()));
	}
	*/

}
