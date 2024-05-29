package redot.athere;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AtHere implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("athere");

	@Override
	public void onInitialize() {
		LOGGER.info(MSG.init);
	}
}