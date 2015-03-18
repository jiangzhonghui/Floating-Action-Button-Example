package com.metova.floatingactionbuttonexample;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

public class FloatingActionButton extends View {

    final static OvershootInterpolator overshootInterpolator = new OvershootInterpolator();
    final static AccelerateInterpolator accelerateInterpolator = new AccelerateInterpolator();

    Context context;
    Paint mButtonPaint;
    Paint mDrawablePaint;
    Paint mTextPaint;
    Bitmap mBitmap;
    String mText;
    boolean mHidden = false;

    // Possible translation states for animating between gravities
    float[] gravTop;
    float[] gravLeft;
    float[] gravCenter;
    float[] gravRight;
    float[] gravBottom;

    private static enum GravityType {

        GravityHorizontalCenter(1),     // (0x00000001)
        GravityLeft(3),                 // (0x00000003)
        GravityRight(5),                // (0x00000005)
        GravityVerticalCenter(16),      // (0x00000010)
        GravityCenter(17),              // (0x00000011)
        GravityTop(48),                 // (0x00000030)
        GravityBottom(80);              // (0x00000050)

        private int val;

        private GravityType(int val) {
            this.val = val;
        }
    }

    public FloatingActionButton(Context context) {
        super(context);
        this.context = context;
        init(Color.WHITE);
    }

    public void setFloatingActionButtonColor(int FloatingActionButtonColor) {
        init(FloatingActionButtonColor);
    }

    public void setFloatingActionButtonDrawable(Drawable floatingActionButtonDrawable) {

        if(floatingActionButtonDrawable == null) {
            mBitmap = null;
        }
        else {
            mBitmap = ((BitmapDrawable) floatingActionButtonDrawable).getBitmap();
        }

        invalidate();
    }

    public void setFloatingActionButtonText(String text, int color) {

        mText = text;

        mTextPaint = new Paint();
        mTextPaint.setColor(color);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DeviceUtil.pxFromDp(this.context, 14));

