package states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game1.MyGdxGame;

import sprites.Tube;
import sprites.Bird;


public class PlayState extends  State {

    public static final int tube_spacing = 125;
    public static final int tube_count = 4;
    private static final int ground_y_offset = -30;

    private Bird bird;
    private Texture bg;
    private Texture ground;
    private Vector2 groundPos1, groundPos2;
    private Array<Tube> tubes;

    public PlayState(GameStateManager gsm) {
        super(gsm);
        bird = new Bird(50, 300);
        camera.setToOrtho(false, MyGdxGame.width / 2, MyGdxGame.height/2);
        bg = new Texture("bg.png");
        ground = new Texture("ground.png");
        groundPos1 = new Vector2(camera.position.x - camera.viewportWidth / 2, ground_y_offset);
        groundPos2 = new Vector2((camera.position.x - camera.viewportWidth / 2) + ground.getWidth(), ground_y_offset);

        tubes = new Array<Tube>();
        for (int i = 0; i < tube_count; i++) {
            tubes.add(new Tube(i * (tube_spacing + Tube.tube_width)));
        }

    }

    @Override
    protected void handleInput() {
        if (Gdx.input.justTouched())
            bird.jump();
    }

    @Override
    public void update(float dt) {
        handleInput();
        updateGround();
        bird.update(dt);
        camera.position.x = bird.getPosition() .x + 80;

        for (int i = 0; i < tubes.size; i++ ){

            Tube tube = tubes.get(i);
            if (camera.position.x - (camera.viewportWidth / 2) > tube.getPosTopTube() .x
                + tube.getTopTube() .getWidth()){
                tube.reposition(tube.getPosTopTube().x + ((Tube.tube_width + tube_spacing) * tube_count));
            }

            if (tube.collides(bird.getBounds()))
                gsm.set(new PlayState(gsm));

        }
        camera.update();

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(camera.combined);
        sb.begin();
        sb.draw(bg, camera.position.x - (camera.viewportWidth/2), 0);
        sb.draw(bird.getBird(), bird.getPosition() .x, bird.getPosition() .y);
        for (Tube tube : tubes) {
            sb.draw(tube.getTopTube(), tube.getPosBotTube().x, tube.getPosTopTube().y);
            sb.draw(tube.getBottomTube(), tube.getPosBotTube().x, tube.getPosBotTube().y);
        }
        sb.draw(ground, groundPos1.x, groundPos1.y);
        sb.draw(ground, groundPos2.x, groundPos2.y);

        sb.end();
    }

    @Override
    public void dispose() {
        bg.dispose();
        bird.dispose();
        ground.dispose();
        for (Tube tube: tubes)
            tube.dispose();
        System.out.println(("PlayState Disposed"));
    }
    private void updateGround() {
        if (camera.position.x - (camera.viewportWidth /2) > groundPos1.x + ground.getWidth())
            groundPos1.add(ground.getWidth() *2, 0);
        if (camera.position.x - (camera.viewportWidth /2) > groundPos2.x + ground.getWidth())
        groundPos2.add(ground.getWidth() *2, 0);
    }
}
