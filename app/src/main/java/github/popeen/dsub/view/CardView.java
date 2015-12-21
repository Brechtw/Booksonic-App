package github.popeen.dsub.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import github.popeen.dsub.R;

public class CardView extends FrameLayout{
	public CardView(Context context) {
		super(context);
		init(context);
	}

	public CardView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public CardView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context);
	}

	@Override
	public void onDraw(Canvas canvas) {
		Path clipPath = new Path();

		float roundedDp = getResources().getDimension(R.dimen.Card_Radius);
		clipPath.addRoundRect(new RectF(canvas.getClipBounds()), roundedDp, roundedDp, Path.Direction.CW);
		canvas.clipPath(clipPath);
		super.onDraw(canvas);
	}

	private void init(Context context) {
		setClipChildren(true);
		setBackgroundResource(R.drawable.card_rounded_corners);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			setElevation(10.0f);
		}
	}
}
