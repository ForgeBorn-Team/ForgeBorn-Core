package com.sqvizers.forgeborncore.common.data;

import static com.sqvizers.forgeborncore.api.registries.FBRegistration.REGISTRATE;

public class FBMachines {

    static {
        REGISTRATE.creativeModeTab(() -> FBCreativeModeTabs.FORGEBORN_CORE);
    }

    public static void init() {}
}
