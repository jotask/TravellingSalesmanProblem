package com.github.jotask.ga;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.jotask.world.City;

import java.util.LinkedList;

/**
 * Population
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class Population {

    // kimo loves you!
    private int POPULATION = 300;

    private int generation;
    private Fitness fitness;

    private LinkedList<Genome> genes;

    private Genome best;

    private Genome always;

    private final LinkedList<City> cities;

    public Population(final LinkedList<City> cities){
        this.cities = cities;
        this.genes = new LinkedList<Genome>();
        this.fitness = new Fitness(this.cities);
    }

    public void init(){
        this.generation = 0;
        this.genes.clear();
        for(int i = 0; i < POPULATION; i++){
            final Genome genome = new Genome(this.cities);
            this.genes.push(genome);
        }
    }

    public void evaluate(){
        this.best = this.genes.getFirst();
        for(final Genome g: this.genes){
            this.fitness.evaluate(g);
            if(this.best.fitness > g.fitness){
                this.best = g;
            }
        }
        this.newGeneration();
    }

    public void newGeneration(){
        if(this.always == null) {
            this.always = best;
        }else if(always.fitness > this.best.fitness){
            this.always = best;
        }
        this.generation++;
        final LinkedList<Genome> childs = new LinkedList<Genome>();
        for(final Genome g: this.genes){
            final Genome child = new Genome(g);
            if(MathUtils.randomBoolean())
                child.mutate();
            childs.push(child);
        }
        this.genes.clear();
        this.genes.addAll(childs);
    }

    public void render(final ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        {
            sr.setColor(Color.RED);
            final LinkedList<Integer> order = this.always.getOrder();
            for (int i = 1; i < order.size(); i++) {
                final Vector2 a = getCity(order.get(i - 1)).getPoint();
                final Vector2 b = getCity(order.get(i)).getPoint();
                sr.rectLine(a, b, 10);
            }
        }

        {
            sr.setColor(Color.BLACK);
            final LinkedList<Integer> order = this.best.getOrder();
            for (int i = 1; i < order.size(); i++) {
                final Vector2 a = getCity(order.get(i - 1)).getPoint();
                final Vector2 b = getCity(order.get(i)).getPoint();
                sr.rectLine(a, b, 3);
            }
        }
    }

    private void crossover(Genome mother, Genome father){

        if(father.fitness < mother.fitness){
            final Genome tmp = mother;
            mother = father;
            father = tmp;
        }

        final LinkedList<Integer> a = mother.getOrder();
        final LinkedList<Integer> b = father.getOrder();

        // TODO crossover

    }

    private City getCity(final int id){
        for(final City c: this.cities){
            if(id == c.getId()){
                return c;
            }
        }
        return null;
    }

    public int getGeneration() { return generation; }
    public Genome getAlways() { return always; }
    public Genome getBest() { return best; }

}
