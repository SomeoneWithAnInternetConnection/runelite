package net.runelite.client.plugins.grandexchange;

import com.google.common.base.Preconditions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.LineBorder;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.GrandExchangeOffer;
import net.runelite.client.game.ItemManager;
import net.runelite.client.ui.PluginPanel;


@Slf4j
class GrandExchangePanel extends PluginPanel
{
	private static final int OFFERS = 8;

	@Nullable
	private final Client client;

	private final ItemManager itemManager;
	private GrandExchangeOfferSlot[] offers = new GrandExchangeOfferSlot[OFFERS];

	@Inject
	GrandExchangePanel(@Nullable Client client, ItemManager itemManager)
	{
		this.client = client;
		this.itemManager = itemManager;
		setVisible(true);

		for (int i = 0; i < offers.length; ++i)
		{
			offers[i] = new GrandExchangeOfferSlot();
		}

		add(buildPanel());
	}

	void updateOffers()
	{
		if (client == null || client.getGameState() != GameState.LOGGED_IN)
		{
			//TODO: only repaint/update if offer changed.
			return;
		}

		GrandExchangeOffer[] geOffers = client.getGrandExchangeOffers();
		if (geOffers == null)
		{
			return;
		}

		// update offers
		for (int i = 0; i < OFFERS; ++i)
		{
			offers[i].offer = geOffers[i];
		}

		for (GrandExchangeOfferSlot slot : offers)
		{
			GrandExchangeOffer offer = slot.offer;

			int itemId = offer.getItemId();
			int price = offer.getPrice();
			int sold = offer.getQuantitySold();
			int total = offer.getTotalQuantity();

			final String itemName = client.getItemDefinition(itemId).getName();
			final Image itemImage = itemManager.getImage(itemId);

			EventQueue.invokeLater(() ->
			{
				try
				{
					slot.setItemNameLabel(itemId == 0 ? "Empty" : itemName);
					slot.setOfferImage(new ImageIcon(itemImage));
					String stackSize = ItemManager.quantityToStackSize(price);
					slot.setOfferPriceLabel(stackSize.endsWith("M") || stackSize.endsWith("K")
						? stackSize
						: stackSize + " gp");
					slot.getProgressBar().setString(sold + " / " + total);
					slot.setProgressBar(total == 0 ? 0 : (sold / total) * 100);
					slot.setStatusIndicator(offer.getState());
				}
				catch (Exception ex)
				{
					log.warn("error updating slot", ex);
				}
			});
		}

	}

	private JComponent buildPanel()
	{
		JFrame frame = new JFrame();
		frame.setBounds(100, 100, 225, 555);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);

		JPanel[] panelSlots = new JPanel[OFFERS];
		for (int i = 0; i < OFFERS; i++)
		{
			panelSlots[i] = buildSlot(i);
		}
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panelSlots[0], GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE)
						.addComponent(panelSlots[1], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSlots[2], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSlots[3], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSlots[4], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSlots[5], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSlots[6], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE)
						.addComponent(panelSlots[7], GroupLayout.PREFERRED_SIZE, 189, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panelSlots[0], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[1], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[2], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[3], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[4], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[5], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[6], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelSlots[7], GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(15, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);

		return panel;
	}

	private JPanel buildSlot(int slotNumber)
	{
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBackground(new Color(90, 82, 69));

		GrandExchangeOfferSlot offerSlot = getSlot(slotNumber);

		GroupLayout groupLayout = new GroupLayout(panel);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGap(0, 187, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(offerSlot.getImageLabel())
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(offerSlot.getItemNameLabel(), GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
							.addGap(58))
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addComponent(offerSlot.getOfferPriceLabel())
							.addPreferredGap(ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
							.addComponent(offerSlot.getStatusIndicatorLabel()))
						.addComponent(offerSlot.getProgressBar(), GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 54, Short.MAX_VALUE)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(offerSlot.getItemNameLabel(), GroupLayout.DEFAULT_SIZE, 14, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(offerSlot.getOfferPriceLabel())
						.addComponent(offerSlot.getStatusIndicatorLabel()))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(offerSlot.getProgressBar(), GroupLayout.PREFERRED_SIZE, 10, Short.MAX_VALUE))
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap(43, Short.MAX_VALUE)
					.addComponent(offerSlot.getImageLabel())
					.addContainerGap())
		);
		panel.setLayout(groupLayout);

		return panel;
	}

	private GrandExchangeOfferSlot getSlot(int slotNumber)
	{
		Preconditions.checkArgument(slotNumber >= 0 && slotNumber < OFFERS);
		return offers[slotNumber];
	}

}
