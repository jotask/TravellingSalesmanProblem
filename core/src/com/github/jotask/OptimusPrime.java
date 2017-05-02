package com.github.jotask;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.jotask.ga.Population;
import com.github.jotask.gui.Gui;
import com.github.jotask.world.World;

public class OptimusPrime extends ApplicationAdapter {

	private OrthographicCamera cam;
	private Viewport viewport;
	private BitmapFont font;
	private SpriteBatch sb;
	private ShapeRenderer sr;

	private World world;
	private Population pop;
	private Gui gui;

	@Override
	public void create () {
		this.font = new BitmapFont();
		this.sb = new SpriteBatch();
		this.sr = new ShapeRenderer();
		this.sr.setAutoShapeType(true);

		this.cam = new OrthographicCamera();
		this.viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), this.cam);
		this.viewport.apply();
		this.cam.position.set(this.cam.viewportWidth / 2f,this.cam.viewportHeight / 2f, 0);

		this.world = new World();
		this.pop = new Population(this.world.getCities());

		this.gui = new Gui(this);

		this.pop.init();
	}

	@Override
	public void render () {

		this.pop.evaluate();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		this.sr.setProjectionMatrix(this.cam.combined);
		this.sr.begin();
		this.pop.render(sr);
		this.world.render(sr);
		this.sr.end();

		this.sb.setProjectionMatrix(this.cam.combined);
		this.sb.begin();
		this.gui.render(this.sb, this.font);
		this.sb.end();

	}

	@Override
	public void resize(int width, int height){
		this.viewport.update(width,height);
		this.cam.position.set(this.cam.viewportWidth / 2f,this.cam.viewportHeight / 2f, 0);
	}
	
	@Override
	public void dispose () {
		this.font.dispose();
		this.sb.dispose();
		this.sr.dispose();
	}

	public OrthographicCamera getCam() { return cam; }

	public Population getPop() { return pop; }

}
