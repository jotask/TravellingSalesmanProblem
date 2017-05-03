package com.github.jotask;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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

	private Stage stage;

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

		this.world = new World(this);
		this.pop = new Population(this.world.getCities());

		this.gui = new Gui(this);

		this.stage = new Stage(viewport, this.sb);
		{
			final Skin skin = new Skin(Gdx.files.internal("skin/plain-james-ui.json"));
			if(false){
				final Table table = new Table();
				table.setFillParent(true);
				{
					final Label l = new Label("Cities:", skin);
					final Label o = new Label("", skin);
					table.add(l);
					final Slider s = new Slider(1, 500, 1, false, skin);
					s.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							final Integer i = (int) s.getValue();
							final String s = String.valueOf(i);
							o.setText(s);
						}
					});
					table.add(s);
					table.add(o);
				}
				table.row();
				{
					final Label l = new Label("Population:", skin);
					final Label o = new Label("", skin);
					table.add(l);
					final Slider s = new Slider(1, 500, 1, false, skin);
					s.addListener(new ChangeListener() {
						@Override
						public void changed(ChangeEvent event, Actor actor) {
							final Integer i = (int) s.getValue();
							final String s = String.valueOf( i );
							o.setText(s);
						}
					});
					table.add(s);
					table.add(o);
				}
				table.align(Align.topRight);
				this.stage.addActor(table);
			}
			final Table table = new Table();
			table.setFillParent(true);
			final TextButton btn = new TextButton("Reset", skin);
			btn.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					reset();
				}
			});
			table.add(btn);
			table.align(Align.topRight);
			table.pad(5f);
			this.stage.addActor(table);
			Gdx.input.setInputProcessor(this.stage);
		}
		this.reset();
	}

	private void reset(){
		this.world.reset();
		this.pop.reset();
	}

	@Override
	public void render () {

		this.pop.evaluate();
		this.stage.act();

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

		this.stage.draw();

	}

	@Override
	public void resize(int width, int height){
		this.viewport.update(width,height);
		this.cam.position.set(this.cam.viewportWidth / 2f,this.cam.viewportHeight / 2f, 0);
	}
	
	@Override
	public void dispose () {
		this.stage.dispose();
		this.font.dispose();
		this.sb.dispose();
		this.sr.dispose();
	}

	public OrthographicCamera getCam() { return cam; }

	public Population getPop() { return pop; }

}
