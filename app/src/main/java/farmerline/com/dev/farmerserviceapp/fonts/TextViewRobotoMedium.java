package farmerline.com.dev.farmerserviceapp.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

public class TextViewRobotoMedium extends android.support.v7.widget.AppCompatTextView {

    public TextViewRobotoMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Typeface typeface=Typeface.createFromAsset(getContext().getAssets(),"Roboto-Regular.ttf");
        setTypeface(typeface);
    }
}
