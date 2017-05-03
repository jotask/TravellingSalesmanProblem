package com.github.jotask.world;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.jotask.OptimusPrime;

import java.util.LinkedList;

/**
 * World
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class World {

    private static final int CITIES = 23;
    private final OptimusPrime prime;

    private final LinkedList<City> cities;

    public World(final OptimusPrime prime) {
        this.prime = prime;
        this.cities = new LinkedList<City>();
    }

    public void reset(){
        this.cities.clear();
        for(int i = 0; i < CITIES; i++){
            final City city = createCity(i);
            cities.push(city);
        }
    }

    private City createCity(final int id){

        final OrthographicCamera cam = this.prime.getCam();
        final float WEIGHT = cam.viewportWidth;
        final float HEIGHT = cam.viewportHeight;

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

        return new City(id, p);
    }

    public City getCity(final int id){
        for(final City c: this.getCities()){
            if(id == c.getId()){
                return c;
            }
        }
        return null;
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
