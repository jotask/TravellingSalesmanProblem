package com.github.jotask.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

/**
 * World
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class World {

    private static int CITIES = 23;

    private LinkedList<City> cities;

    public World() {
        this.cities = new LinkedList<City>();
        for(int i = 0; i < CITIES; i++){
            final City city = getCity(i);
            cities.push(city);
        }
    }

    private City getCity(final int id){
        final float WEIGHT = Gdx.graphics.getWidth();
        final float HEIGHT = Gdx.graphics.getHeight();

        final Vector2 p = new Vector2();

        final float R = City.SIZE;

        main: while(true){

            p.x = MathUtils.random(0 + City.SIZE, WEIGHT - City.SIZE);
            p.y = MathUtils.random(0 + City.SIZE, HEIGHT - City.SIZE);

                for (final City c : this.cities) {
                    if(c == null){
                        continue;
                    }
                    final Vector2 o = c.getPoint();
                    final float dx = o.x - p.x;
                    final float dy = o.y - p.y;
                    final float radiusSum = R + R;
                    boolean collision = dx * dx + dy * dy <= radiusSum * radiusSum; // true if collision
                    if (collision) {
                        continue main;
                    }
                }

            break;

        }

        final City city = new City(id, p);
        return city;
    }

    public void render(final ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.BLACK);
        for(final City c: this.cities){
            c.render(sr);
        }
    }

    public LinkedList<City> getCities() { return cities; }

}
