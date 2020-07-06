package com.google.firebase.quickstart.database.controller.access.ui.activity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.quickstart.database.R;
import com.google.firebase.quickstart.database.controller.access.ui.fragment.BaseFragment;
import com.google.firebase.quickstart.database.controller.access.ui.fragment.ChatListFragment;
import com.google.firebase.quickstart.database.controller.access.ui.fragment.ContactFragment;
import com.google.firebase.quickstart.database.entity.Contact;
import com.google.firebase.quickstart.database.util.Constant;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements
        ContactFragment.OnListFragmentInteractionListener,
        ChatListFragment.OnListFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean isRunnning;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private BaseFragment fragment;
    private Toolbar searchToolBar;
    private Toolbar toolbar;
    private AppBarLayout appBar;
    private AppBarLayout searchAppBar;
    private View searchAppBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBar = (AppBarLayout) findViewById(R.id.appBar);
        searchAppBar = (AppBarLayout) findViewById(R.id.appBar_search);
        searchAppBarLayout = findViewById(R.id.layout_appbar_search);

        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        searchToolBar = (Toolbar) findViewById(R.id.toolbar_search);
        if (searchToolBar != null) {
            searchToolBar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
//            searchToolBar.setVisibility(View.GONE);
            searchAppBarLayout.setVisibility(View.GONE);
            searchToolBar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hideSearch();
                }
            });

            EditText editText = (EditText) findViewById(R.id.editText_search);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    fragment.search(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }


        initTabLayout();
        initViewPager();

    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunnning = true;


        appBar.setExpanded(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunnning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).select();
    }

    @Override
    public void onBackPressed() {
        if (searchAppBarLayout.getVisibility() == View.VISIBLE)
            hideSearch();
        else
            super.onBackPressed();

    }

    List<BaseFragment> fragments = new ArrayList<>();

    private void initViewPager() {
        fragments.add(ChatListFragment.newInstance(1));
        fragments.add(ContactFragment.newInstance(1));

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position < fragments.size())
                    return fragments.get(position);
                else
                    return new BaseFragment();
            }

            @Override
            public int getCount() {
                return tabLayout.getTabCount();
            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void initTabLayout() {

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TabLayout.Tab[] tabs = new TabLayout.Tab[]{
                tabLayout.newTab().setText(R.string.chat),
                tabLayout.newTab().setText(R.string.contact)
        };

        for (TabLayout.Tab tab : tabs) {
            View customView = getLayoutInflater().inflate(R.layout.tablayout_tab_view, tabLayout, false);
            TextView titleTextView = (TextView) customView.findViewById(R.id.textView_title);
            TextView countTextView = (TextView) customView.findViewById(R.id.textView_count);
            ImageView countImageView = (ImageView) customView.findViewById(R.id.imageView_count);
            View countLayout = customView.findViewById(R.id.layout_count);
            countLayout.setVisibility(View.GONE);
            countImageView.setColorFilter(android.support.v4.content.ContextCompat.getColor(this, R.color.colorWhite));

            customView.setAlpha(0.7f);

            titleTextView.setText(tab.getText());
            tab.setCustomView(customView);
            tabLayout.addTab(tab);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                toolbar.setTitle(tab.getText());
                viewPager.setCurrentItem(tab.getPosition());
                hideSearch();

                fragment = fragments.get(tab.getPosition());
                tab.getCustomView().setAlpha(1);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getCustomView().setAlpha(0.7f);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected()");
                toolbar.setTitle(tab.getText());
                tab.getCustomView().setAlpha(1);
                fragment = fragments.get(tab.getPosition());

            }
        });


    }

    private void hideSearch() {
//        searchToolBar.setVisibility(View.GONE);
//        appBar.setExpanded(true);


        // get the center for the clipping circle

        int cx = toolbar.getWidth() - toolbar.getHeight() / 2;
        int cy = (toolbar.getTop() + toolbar.getBottom()) / 2;

        // get the final radius for the clipping circle
        int dx = Math.max(cx, toolbar.getWidth() - cx);
        int dy = Math.max(cy, toolbar.getHeight() - cy);
        float finalRadius = (float) Math.hypot(dx, dy);

        Animator animator;
        animator = io.codetail.animation.ViewAnimationUtils
                .createCircularReveal(searchAppBarLayout, cx, cy, finalRadius, 0);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                toolbar.setAlpha(1f);
                searchAppBarLayout.setVisibility(View.GONE);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        animator.start();


        appBar.setVisibility(View.VISIBLE);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(appBar, "translationY", 0),
                ObjectAnimator.ofFloat(appBar, "alpha", 1),
                ObjectAnimator.ofFloat(viewPager, "translationY", 0)
        );
        set.setDuration(100).start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            toolbar.setAlpha(0);

            AnimatorSet set = new AnimatorSet();
            set.playTogether(
                    ObjectAnimator.ofFloat(appBar, "translationY", -tabLayout.getHeight()),
                    ObjectAnimator.ofFloat(viewPager, "translationY", -tabLayout.getHeight()),
                    ObjectAnimator.ofFloat(appBar, "alpha", 0)
            );
            set.setDuration(100).addListener(new com.nineoldandroids.animation.Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(com.nineoldandroids.animation.Animator animation) {

                }

                @Override
                public void onAnimationEnd(com.nineoldandroids.animation.Animator animation) {
                    appBar.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(com.nineoldandroids.animation.Animator animation) {

                }

                @Override
                public void onAnimationRepeat(com.nineoldandroids.animation.Animator animation) {

                }
            });
            set.start();


            // get the center for the clipping circle

            int cx = toolbar.getWidth() - toolbar.getHeight() / 2;
            int cy = (toolbar.getTop() + toolbar.getBottom()) / 2;

            // get the final radius for the clipping circle
            int dx = Math.max(cx, toolbar.getWidth() - cx);
            int dy = Math.max(cy, toolbar.getHeight() - cy);
            float finalRadius = (float) Math.hypot(dx, dy);

            final Animator animator;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            animator = io.codetail.animation.ViewAnimationUtils
                    .createCircularReveal(searchAppBarLayout, cx, cy, 0, finalRadius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(200);
//                animator.start();

//            } else {
////                searchAppBarLayout.setVisibility(View.VISIBLE);
//                animator = null;
//            }


//            toolbar.postDelayed(new Runnable() {
//                @Override
//                public void run() {
////                    if (animator != null)
//                }
//            }, 100);

            searchAppBarLayout.setVisibility(View.VISIBLE);
            animator.start();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Contact item) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra(ChatActivity.CONTACT, item);
        startActivity(intent);
    }

    @Override
    public void onNumberOfUnreadChatChanged(int size) {
        TabLayout.Tab tab = tabLayout.getTabAt(0);

        View customView = tab.getCustomView();

        if (customView == null) {
            return;
        }
        TextView countTextView = (TextView) customView.findViewById(R.id.textView_count);
        View countLayout = customView.findViewById(R.id.layout_count);
        countLayout.setVisibility(View.GONE);

        if (size > 0) {
            countLayout.setVisibility(View.VISIBLE);
            countTextView.setText(String.valueOf(size));
        } else
            countLayout.setVisibility(View.GONE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.PERMISSION_REQUEST && resultCode == RESULT_OK)
            if (fragment instanceof ContactFragment) {
                ContactFragment contactFragment = (ContactFragment) fragment;
                contactFragment.reload();
            } else
                super.onActivityResult(requestCode, resultCode, data);
    }

}
