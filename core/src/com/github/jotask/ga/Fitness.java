package com.github.jotask.ga;

import com.badlogic.gdx.math.Vector2;
import com.github.jotask.world.World;

import java.util.LinkedList;

/**
 * Fitness
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
class Fitness {

    private final World world;

    public Fitness(final World world) {
        this.world = world;
    }

    public void evaluate(Genome gen){
        double sum = 0.0;
        final LinkedList<Integer> order = gen.getOrder();
        for(int i = 1; i < order.size(); i++){
            final Vector2 a = this.world.getCity(order.get(i - 1)).getPoint();
            final Vector2 b = this.world.getCity(order.get(i)).getPoint();
            double dst = a.dst(b);
            sum += dst;
        }
        gen.fitness = sum;
    }

}
