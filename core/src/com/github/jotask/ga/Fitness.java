package com.github.jotask.ga;

import com.badlogic.gdx.math.Vector2;
import com.github.jotask.world.City;

import java.util.LinkedList;

/**
 * Fitness
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
class Fitness {

    private final LinkedList<City> cities;

    public Fitness(LinkedList<City> cities) {
        this.cities = cities;
    }

    public void evaluate(Genome gen){
        double sum = 0.0;
        final LinkedList<Integer> order = gen.getOrder();
        for(int i = 1; i < order.size(); i++){
            final Vector2 a = getCity(order.get(i - 1)).getPoint();
            final Vector2 b = getCity(order.get(i)).getPoint();
            double dst = a.dst(b);
            sum += dst;
        }
        gen.fitness = sum;
    }

    private City getCity(final int id){
        for(final City c: this.cities){
            if(id == c.getId()){
                return c;
            }
        }
        return null;
    }

}
