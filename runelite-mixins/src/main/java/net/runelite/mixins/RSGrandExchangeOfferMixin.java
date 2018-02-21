package net.runelite.mixins;

import net.runelite.api.GrandExchangeOfferState;
import static net.runelite.api.GrandExchangeOfferState.BOUGHT;
import static net.runelite.api.GrandExchangeOfferState.BUYING;
import static net.runelite.api.GrandExchangeOfferState.CANCELLED;
import static net.runelite.api.GrandExchangeOfferState.EMPTY;
import static net.runelite.api.GrandExchangeOfferState.SELLING;
import static net.runelite.api.GrandExchangeOfferState.SOLD;
import net.runelite.api.mixins.Inject;
import net.runelite.api.mixins.Mixin;
import net.runelite.rs.api.RSGrandExchangeOffer;

@Mixin(RSGrandExchangeOffer.class)
public abstract class RSGrandExchangeOfferMixin implements RSGrandExchangeOffer
{


	@Inject
	@Override
	public GrandExchangeOfferState getState()
	{
		/*
		 Internally a GrandExchangeOffer's state is represented as 4 flags
		 packed into the lower half of a byte. They are:
		*/

		//Set for sell offers, unset for buy offers
		int IS_SELLING = 1 << 3; // 0b1000


		/*
		Set for offers that have finished, either because they've
		been filled, or because they were cancelled
		*/
		int COMPLETED = 1 << 2; // 0b0100

		/*
		Set for offers that are actually live
		NB: Insta-buy/sell offers will be simultaneously LIVE and LOCAL
		*/
		int LIVE = 1 << 1; // 0b0010

		//True for just-made, just-cancelled, completely cancelled, and completed offers
		int LOCAL = 1;

		byte code = getRSState();
		boolean isSelling = (code & IS_SELLING) == IS_SELLING;
		boolean isFinished = (code & COMPLETED) == COMPLETED;


		if (code == 0)
		{
			return EMPTY;
		}
		else if (isFinished && getQuantitySold() < getTotalQuantity())
		{
			return CANCELLED;
		}
		else if (isSelling)
		{
			if (isFinished)
			{
				return SOLD;
			}
			else // if isUnfinished
			{
				return SELLING;
			}
		}
		else // if isBuying
		{
			if (isFinished)
			{
				return BOUGHT;
			}
			else // if isUnfinished
			{
				return BUYING;
			}
		}
	}
}
