package com.pedrocarrillo.scrollablefixedview;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

public class ScrollingActivity extends AppCompatActivity {

    NestedScrollView nestedScrollView;
    View stackedView;
    Space placeholder;
    int threshold = 60;
    float initialPosition = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nestedScrollView = (NestedScrollView) findViewById(R.id.nested);
        stackedView = findViewById(R.id.stacked_view);


        placeholder = (Space) findViewById(R.id.placeholder);
        final Rect scrollBounds = new Rect();
        nestedScrollView.getHitRect(scrollBounds);

        stackedView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (initialPosition == 0f) {
                    initialPosition = stackedView.getY();
                    nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                        @Override
                        public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            final int[] location = {0, 0};
                            placeholder.getLocationOnScreen(location);
                            // Check if the placeholder is visible in the scroll
                            if (placeholder.getLocalVisibleRect(scrollBounds)) {
                                // if it's visible and our view is hidden, we should show it
                                if (stackedView.getVisibility() == View.GONE) {
                                    stackedView.setVisibility(View.VISIBLE);
                                }
                                // updating the views Y according to the placeholders Y
                                stackedView.setY(location[1] - stackedView.getHeight() - (threshold));
                            } else {
                                // If the placeholder it's not visible, we should decide whether we want to
                                // hide or not the view. In this case if the view comes from the bottom we hide it. (scrollBounds.bottom > 0)
                                if (location[1] > scrollBounds.bottom && scrollBounds.bottom > 0) {
                                    stackedView.setVisibility(View.VISIBLE);
                                    stackedView.setY(initialPosition - stackedView.getHeight());
                                } else {
                                    stackedView.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                }
            }
        });
     }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
