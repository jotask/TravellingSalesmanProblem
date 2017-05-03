package com.github.jotask.ga;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.jotask.world.City;
import com.github.jotask.world.World;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Population
 *
 * @author Jose Vives Iznardo
 * @since 01/05/2017
 */
public class Population {

    private static final int POPULATION = 3;
    private static final float CROSSOVER_RATE = .75f;
    static final float MUTATION_RATE  = 0.25f;

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

        Collections.sort(this.genes, new Comparator<Genome>() {
            @Override
            public int compare(Genome o1, Genome o2) {
                if(o1.fitness < o2.fitness) return 1;
                if(o1.fitness > o2.fitness) return -1;
                return 0;
            }
        });
        this.normalizeFitness();

        final LinkedList<Genome> childs = new LinkedList<Genome>();
        for(final Genome g: this.genes){
            final Genome child;
            if(MathUtils.random() < CROSSOVER_RATE){
                final Genome father = poolSelection();
                child = crossover(g, father);
            }else{
                child = g;
                child.mutate();
            }

            childs.push(child);

        }
        this.genes.clear();
        this.genes.addAll(childs);

        this.generation++;

    }

    void printo(){
        StringBuilder sb = new StringBuilder();
        for(final Genome g: this.genes){
            sb.append( ((float)g.fitness) + " : ");
        }
        System.out.println(sb.toString());
    }

    private void normalizeFitness(){
        double sum = 0.0;
        for(final Genome g: this.genes){
            sum += g.fitness;
        }
        for(final Genome g: this.genes){
            g.probability = g.fitness / sum;
        }
    }

    private Genome poolSelection(){

        int index = 0;
        double r = MathUtils.random();

        while(r > 0){
            r = r - this.genes.get(index).probability;
            index++;
        }
        index--;
        return this.genes.get(index);

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
