package com.github.jotask.ga;

import com.badlogic.gdx.math.MathUtils;
import com.github.jotask.world.City;

import java.util.LinkedList;

import static com.github.jotask.ga.Population.MUTATION_RATE;

/**
 * Genome
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class Genome {

    private final LinkedList<Integer> order;

    public double fitness;
    public double probability;

    public Genome(final LinkedList<City> cities) {
        this.fitness = 0.0;
        this.probability = 0.0;
        this.order = new LinkedList<Integer>();
        for(int i = 0; i < cities.size(); i++){
            this.order.push(i);
        }
        this.shuffle(MathUtils.random(5, 50));
    }

    public Genome(final double fitness, final LinkedList<Integer> order){
        this.fitness = fitness;
        this.order = order;
    }

    public void mutate(){
        for(int i = 0; i < this.order.size(); i++){
            if(MathUtils.random() < MUTATION_RATE) {
                final int a = MathUtils.random(order.size() - 1);
                final int b = (a + 1) % this.order.size();
                swap(a, b);
            }
        }
    }

    private void shuffle(final int n){
        for(int i = 0; i < n; i++){
            this.mutate();
        }
    }

    private void swap(final int i, final int j){
        final int tmp = order.get(i);
        order.set(i, order.get(j));
        order.set(j, tmp);
    }

    public LinkedList<Integer> getOrder() { return order; }

}
