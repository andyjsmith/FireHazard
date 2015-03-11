package com.andall.firehazard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DubstepGun {

    Music dub1;
    Music dub2;
    ParticleEffect effect;
    boolean dub = false;

    public void loadGun() {
        dub1 = Gdx.audio.newMusic(Gdx.files.internal("sounds/dub1.ogg"));
        dub2 = Gdx.audio.newMusic(Gdx.files.internal("sounds/dub2.ogg"));
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("effects/explode.particle"), Gdx.files.internal("effects"));
    }

    public void update(SpriteBatch sb, int x, int y) {
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            if (!dub1.isPlaying() && !dub2.isPlaying() && dub) {
                dub2.play();
                for (int i = 0; i < effect.getEmitters().size; i++) {
                    effect.getEmitters().get(i).getVelocity().setHigh(1000);
                    /*
                    effect.getEmitters().get(i).getAngle().setHigh(0); // Directional dubstep effects vs radial
                    effect.getEmitters().get(i).getAngle().setLow(0);
                    */
                }
            } else if (!dub1.isPlaying() && !dub2.isPlaying()) {
                dub1.play();
                dub = true;
                for (int i = 0; i < effect.getEmitters().size; i++) {
                    effect.getEmitters().get(i).getVelocity().setHigh(100);
                    /*
                    effect.getEmitters().get(i).getAngle().setHigh(0); // Directional dubstep effects vs radial
                    effect.getEmitters().get(i).getAngle().setLow(0);
                    */
                }
            }
            effect.setPosition(x, y);
            effect.setDuration(0);
            effect.start();
        } else {
            dub1.stop();
            dub2.stop();
            dub = false;
        }
        effect.draw(sb, Gdx.graphics.getRawDeltaTime()); // Draw the particle
        effect.update(Gdx.graphics.getRawDeltaTime()); // Update the particle
    }
}