        invalidate();
    }

    public void init(int FloatingActionButtonColor) {

        setWillNotDraw(false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mButtonPaint.setColor(FloatingActionButtonColor);
        mButtonPaint.setStyle(Paint.Style.FILL);
        mButtonPaint.setShadowLayer(10.0f, 0.0f, 3.5f, Color.argb(100, 0, 0, 0));
        mDrawablePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(DeviceUtil.pxFromDp(this.context, 14));

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        setClickable(true);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, (float) (getWidth() / 2.6), mButtonPaint);

        if (mBitmap != null) {

            canvas.drawBitmap(mBitmap, (getWidth() - mBitmap.getWidth()) / 2,
                    (getHeight() - mBitmap.getHeight()) / 2, mDrawablePaint);
        }

        if (mText != null && !mText.isEmpty()) {

            int xPos = (canvas.getWidth() / 2);
            int yPos = (int) ((canvas.getHeight() / 2) - ((mTextPaint.descent() + mTextPaint.ascent()) / 2));

            canvas.drawText(mText, xPos, yPos, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setAlpha(1.0f);
        } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
            setAlpha(0.6f);
        }
        return super.onTouchEvent(event);
    }

    public void hideFloatingActionButton() {

        if (!mHidden) {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 1, 0);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 1, 0);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(scaleX, scaleY);
            animSetXY.setInterpolator(accelerateInterpolator);
            animSetXY.setDuration(100);
            animSetXY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                    setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                    setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animSetXY.start();
            mHidden = true;
        }
    }

    public void showFloatingActionButton() {

        if (mHidden) {

            setVisibility(View.VISIBLE);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(this, "scaleX", 0, 1);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(this, "scaleY", 0, 1);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(scaleX, scaleY);
            animSetXY.setInterpolator(overshootInterpolator);
            animSetXY.setDuration(200);
            animSetXY.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                    setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                    setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                    setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animSetXY.start();
            mHidden = false;
        }
    }

    public void setGravityPositions(float[] left, float[] top, float[] right, float[] bottom, float[] center) {

        this.gravTop = top;
        this.gravLeft = left;
        this.gravCenter = center;
        this.gravRight = right;
        this.gravBottom = bottom;
    }

    public void slideToGravity(int gravity) {

        ViewPropertyAnimator animator = animate().setDuration(300).setInterpolator(new AccelerateDecelerateInterpolator());
        float x = 0f, y = 0f;

        // center vertical
        if((gravity & GravityType.GravityVerticalCenter.val) == GravityType.GravityVerticalCenter.val) {

            y = gravCenter[1];
        }

        // center horizontal
        if((gravity & GravityType.GravityHorizontalCenter.val) == GravityType.GravityHorizontalCenter.val) {

            x = gravCenter[0];
        }

        // center
        if((gravity & GravityType.GravityHorizontalCenter.val) == GravityType.GravityHorizontalCenter.val) {

            x = gravCenter[0];
            y = gravCenter[1];
        }

        // top
        if((gravity & GravityType.GravityTop.val) == GravityType.GravityTop.val) {

            y = gravTop[1];
        }

        // bottom
        if((gravity & GravityType.GravityBottom.val) == GravityType.GravityBottom.val) {

            y = gravBottom[1];
        }

        // left
        if((gravity & GravityType.GravityLeft.val) == GravityType.GravityLeft.val) {

            x = gravLeft[0];
        }

        // right
        if((gravity & GravityType.GravityRight.val) == GravityType.GravityRight.val) {

            x = gravRight[0];
        }

        animator.x(x).y(y).start();
    }

    public boolean isHidden() {
        return mHidden;
    }

    static public class Builder {

        private FrameLayout.LayoutParams params;
        private final Activity activity;
        int gravity = Gravity.BOTTOM | Gravity.RIGHT; // default bottom right
        Drawable drawable;
        int color = Color.WHITE;
        int size = 0;
        float scale = 0;
        String text;
        int textColor = Color.WHITE;
        boolean startHidden = false;

        public Builder(Activity context) {

            this.activity = context;

            scale = context.getResources().getDisplayMetrics().density;
            size = convertToPixels(72, scale); // default size is 72dp by 72dp
            params = new FrameLayout.LayoutParams(size, size);
        }

        /**
         * Sets the gravity for the FAB
         */
        public Builder withGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        /**
         * Sets the margins for the FAB in dp
         */
        public Builder withMargins(int left, int top, int right, int bottom) {
            params.setMargins(
                    convertToPixels(left, scale),
                    convertToPixels(top, scale),
                    convertToPixels(right, scale),
                    convertToPixels(bottom, scale));
            return this;
        }

        /**
         * Sets the FAB drawable
         */
        public Builder withDrawable(final Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        /**
         * Sets the FAB color
         */
        public Builder withButtonColor(final int color) {
            this.color = color;
            return this;
        }

        public Builder withText(final String text, final int color) {
            this.text = text;
            this.textColor = color;
            return this;
        }

        /**
         * Sets the FAB size in dp
         */
        public Builder withButtonSize(int size) {
            size = convertToPixels(size, scale);
            params = new FrameLayout.LayoutParams(size, size);
            return this;
        }

        public Builder withVisibility(boolean visible) {

            this.startHidden = !visible;
            return this;
        }

        public FloatingActionButton create() {

            final FloatingActionButton button = new FloatingActionButton(activity);
            button.setFloatingActionButtonColor(this.color);
            if (this.drawable != null) {
                button.setFloatingActionButtonDrawable(this.drawable);
            }
            button.setFloatingActionButtonText(this.text, this.textColor);

            ViewGroup root = (ViewGroup) activity.findViewById(android.R.id.content);
            root.addView(button, params);

            // Use the gravity to determine the position of the view (don't directly set gravity because we need to animate translation)
            setupGravityPosition(button);
            initGravAnimatePositions(button);

            if(this.startHidden) { button.setVisibility(View.GONE); button.mHidden = true; }
            return button;
        }

        private void initGravAnimatePositions(FloatingActionButton button) {

            ViewGroup root = (ViewGroup) activity.findViewById(android.R.id.content);
            int width = root.getMeasuredWidth();
            int height = root.getMeasuredHeight();
            float offset = (size / 2f);

            float[] left = new float[]{0f, (height / 2f) - offset};
            float[] top = new float[]{(width / 2f) - offset, 0f};
            float[] right = new float[]{width - size, (height / 2f) - offset};
            float[] bottom = new float[]{(width / 2f) - offset, height - size};
            float[] center = new float[]{(width / 2f) - offset, (height / 2f) - offset};

            button.setGravityPositions(left, top, right, bottom, center);
        }

        private void setupGravityPosition(FloatingActionButton button) {

            ViewGroup root = (ViewGroup) activity.findViewById(android.R.id.content);
            float x = 0f;
            float y = 0f;

            // center vertical
            if((this.gravity & GravityType.GravityVerticalCenter.val) == GravityType.GravityVerticalCenter.val) {

                y = (root.getMeasuredHeight() / 2f) - (size / 2f);
            }

            // center horizontal
            if((this.gravity & GravityType.GravityHorizontalCenter.val) == GravityType.GravityHorizontalCenter.val) {

                x = (root.getMeasuredWidth() / 2f) - (size / 2f);
            }

            // center
            if((this.gravity & GravityType.GravityHorizontalCenter.val) == GravityType.GravityHorizontalCenter.val) {

                y = (root.getMeasuredHeight() / 2f) - (size / 2f);
                x = (root.getMeasuredWidth() / 2f) - (size / 2f);
            }

            // top
            if((this.gravity & GravityType.GravityTop.val) == GravityType.GravityTop.val) {

                y = 0f;
            }

            // bottom
            if((this.gravity & GravityType.GravityBottom.val) == GravityType.GravityBottom.val) {

                y = root.getMeasuredHeight() - size;
            }

            // left
            if((this.gravity & GravityType.GravityLeft.val) == GravityType.GravityLeft.val) {

                x = 0f;
            }

            // right
            if((this.gravity & GravityType.GravityRight.val) == GravityType.GravityRight.val) {

                x = root.getMeasuredWidth() - size;
            }

            button.setX(x);
            button.setY(y);
        }

        private int convertToPixels(int dp, float scale) {

            return (int)(dp * scale + 0.5f);
        }
    }
}