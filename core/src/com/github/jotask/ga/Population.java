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

    private static final float CROSSOVER_RATE = .75f;
    private static final float MUTATION_RATE  = 0.1f;

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
            final Genome child;
            if(MathUtils.random() < CROSSOVER_RATE){
                final Genome father = selection();
                child = crossover(g, father);
            }else{
                child = g;
            }

            if(MathUtils.random() < MUTATION_RATE)
                child.mutate();

            childs.push(child);

        }
        this.genes.clear();
        this.genes.addAll(childs);
    }

    public Genome selection(){
        return this.genes.get(MathUtils.random(this.genes.size() - 1));
    }

    public void render(final ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        {
            final LinkedList<Integer> order = this.always.getOrder();
            for (int i = 1; i < order.size(); i++) {
                final City a = getCity(order.get(i - 1));
                final City b = getCity(order.get(i));
                final Vector2 z = a.getPoint();
                final Vector2 x = b.getPoint();
                sr.rectLine(z.x, z.y, x.x, x.y, 15, a.getColor(), b.getColor());
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

    private Genome crossover(Genome mother, Genome father){

        if(father.fitness < mother.fitness){
            final Genome tmp = mother;
            mother = father;
            father = tmp;
        }

        final LinkedList<Integer> a = mother.getOrder();
        final LinkedList<Integer> b = mother.getOrder();

        final LinkedList<Integer> child = new LinkedList<Integer>();
        final int start = MathUtils.random(a.size() - 1);
        final int end = MathUtils.random(start, a.size() - 1);
        for(int i = start; i < end; i++)
            child.push(a.get(i));

        maiin: for(int i = 0; i < b.size(); i++){
            int f = b.get(i);
            for(int z = 0; z < child.size(); z++){
               if(f == child.get(z))
                  continue maiin;
            }
            child.push(f);
        }

        final double fitness = Math.max(mother.fitness, father.fitness);
        final Genome c = new Genome(fitness, child);
        return c;

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
