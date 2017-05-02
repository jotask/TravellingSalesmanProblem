package com.github.jotask.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * City
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class City {

    public static final float SIZE = 16;

    private final Vector2 point;
    private final Color color;

    private final int id;

    public City(final int id, final Vector2 point) {
        this.id = id;
        this.point = point;
        final float r = MathUtils.random();
        final float g = MathUtils.random();
        final float b = MathUtils.random();
        this.color = new Color(r, g, b,1);
    }

    public void render(final ShapeRenderer sr){
        sr.setColor(this.color);
        sr.circle(point.x, point.y, SIZE);
    }

    public int getId() { return id; }

    public Vector2 getPoint() { return point; }

}
