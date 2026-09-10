package com.sqvizers.forgeborncore.common.data;

import com.sqvizers.forgeborncore.api.registries.FBRegistration;
import com.sqvizers.forgeborncore.common.data.lang.FBLangHandler;

import com.tterrag.registrate.providers.ProviderType;

public class FBDataGen {

    public static void init() {
        FBRegistration.REGISTRATE.addDataGenerator(ProviderType.LANG, FBLangHandler::init);
    }
}
