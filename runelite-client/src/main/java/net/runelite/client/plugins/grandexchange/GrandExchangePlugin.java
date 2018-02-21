package net.runelite.client.plugins.grandexchange;

import com.google.common.eventbus.Subscribe;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import net.runelite.api.events.GrandExchangeOfferChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.NavigationButton;

/**
 * @author Robbie, created on 29/10/2017 09:59 AM
 */
@PluginDescriptor(
	name = "Grand Exchange offer plugin"
)
public class GrandExchangePlugin extends Plugin
{
	private NavigationButton button;

	private GrandExchangePanel panel;

	@Inject
	private	ClientUI ui;

	@Override
	protected void startUp() throws IOException
	{
		panel = injector.getInstance(GrandExchangePanel.class);
		BufferedImage icon = ImageIO.read(getClass().getResourceAsStream("ge_icon.png"));
		button = new NavigationButton("GE Offers", icon, () -> panel);
		ui.getPluginToolbar().addNavigation(button);
	}

	@Override
	protected void shutDown()
	{
		ui.getPluginToolbar().removeNavigation(button);
	}

	@Subscribe
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged offerEvent)
	{
		SwingUtilities.invokeLater(() ->
		{
			panel.updateOffer(offerEvent.getOffer(), offerEvent.getSlot());
		});
	}

}
