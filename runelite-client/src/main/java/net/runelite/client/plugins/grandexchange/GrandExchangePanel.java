package net.runelite.client.plugins.grandexchange;

import javax.inject.Inject;
import javax.swing.BoxLayout;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;

@Slf4j
class GrandExchangePanel extends PluginPanel
{
	private static final int MAX_OFFERS = 8;

	private GrandExchangeOfferSlot[] offerSlotPanels = new GrandExchangeOfferSlot[MAX_OFFERS];

	@Inject
	GrandExchangePanel(ItemManager itemManager)
	{
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		for (int i = 0; i < offerSlotPanels.length; ++i)
		{
			offerSlotPanels[i] = new GrandExchangeOfferSlot(itemManager);
			add(offerSlotPanels[i]);
		}

		setVisible(true);
	}

	void updateOffer(GrandExchangeOffer newOffer, int slot)
	{
		offerSlotPanels[slot].updateOffer(newOffer);
	}

}
