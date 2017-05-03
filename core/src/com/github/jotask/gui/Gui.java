package com.github.jotask.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.jotask.OptimusPrime;

/**
 * Gui
 *
 * @author Jose Vives Iznardo
 * @since 02/05/2017
 */
public class Gui {

    private final OptimusPrime prime;
    private final OrthographicCamera cam;

    public Gui(final OptimusPrime prime) {
        this.prime = prime;
        this.cam = prime.getCam();
    }

    public void render(final SpriteBatch sb, final BitmapFont font){
        font.setColor(Color.BLACK);
        float h = this.cam.viewportHeight;
        float i = 0.5f;
        final float space = 15f;
        font.draw(sb, "Memory: " + String.valueOf(toMb()) + " mb", 5, h - i++ * space);
        font.draw(sb, "Generation: " + String.valueOf(prime.getPop().getGeneration()), 5, h - i++ * space);
        font.draw(sb, "Shortest: " + String.valueOf(cutDecimals(prime.getPop().getAlways().fitness, 3)), 5, h - i++ * space);
        font.draw(sb, "Generation: " + String.valueOf(cutDecimals(prime.getPop().getBest().fitness, 3)), 5, h - i ++ * space);
    }

    private long toMb(){
        return (Gdx.app.getJavaHeap() / 1024) / 1024;
    }

    private double cutDecimals(final double number, final double n){
        double dec = 1;
        for(int i = 0; i < n; i++)
            dec *= 10;
        int temp = (int)(number * dec);
        return ((double)temp) / dec;
    }

}
