package one.theaq.cfsf;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CFSF.MODID)
public class CFSF {
  public static final String MODID = "cfsf";
  private static final Logger LOGGER = LogUtils.getLogger();
  
  public CFSF() {
    LOGGER.info("Mod is loaded");
  }
}
