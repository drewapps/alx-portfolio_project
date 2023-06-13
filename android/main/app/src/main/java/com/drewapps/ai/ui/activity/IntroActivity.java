package com.drewapps.ai.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.drewapps.ai.R;
import com.drewapps.ai.databinding.ActivityIntroBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    ActivityIntroBinding binding;

    private int max_step = 0;

    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private Button btnNext;
    private List<Image> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        items.add(new Image("write_anim.json", "Write Better, Everywhere",
                "Whether you’re writing emails, captions, or love letters, Copy Builder will help craft the perfect message."));
        items.add(new Image("template_preloader.json", "Use Cases & Template",
                "100+ well structured use cases and templates to choose from to cover all your writing needs"));
        items.add(new Image("language.json", "Create content in 30 languages",
                "Copy Builder can read and write content in 30 languages including English, Spanish, Japanese, and Portuguese."));

        max_step = items.size();
        viewPager = binding.viewPager;
        btnNext = binding.btnNext;

        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setPageTransformer(false, new PageTransformer());
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() + 1;
                if (current < max_step) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                    if (getIntent().hasExtra("FROM_LOGIN")) {
                        startActivity(new Intent(IntroActivity.this, MainActivity.class));
                        finish();
                    } else {
                        finish();
                    }
                }
            }
        });

        binding.btClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("FROM_LOGIN")) {
                    startActivity(new Intent(IntroActivity.this, MainActivity.class));
                    finish();
                } else {
                    finish();
                }
            }
        });

    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = binding.layoutDots;
        ImageView[] dots = new ImageView[max_step];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int height = 8;
            int width = i == current_index ? (height * 15) : (height * 4);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width, height));
            params.setMargins(2, 10, 2, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_rectangle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_80), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        dots[current_index].setImageResource(R.drawable.shape_rectangle);
        dots[current_index].setColorFilter(getResources().getColor(R.color.colorSecondary), PorterDuff.Mode.SRC_IN);
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);

            if (position == max_step - 1) {
                btnNext.setText("DONE");

            } else {
                btnNext.setText("NEXT");
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_intro, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(items.get(position).name);
            ((TextView) view.findViewById(R.id.description)).setText(items.get(position).brief);
//            ((ImageView) view.findViewById(R.id.image)).setImageResource(items.get(position).image);
            ((LottieAnimationView) view.findViewById(R.id.image)).setAnimation(items.get(position).image);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return max_step;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    public static class PageTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        @Override
        public void transformPage(View view, float position) {
            if (position >= -1 || position <= 1) {
                // Modify the default slide transition to shrink the page as well
                final float height = view.getHeight();
                final float width = view.getWidth();
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = height * (1 - scaleFactor) / 2;
                final float horzMargin = width * (1 - scaleFactor) / 2;

                // Center vertically
                view.setPivotY(0.5f * height);
                view.setPivotX(0.5f * width);

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }


    }

    public class Image implements Serializable {

        public String image;
        public String name;
        public String brief;

        public Image(String image, String name, String brief) {
            this.image = image;
            this.name = name;
            this.brief = brief;
        }
    }
}