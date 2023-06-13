package com.drewapps.ai.utils;

import static com.drewapps.ai.utils.Constants.CURRENT_REWARD;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

import com.drewapps.ai.MyApplication;
import com.drewapps.ai.ui.templates.article.ArticleWriterActivity;
import com.drewapps.ai.ui.templates.article.BlogPostTopicIdeasActivity;
import com.drewapps.ai.ui.templates.article.ParagraphGeneratorActivity;
import com.drewapps.ai.ui.templates.article.SEOMetaTagsBlogActivity;
import com.drewapps.ai.ui.templates.aso.ASOActivity;
import com.drewapps.ai.ui.templates.copywrite.CopyWritingActivity;
import com.drewapps.ai.ui.templates.ecommerce.AIDAFrameworkActivity;
import com.drewapps.ai.ui.templates.ecommerce.AmazonProductDescriptionParagraphActivity;
import com.drewapps.ai.ui.templates.ecommerce.ConentImproverActivity;
import com.drewapps.ai.ui.templates.ecommerce.ProductDescriptionActivity;
import com.drewapps.ai.ui.templates.email.CancellationEmailActivity;
import com.drewapps.ai.ui.templates.email.WriteColdEmailsActivity;
import com.drewapps.ai.ui.templates.generalWriting.ChecklistAdCopyActivity;
import com.drewapps.ai.ui.templates.generalWriting.CompanyBioActivity;
import com.drewapps.ai.ui.templates.generalWriting.CoverLetterActivity;
import com.drewapps.ai.ui.templates.generalWriting.FreelanceProjectProposalActivity;
import com.drewapps.ai.ui.templates.generalWriting.ProsandConsActivity;
import com.drewapps.ai.ui.templates.generalWriting.QuoraAnswersActivity;
import com.drewapps.ai.ui.templates.generalWriting.SynonymGeneratorActivity;
import com.drewapps.ai.ui.templates.generalWriting.TextExtenderActivity;
import com.drewapps.ai.ui.templates.marketing.AppSMSNotificationActivity;
import com.drewapps.ai.ui.templates.marketing.FacebookAdsActivity;
import com.drewapps.ai.ui.templates.marketing.GoogleAdsDescriptionActivity;
import com.drewapps.ai.ui.templates.marketing.LinkedinAdsDescriptionActivity;
import com.drewapps.ai.ui.templates.marketing.LinkedinAdsHeadlineActivity;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.drewapps.ai.R;
import com.drewapps.ai.items.Template;
import com.drewapps.ai.ui.templates.social.InstagramCaptionActivity;
import com.drewapps.ai.ui.templates.social.InstagramHashtagsActivity;
import com.drewapps.ai.ui.templates.social.InstagramReelScriptsActivity;
import com.drewapps.ai.ui.templates.social.LinkedinPostActivity;
import com.drewapps.ai.ui.templates.social.PinTitleDescriptionActivity;
import com.drewapps.ai.ui.templates.social.SocialMediaContentPlanActivity;
import com.drewapps.ai.ui.templates.social.TitTokVideoScriptsActivity;
import com.drewapps.ai.ui.templates.social.TwitterTweetsActivity;
import com.drewapps.ai.ui.templates.social.YoutubeShortScriptsActivity;
import com.drewapps.ai.ui.templates.social.YoutubeVideoDescriptionActivity;
import com.drewapps.ai.ui.templates.social.YoutubeVideoIdeasActivity;
import com.drewapps.ai.ui.templates.social.YoutubeVideoOutlinesActivity;
import com.drewapps.ai.ui.templates.social.YoutubeVideoScriptsActivity;
import com.drewapps.ai.ui.templates.social.YoutubeVideoTagActivity;
import com.drewapps.ai.ui.templates.social.YoutubeVideoTitleActivity;

import java.io.Serializable;
import java.util.Calendar;

public class Tools {

