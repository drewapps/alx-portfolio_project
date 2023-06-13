package com.drewapps.ai.binding;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.drewapps.ai.R;
import com.google.android.material.textview.MaterialTextView;
import com.drewapps.ai.Config;
import com.drewapps.ai.utils.Util;
import com.makeramen.roundedimageview.RoundedImageView;

public class GlideBinding {

    @BindingAdapter("imageURL")
    public static void bindImage(ImageView imageView, String url) {
        Context mContext = imageView.getContext();
        if (isValid(imageView, url)) {
            if (Config.PRE_LOAD_IMAGE) {
                Glide.with(mContext).load(url).thumbnail(Glide.with(mContext).load(url))
                        .into(imageView);
            } else {
                Glide.with(mContext).load(url).placeholder(R.drawable.placeholder).into(imageView);
            }
        }
    }

    @BindingAdapter("imageSVGURL")
    public static void bindImage(AppCompatImageView imageView, String url) {
        Context mContext = imageView.getContext();
        if (isValid(imageView, url)) {
            if (Config.PRE_LOAD_IMAGE) {
                Glide.with(mContext).load(url).thumbnail(Glide.with(mContext).load(url))
                        .into(imageView);
            } else {
                Glide.with(mContext).load(url).placeholder(R.drawable.placeholder).into(imageView);
            }
        }
    }

    @BindingAdapter("imageURL")
    public static void bindImage(RoundedImageView imageView, String url) {
        Context mContext = imageView.getContext();
        if (isValid(imageView, url)) {
            if (Config.PRE_LOAD_IMAGE) {
                Glide.with(mContext).load(url).thumbnail(Glide.with(mContext).load(url)).into(imageView);
            } else {
                Glide.with(mContext).load(url).placeholder(R.drawable.placeholder).into(imageView);
            }
        }
    }

    public static void bindImageWithURI(ImageView imageView, Uri url) {
        Context mContext = imageView.getContext();

        //Glide.with(mContext).load(url).placeholder(R.drawable.spaceholder).into(imageView);

    }

    public static Boolean isValid(ImageView imageView, String url) {
        Context mContext = imageView.getContext();
        return !(url == null
                || imageView == null
                || mContext == null
                || url.equals(""));
    }

    @BindingAdapter("font")
    public static void setFont(TextView textView, String type) {
        switch (type) {
            case "normal":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR));
                break;
            case "bold":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR), Typeface.BOLD);
                break;
            case "extra_bold":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_MEDIUM), Typeface.BOLD);
                break;
            case "super_extra_bold":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_BOLD), Typeface.BOLD);
                break;
            case "bold_italic":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_MEDIUM), Typeface.BOLD_ITALIC);
                break;
            case "italic":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR), Typeface.ITALIC);
                break;
            case "medium":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_MEDIUM));
                break;
            case "light":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_LIGHT));
                break;
            default:
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR));
                break;
        }

    }

    @BindingAdapter("font")
    public static void setFont(Button button, String type) {
        switch (type) {
            case "normal":
                button.setTypeface(Util.getTypeFace(button.getContext(), Util.Fonts.LATO_REGULAR));
                break;
            case "medium":
                button.setTypeface(Util.getTypeFace(button.getContext(), Util.Fonts.LATO_MEDIUM));
                break;
            case "bold":
                button.setTypeface(Util.getTypeFace(button.getContext(), Util.Fonts.LATO_BOLD));
                break;
            case "light":
                button.setTypeface(Util.getTypeFace(button.getContext(), Util.Fonts.LATO_LIGHT));
                break;
            default:
                button.setTypeface(Util.getTypeFace(button.getContext(), Util.Fonts.LATO_REGULAR));
                break;
        }

    }

    @BindingAdapter("textSize")
    public static void setTextSize(TextView textView, String dimenType) {

        float dimenPix = 0;
//        Util.showLog("dimenType " + dimenType);
        switch (dimenType) {

            case "font_h1_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._96sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h2_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._60sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h3_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._48sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h4_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._34sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h5_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._24sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h6_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._20sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h7_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._18sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_title_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._16sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._14sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_s_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._12sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_xs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._10sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
            case "font_body_xxs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._8sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
            case "font_body_xxxs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._7sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
        }
    }

    @BindingAdapter("textSize")
    public static void setTextSize(CheckBox textView, String dimenType) {

        float dimenPix = 0;
//        Util.showLog("dimenType " + dimenType);
        switch (dimenType) {

            case "font_h1_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._96sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h2_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._60sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h3_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._48sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h4_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._34sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h5_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._24sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h6_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._20sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h7_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._18sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_title_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._16sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._14sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_s_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._12sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_xs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._10sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
            case "font_body_xxs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._8sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
        }
    }

    @BindingAdapter("textSize")
    public static void setTextSize(EditText editText, String dimenType) {

        float dimenPix = 0;
        switch (dimenType) {
            case "edit_text":

                dimenPix = editText.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._14sdp);
                editText.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);

                break;
        }
    }

    @BindingAdapter("textSize")
    public static void setTextSize(Button button, String dimenType) {

        float dimenPix = 0;
        switch (dimenType) {
            case "button_text_12":

                dimenPix = button.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._12sdp);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);

                break;

            case "button_text_14":

                dimenPix = button.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._14sdp);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);

                break;

            case "button_text_16":

                dimenPix = button.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._16sdp);
                button.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);

                break;
        }
    }

    @BindingAdapter("font")
    public static void setFont(MaterialTextView textView, String type) {
        switch (type) {
            case "normal":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR));
                break;
            case "bold":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR), Typeface.BOLD);
                break;
            case "extra_bold":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_MEDIUM), Typeface.BOLD);
                break;
            case "super_extra_bold":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_BOLD), Typeface.BOLD);
                break;
            case "bold_italic":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_MEDIUM), Typeface.BOLD_ITALIC);
                break;
            case "italic":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR), Typeface.ITALIC);
                break;
            case "medium":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_MEDIUM));
                break;
            case "light":
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_LIGHT));
                break;
            default:
                textView.setTypeface(Util.getTypeFace(textView.getContext(), Util.Fonts.LATO_REGULAR));
                break;
        }

    }

    @BindingAdapter("textSize")
    public static void setTextSize(MaterialTextView textView, String dimenType) {

        float dimenPix = 0;
//        Util.showLog("dimenType " + dimenType);
        switch (dimenType) {

            case "font_h1_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._96sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h2_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._60sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h3_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._48sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h4_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._34sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h5_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._24sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h6_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._20sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_h7_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._18sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_title_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._16sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._14sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_s_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._12sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;

            case "font_body_xs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._10sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
            case "font_body_xxs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._8sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
            case "font_body_xxxs_size":
                dimenPix = textView.getResources().getDimensionPixelOffset(com.intuit.sdp.R.dimen._7sdp);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, dimenPix);
                break;
        }
    }

}
