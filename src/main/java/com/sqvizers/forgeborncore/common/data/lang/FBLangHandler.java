package com.sqvizers.forgeborncore.common.data.lang;

import com.tterrag.registrate.providers.RegistrateLangProvider;

public class FBLangHandler {

    private static final String[] FB_RECIPE_TYPE_IDS = {
            "spiritfire_furnace"
    };

    private static String toTitle(String snakeCase) {
        StringBuilder out = new StringBuilder();
        for (String part : snakeCase.split("_")) {
            if (part.isEmpty()) continue;
            if (out.length() > 0) out.append(' ');
            out.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }
        return out.toString();
    }

    public static void init(RegistrateLangProvider provider) {
        provider.add("forgeborncore.machine.spirit_furnace.tooltip.0", "Performs Spiritfire recipes");
    }
}
