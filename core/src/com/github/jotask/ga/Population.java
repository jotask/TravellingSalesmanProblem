package com.github.jotask.ga;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.jotask.world.City;
import com.github.jotask.world.World;

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

    private int generation;
    private final Fitness fitness;

    private final LinkedList<Genome> genes;

    private Genome best;

    private Genome always;

    private final World world;

    public Population(final World world){
        this.world = world;
        this.genes = new LinkedList<Genome>();
        this.fitness = new Fitness(world);
    }

    public void reset(){
        init();
    }

    public void init(){
        this.best = null;
        this.always = null;
        this.generation = 0;
        this.genes.clear();
        int POPULATION = 300;
        for(int i = 0; i < POPULATION; i++){
            final Genome genome = new Genome(world.getCities());
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

    private void newGeneration(){
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

    private Genome selection(){
        return this.genes.get(MathUtils.random(this.genes.size() - 1));
    }

    public void render(final ShapeRenderer sr){
        sr.set(ShapeRenderer.ShapeType.Filled);
        {
            final LinkedList<Integer> order = this.always.getOrder();
            for (int i = 1; i < order.size(); i++) {
                final City a = this.world.getCity(order.get(i - 1));
                final City b = this.world.getCity(order.get(i));
                final Vector2 z = a.getPoint();
                final Vector2 x = b.getPoint();
                sr.rectLine(z.x, z.y, x.x, x.y, 15, a.getColor(), b.getColor());
            }
        }

        {
            sr.setColor(Color.BLACK);
            final LinkedList<Integer> order = this.best.getOrder();
            for (int i = 1; i < order.size(); i++) {
                final Vector2 a = this.world.getCity(order.get(i - 1)).getPoint();
                final Vector2 b = this.world.getCity(order.get(i)).getPoint();
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

        maiin: for (Integer aB : b) {
            int f = aB;
            for (Integer aChild : child) {
                if (f == aChild)
                    continue maiin;
            }
            child.push(f);
        }

        final double fitness = Math.max(mother.fitness, father.fitness);
        return new Genome(fitness, child);

    }

    public int getGeneration() { return generation; }
    public Genome getAlways() { return always; }
    public Genome getBest() { return best; }

}
