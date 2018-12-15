

package aman.com.imagesearch.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/** A  widget forcefully maintaining a 1:1 aspect ratio. */
public class SquareImageView extends ImageView {

  //region Constructors
  public SquareImageView(Context context) {
    super(context);
  }

  public SquareImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public SquareImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }
  //endregion

  //region View methods (Force square layout)
  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    // Force 1:1 aspect ratio.
    setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
  }
  //endregion
}
