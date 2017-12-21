/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.runelite.client.plugins.grandexchange;

import net.runelite.api.Client;
import static net.runelite.api.GameState.LOGGED_IN;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.api.ItemComposition;
import net.runelite.client.RuneLite;
import net.runelite.client.game.ItemManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Matchers.anyInt;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;

@RunWith(MockitoJUnitRunner.class)
public class GrandExchangePanelTest
{

	@Mock(answer = RETURNS_DEEP_STUBS)
	private RuneLite runeLite;

	@Mock
	private Client client;

	@Mock
	private ItemManager itemManager;

	@Test
	public void testUpdateOffers()
	{
		GrandExchangeOffer[] offers = new GrandExchangeOffer[]
		{
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
			mock(GrandExchangeOffer.class),
		};

		when(client.getGrandExchangeOffers()).thenReturn(offers);
		when(client.getGameState()).thenReturn(LOGGED_IN);
		when(client.getItemDefinition(anyInt())).thenReturn(mock(ItemComposition.class));
		when(itemManager.getImage(anyInt())).thenReturn(mock(BufferedImage.class));

		GrandExchangePanel grandExchangePanel = new GrandExchangePanel(client, itemManager);
		grandExchangePanel.updateOffers();
	}

}
