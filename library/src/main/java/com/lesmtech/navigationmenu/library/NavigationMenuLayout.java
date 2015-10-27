package com.lesmtech.navigationmenu.library;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * The view includes a fixed top bar, a customized header, content, bottom bar.
 *
 * @author Rindt
 * @version 0.1
 * @since 10/26/15
 */
public class NavigationMenuLayout extends FrameLayout {

    public Handler mHandler = new Handler();

    public final static String TAG = "nevigationmenulayout";

    private Context mContext;

    private NavigationMenuListener mNavigationMenuListener;

    public void setNavigationMenuListener(NavigationMenuListener mNavigationMenuListener) {
        this.mNavigationMenuListener = mNavigationMenuListener;
    }

    public interface NavigationMenuListener {
        void hided();

        void showed();
    }

    // For items in the content container
    private final int ANIMATION_DURATION = 175;

    private int colorLineDivider = R.color.default_line_divider;

    private int colorBackground = R.color.default_background;

    // Header Part
    FrameLayout headerContainer;
    View headerChildView;

    // Content Part
    FrameLayout contentContainer;

    ArrayList<MenuItem> menuItems;

    // Bottom Part
    FrameLayout bottomContainer;

    ImageView btnSearch;

    ImageView btnClose;

    public NavigationMenuLayout(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public NavigationMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public NavigationMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    public NavigationMenuLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initView();
    }

    private void initView() {
        addView(LayoutInflater.from(mContext).inflate(R.layout.layout_navigation_menu, null));

        this.setVisibility(INVISIBLE);

        headerContainer = (FrameLayout) findViewById(R.id.header_container);
        contentContainer = (FrameLayout) findViewById(R.id.content_container);
        bottomContainer = (FrameLayout) findViewById(R.id.bottom_container);
        btnSearch = (ImageView) findViewById(R.id.action_search);
        btnClose = (ImageView) findViewById(R.id.action_cancel);

        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "search");
            }
        });

        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "close");
                // Close with animation
                hide();
            }
        });
    }

    // ------------  All public methods -------------------
    public void addHeaderLayout(View view) {
        view.setVisibility(INVISIBLE);
        headerChildView = view;
        headerContainer.addView(view);
    }

    public void addContentLayout(ViewGroup view, ArrayList<MenuItem> menuItems) {

        contentContainer.addView(view);

        this.menuItems = menuItems;
        // Might create a customized menu item for layout
    }

    public void addBottomContainer(View view) {
        bottomContainer.addView(view);
    }

    public void hide() {
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_up);
        anim.setAnimationListener(thisHideAnimationLsn);
        startAnimation(anim);
        // Do the state keep
    }

    // 1. The menu shows up
    // 2. The item shows orderly
    // 3. The header shows
    public void show() {

        // this, header and items animation
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_down);

        // Menu Animation
        if (headerContainer != null) {
            anim.setAnimationListener(thisShowAnimationLsn);
        }
        startAnimation(anim);

        // Menu Item starts a little bit after menu animation
        // Menu items in content starts animation orderly
        if (menuItems != null && menuItems.size() != 0) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        NavigationMenuLayout.this.showMenu();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // Check the menuItem whether is null before using this method
    // Show item orderly
    private void showMenu() {
        for (int i = 0; i < menuItems.size(); i++) {
            final double position = i;
            final double delay = 3 * ANIMATION_DURATION * (position / menuItems.size());
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (position < menuItems.size()) {
                        animationItem((int) position);
                    }
                }
            }, (long) delay);
        }
    }

    private void animationItem(int position) {
        final View view = menuItems.get(position).getView();
        view.setVisibility(View.INVISIBLE);
        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.clearAnimation();
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        view.startAnimation(anim);
    }

    // ------------------ Animation Listeners -------------------
    // The show animation listener for the view
    private Animation.AnimationListener thisShowAnimationLsn = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            headerContainer.setVisibility(INVISIBLE);
            // Menu item should be invisible, so don't have to set container to invisible
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            // header animation, startOffSet 1s
            Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left);
            anim.setAnimationListener(headerAnimationListener);
            headerContainer.startAnimation(anim);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    // The hide animation for the view
    private Animation.AnimationListener thisHideAnimationLsn = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            NavigationMenuLayout.this.setVisibility(INVISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            NavigationMenuLayout.this.setVisibility(GONE);
            clearAnimation();
            headerContainer.clearAnimation();
            for (int i = 0; i < menuItems.size(); i++) {
                menuItems.get(i).getView().setVisibility(INVISIBLE);
            }
            if(mNavigationMenuListener != null) {
                mNavigationMenuListener.hided();
            }
            else{
                Log.d("navigation_menu_log", "set listener is required.");
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    private Animation.AnimationListener headerAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            for (int i = 0; i < headerContainer.getChildCount(); i++) {
                final View view = headerContainer.getChildAt(i);
                final Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_fast);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        view.setVisibility(VISIBLE);
                        view.clearAnimation();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(anim);
            }
            if(mNavigationMenuListener != null) {
                mNavigationMenuListener.showed();
            }
            else{
                Log.d("navigation_menu_log", "set listener is required.");
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

}
