package com.github.jotask.ga;

import com.badlogic.gdx.math.MathUtils;
import com.github.jotask.world.City;

import java.util.LinkedList;

/**
 * Genome
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class Genome {

    private LinkedList<Integer> order;

    public double fitness;

    public Genome(final LinkedList<City> cities) {
        this.fitness = 0;
        this.order = new LinkedList<Integer>();
        for(int i = 0; i < cities.size(); i++){
            this.order.push(i);
        }
        this.shuffle(MathUtils.random(5, 50));
    }

    public Genome(final Genome genome){
        this.fitness = 0;
        this.order = new LinkedList<Integer>(genome.getOrder());
    }

    public void mutate(){
        final int i = MathUtils.random(order.size() - 1);
        final int j = MathUtils.random(order.size() - 1);
        swap(i, j);
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
