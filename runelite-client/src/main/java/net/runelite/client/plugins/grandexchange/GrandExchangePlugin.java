package net.runelite.client.plugins.grandexchange;

import com.google.common.eventbus.Subscribe;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import net.runelite.api.events.GrandExchangeOfferChanged;
import com.google.inject.Binder;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientUI;
import net.runelite.client.ui.NavigationButton;

import java.awt.image.BufferedImage;

/**
 * @author Robbie, created on 29/10/2017 09:59 AM
 */
@PluginDescriptor(
	name = "Grand Exchange offer plugin"
)
public class GrandExchangePlugin extends Plugin
{
	private BufferedImage icon;
	private NavigationButton button;

	private GrandExchangePanel panel;

	@Inject
	ClientUI ui;

	@Override
	public void configure(Binder binder)
	{
		binder.bind(GrandExchangePanel.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		panel = injector.getInstance(GrandExchangePanel.class);
		icon = ImageIO.read(getClass().getResourceAsStream("ge_icon.png"));
		button = new NavigationButton("GE Offers", icon, () -> panel);
		ui.getPluginToolbar().addNavigation(button);
	}

	@Subscribe
	public void onGrandExchangeOfferChanged(GrandExchangeOfferChanged grandExchangeOfferChanged)
	{
		panel.updateOffers();
	}

}
