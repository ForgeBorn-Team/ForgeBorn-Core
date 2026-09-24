package com.sqvizers.forgeborncore.client.cutscene.background;

@FunctionalInterface
public interface BackgroundEffect {

    int colorAt(Point point, float time);

    interface Data extends BackgroundEffect {

        String type();
    }

    final class Point {

        public float u, v, nx, ny, dist, angle;
    }
}
