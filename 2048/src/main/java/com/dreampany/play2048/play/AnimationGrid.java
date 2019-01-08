package com.dreampany.play2048.play;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hawladar Roman on 12/26/2018.
 * BJIT Group
 * hawladar.roman@bjitgroup.com
 */
public class AnimationGrid {

    public List<AnimationCell>[][] field;
    int activeAnimations = 0;
    boolean oneMoreFrame = false;
    public ArrayList<AnimationCell> globalAnimation = new ArrayList<AnimationCell>();

    @SuppressWarnings("unchecked")
    public AnimationGrid(int x, int y) {
        field = new ArrayList[x][y];

        for (int xx = 0; xx < x; xx++) {
            for (int yy = 0; yy < y; yy++) {
                field[xx][yy] = new ArrayList<>();
            }
        }
    }

    public void startAnimation(int x, int y, int animationType, long length,
                               long delay, int[] extras) {
        AnimationCell animationToAdd = new AnimationCell(x, y, animationType,
                length, delay, extras);
        if (x == -1 && y == -1) {
            globalAnimation.add(animationToAdd);
        } else {
            field[x][y].add(animationToAdd);
        }
        activeAnimations = activeAnimations + 1;
    }

    public void tickAll(long timeElapsed) {
        ArrayList<AnimationCell> cancelledAnimations = new ArrayList<AnimationCell>();
        for (AnimationCell animation : globalAnimation) {
            animation.tick(timeElapsed);
            if (animation.animationDone()) {
                cancelledAnimations.add(animation);
                activeAnimations = activeAnimations - 1;
            }
        }

        for (List<AnimationCell>[] array : field) {
            for (List<AnimationCell> list : array) {
                for (AnimationCell animation : list) {
                    animation.tick(timeElapsed);
                    if (animation.animationDone()) {
                        cancelledAnimations.add(animation);
                        activeAnimations = activeAnimations - 1;
                    }
                }
            }
        }

        for (AnimationCell animation : cancelledAnimations) {
            cancelAnimation(animation);
        }
    }

    public boolean isAnimationActive() {
        if (activeAnimations != 0) {
            oneMoreFrame = true;
            return true;
        } else if (oneMoreFrame) {
            oneMoreFrame = false;
            return true;
        } else {
            return false;
        }
    }

    public List<AnimationCell> getAnimationCell(int x, int y) {
        return field[x][y];
    }

    public void cancelAnimations() {
        for (List<AnimationCell>[] array : field) {
            for (List<AnimationCell> list : array) {
                list.clear();
            }
        }
        globalAnimation.clear();
        activeAnimations = 0;
    }

    public void cancelAnimation(AnimationCell animation) {
        if (animation.getX() == -1 && animation.getY() == -1) {
            globalAnimation.remove(animation);
        } else {
            field[animation.getX()][animation.getY()].remove(animation);
        }
    }
}