    public static void ShowPopUpMenu(Context context, View parentView, View mainView, View iconView, int menu) {
        PopupMenu popup = new PopupMenu(context, parentView, Gravity.NO_GRAVITY, 0,
                R.style.popupMenu);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                ((MaterialAutoCompleteTextView) mainView).setText(item.getTitle());
                if (iconView != null) {
                    ((TextInputLayout) iconView).setStartIconDrawable(item.getIcon());
                }

                return true;
            }
        });
        popup.inflate(menu);
        if (menu == R.menu.outputs || menu == R.menu.outputs) {
            popup.setForceShowIcon(false);
        } else {
            popup.setForceShowIcon(true);
        }
        popup.show();

    }

    public static void TodayRewardAvl(Context context) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String todayStr = year + "" + month + "" + day + "";
        PrefManager prefManager = MyApplication.prefManager();
        boolean currentDay = prefManager.getBoolean(todayStr);

        Util.showLog("TODAY: " + todayStr);
        //Daily reward
        if (!currentDay) {
            prefManager.setInt(CURRENT_REWARD, 0);
            prefManager.setBoolean(todayStr, true);
        }else {

        }

    }

    public static boolean isTimeAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static boolean isZoneAutomatic(Context c) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(c.getContentResolver(), Settings.Global.AUTO_TIME_ZONE, 0) == 1;
        } else {
            return android.provider.Settings.System.getInt(c.getContentResolver(), android.provider.Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public static void gotoTemplate(Context context, Template template) {
        Intent intent = null;

        switch (template.templateName) {
            case "Linkedin Post":
                intent = new Intent(context, LinkedinPostActivity.class);
                break;
            case "Instagram Caption":
                intent = new Intent(context, InstagramCaptionActivity.class);
                break;
            case "Twitter Tweets":
            case "Pinterest Board Ideas":
            case "Pinterest Pin ideas":
            case "Board Descriptions":
            case "Pin Group Ideas":
            case "Snapchat Content Ideas":
            case "Snapchat Spotlight Ideas":
            case "Snapchat Story Series":
                intent = new Intent(context, TwitterTweetsActivity.class);
                break;
            case "Trending Instagram Hashtags":
                intent = new Intent(context, InstagramHashtagsActivity.class);
                break;
            case "Youtube Video Description":
                intent = new Intent(context, YoutubeVideoDescriptionActivity.class);
                break;
            case "Youtube Video Ideas":
                intent = new Intent(context, YoutubeVideoIdeasActivity.class);
                break;
            case "Youtube Video Outlines":
                intent = new Intent(context, YoutubeVideoOutlinesActivity.class);
                break;
            case "Youtube Video Title":
                intent = new Intent(context, YoutubeVideoTitleActivity.class);
                break;
            case "Youtube Video Tag":
                intent = new Intent(context, YoutubeVideoTagActivity.class);
                break;
            case "TikTok Video Scripts":
                intent = new Intent(context, TitTokVideoScriptsActivity.class);
                break;
            case "Youtube Video Scripts":
                intent = new Intent(context, YoutubeVideoScriptsActivity.class);
                break;
            case "Instagram Reel Scripts":
                intent = new Intent(context, InstagramReelScriptsActivity.class);
                break;
            case "Youtube Short Scripts":
                intent = new Intent(context, YoutubeShortScriptsActivity.class);
                break;
            case "Pin Title & Description":
                intent = new Intent(context, PinTitleDescriptionActivity.class);
                break;
            case "Social Media Content Plan":
                intent = new Intent(context, SocialMediaContentPlanActivity.class);
                break;

            case "AIDA Copywriting Frame":
            case "PAS Copywriting Frame":
            case "Marketing USP":
            case "BAB Copywriting Framework":
            case "FAB Copywriting Framework":
            case "The 4 C Copywriting Framework":
            case "FOREST Copywriting Framework":
            case "5 Basic Objections Copywriting Framework":
            case "The PPPP Copywriting Framework":
            case "The SCH Copywriting Framework":
            case "The SSS Copywriting Framework":
            case "PASTOR Copywriting Framework":
            case "ACCA Copywriting Framework":
            case "1-2-3-4 Copywriting Framework":
            case "6+1 Copywriting Framework":
            case "SLAP Copywriting Framework":
            case "4U Copywriting Framework":
                intent = new Intent(context, CopyWritingActivity.class);
                break;
            case "Facebook Ads":
                intent = new Intent(context, FacebookAdsActivity.class);
                break;
            case "Linkedin Ads Headline":
            case "Google Ads Titles":
            case "Amazon Product Title":
            case "Flipkart Product Title":
            case "Product Names":
                intent = new Intent(context, LinkedinAdsHeadlineActivity.class);
                break;
            case "Linkedin Ads Description":
                intent = new Intent(context, LinkedinAdsDescriptionActivity.class);
                break;
            case "Google Ads Description":
            case "Call To Action":
            case "Pinterest Ad Campaign":
                intent = new Intent(context, GoogleAdsDescriptionActivity.class);
                break;
            case "App & SMS Notification":
                intent = new Intent(context, AppSMSNotificationActivity.class);
                break;
            case "Product Description":
                intent = new Intent(context, ProductDescriptionActivity.class);
                break;
            case "AIDA Framework":
                intent = new Intent(context, AIDAFrameworkActivity.class);
                break;
            case "Amazon Product Description (Paragraph)":
            case "Amazon Product Feature (Bullet)":
            case "SEO Title and Meta Description":
            case "Amazon Sponsered Brand Ad Headline":
            case "Flipkart Product Description":
                intent = new Intent(context, AmazonProductDescriptionParagraphActivity.class);
                break;
            case "Content Improver":
            case "Content Rewriter":
            case "Content Rephrase":
            case "Text Summarizer":
            case "Fix the Grammar":
            case "Sentence Simplifier":
                intent = new Intent(context, ConentImproverActivity.class);
                break;
            case "Article Writer":
                intent = new Intent(context, ArticleWriterActivity.class);
                break;
            case "Blog Post Topic Ideas":
            case "Blog Post Outlines":
            case "FAQ Generator":
            case "Blog Post Conclusion Paragraph":
            case "Blog Post Intro Paragraph":
                intent = new Intent(context, BlogPostTopicIdeasActivity.class);
                break;
            case "Paragraph Generator":
                intent = new Intent(context, ParagraphGeneratorActivity.class);
                break;
            case "SEO Meta Tags (Blog Post)":
            case "Landing Page Headlines":
            case "SEO Meta Tags (Product Page)":
                intent = new Intent(context, SEOMetaTagsBlogActivity.class);
                break;
            case "Text Expander":
                intent = new Intent(context, TextExtenderActivity.class);
                break;
            case "Company Bio":
            case "Company Mission":
            case "Company Vision":
                intent = new Intent(context, CompanyBioActivity.class);
                break;
            case "Quora Answers":
            case "Bullet Point Answers":
            case "Answers":
            case "Generate Question":
            case "Listicle Ideas":
            case "Startup Ideas":
            case "Definition":
                intent = new Intent(context, QuoraAnswersActivity.class);
                break;
            case "Pros and Cons":
                intent = new Intent(context, ProsandConsActivity.class);
                break;
            case "Synonyms Generator":
            case "Antonyms Generator":
                intent = new Intent(context, SynonymGeneratorActivity.class);
                break;
            case "Freelance Project Proposal":
                intent = new Intent(context, FreelanceProjectProposalActivity.class);
                break;
            case "Cover Letter":
                intent = new Intent(context, CoverLetterActivity.class);
                break;
            case "Checklist Ad Copy":
                intent = new Intent(context, ChecklistAdCopyActivity.class);
                break;
            case "PlayStore App Title":
            case "PlayStore App Short Description":
            case "PlayStore App Description":
            case "AppStore Title":
            case "AppStore Sub Title":
            case "AppStore Promotional Text":
            case "AppStore Description":
            case "App Reviews":
                intent = new Intent(context, ASOActivity.class);
                break;
            case "Write Cold Emails":
                intent = new Intent(context, WriteColdEmailsActivity.class);
                break;
            case "Cancellation Email":
            case "Welcome Emails":
            case "Confirmation Emails":
            case "Email Subject Lines":
                intent = new Intent(context, CancellationEmailActivity.class);
                break;

            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(Constants.TEMPLATE, (Serializable) template);
            context.startActivity(intent);
        }
    }

}
