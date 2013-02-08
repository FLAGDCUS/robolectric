package com.xtremelabs.robolectric.shadows;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.view.View;
import android.view.animation.LinearInterpolator;
import com.xtremelabs.robolectric.Robolectric;
import com.xtremelabs.robolectric.WithTestDefaultsRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@RunWith(WithTestDefaultsRunner.class)
public class AnimatorSetTest {
    private Context context;
    private AnimatorSet subject;
    private ObjectAnimator alphaAnimator;
    private ObjectAnimator scaleAnimator;
    private View target;

    @Before
    public void setup() throws Exception {
        context = Robolectric.application;
        subject = new AnimatorSet();
        target = new View(context);
        alphaAnimator = ObjectAnimator.ofFloat(target, "alpha", 0.0f, 3.0f);
        scaleAnimator = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 0.0f);
    }

    @Test
    public void start_withPlayTogether_shouldSetTheInitialValuesOfAllChildAnimators() throws Exception {
        subject.playTogether(alphaAnimator, scaleAnimator);
        subject.setDuration(70);

        subject.start();

        assertThat(target.getAlpha(), equalTo(0.0f));
        assertThat(target.getScaleX(), equalTo(0.5f));
    }

    @Test
    public void startAndWaitForAnimationEnd_withPlayTogether_shouldSetTheFinalValuesOfAllChildAnimators() throws Exception {
        subject.playTogether(alphaAnimator, scaleAnimator);
        subject.setDuration(70);

        subject.start();
        Robolectric.idleMainLooper(70);

        assertThat(target.getAlpha(), equalTo(3.0f));
        assertThat(target.getScaleX(), equalTo(0.0f));
    }

    @Test
    public void setInterpolator_shouldImmediatelySetInterpolatorsOfAllChildren() throws Exception {
        subject.playTogether(alphaAnimator);
        TimeInterpolator expectedInterpolator = new LinearInterpolator();
        subject.setInterpolator(expectedInterpolator);

        assertThat(alphaAnimator.getInterpolator(), sameInstance(expectedInterpolator));
    }

    @Test
    public void start_shouldNotChangeChildDurations_whenDurationNotSet() throws Exception {
        subject.setDuration(0);
        alphaAnimator.setDuration(99);
        subject.playTogether(alphaAnimator);
        subject.start();
        assertThat(alphaAnimator.getDuration(), equalTo(99L));
    }
}
