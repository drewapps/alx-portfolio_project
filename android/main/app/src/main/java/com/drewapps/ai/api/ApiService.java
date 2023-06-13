package com.drewapps.ai.api;

import androidx.lifecycle.LiveData;

import com.drewapps.ai.items.AllTemplateData;
import com.drewapps.ai.items.AppInfo;
import com.drewapps.ai.items.ChatItem;
import com.drewapps.ai.items.Document;
import com.drewapps.ai.items.ItemChatHistory;
import com.drewapps.ai.items.ItemGenMagicArt;
import com.drewapps.ai.items.ItemMagicArt;
import com.drewapps.ai.items.ItemSubsPlan;
import com.drewapps.ai.items.ItemSupportReq;
import com.drewapps.ai.items.LoginUser;
import com.drewapps.ai.items.PurchaseHistory;
import com.drewapps.ai.items.StripeResponse;
import com.drewapps.ai.items.SupportDetails;
import com.drewapps.ai.items.UserData;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("{API_KEY}/app-about")
    LiveData<ApiResponse<AppInfo>> getAppInfo(@Path("API_KEY") String apiKey,
                                              @Query("user_id") String userId);

    @FormUrlEncoded
    @POST("{API_KEY}/google-registration")
    LiveData<ApiResponse<LoginUser>> googleLogin(@Path("API_KEY") String apiKey,
                                                 @Field("name") String name,
                                                 @Field("email") String email,
                                                 @Field("image") String imageUrl);

    @GET("{API_KEY}/user")
    LiveData<ApiResponse<UserData>> getUserData(@Path("API_KEY") String apiKey,
                                                @Query("id") Integer userId);

    @FormUrlEncoded
    @POST("{API_KEY}/account-delete-request")
    Call<ApiStatus> deleteAccount(@Path("API_KEY") String api_key,
                                  @Field("user_id") Integer userId);

    @Multipart
    @POST("{API_KEY}/profile-update")
    Call<ApiStatus> updateAccount(@Path("API_KEY") String apiKey,
                                  @Part("user_id") RequestBody userId,
                                  @Part("image") RequestBody name,
                                  @Part MultipartBody.Part file,
                                  @Part("name") RequestBody userName,
                                  @Part("email") RequestBody userEmail);

    @GET("{API_KEY}/purchase-history")
    LiveData<ApiResponse<List<PurchaseHistory>>> getPurchases(@Path("API_KEY") String apiKey,
                                                              @Query("user_id") Integer userId);

    @GET("{API_KEY}/template")
    LiveData<ApiResponse<AllTemplateData>> getAllTemplateData(@Path("API_KEY") String api_key,
                                                              @Query("user_id") Integer userID);

    @FormUrlEncoded
    @POST("{API_KEY}/favorite-template")
    Call<ApiStatus> favoriteTemplate(@Path("API_KEY") String apiKey,
                                     @Field("user_id") Integer userID,
                                     @Field("template_id") Integer templateId);

    @FormUrlEncoded
    @POST("{API_KEY}/update-document")
    LiveData<ApiResponse<ApiStatus>> updateDocument(@Path("API_KEY") String apiKey,
                                                    @Field("id") Integer documentId,
                                                    @Field("title") String documentTitle,
                                                    @Field("text") String documentContent);

    @GET("{API_KEY}/document")
    LiveData<ApiResponse<List<Document>>> getAllDocuments(@Path("API_KEY") String apiKey,
                                                          @Query("user_id") Integer user_id);

    @FormUrlEncoded
    @POST("{API_KEY}/delete-document")
    Call<ApiStatus> deleteDocument(@Path("API_KEY") String api_key,
                                   @Field("id") Integer document_id);

    @GET("{API_KEY}/magik-art")
    LiveData<ApiResponse<List<ItemMagicArt>>> getMagicArt(@Path("API_KEY") String apiKey,
                                                          @Query("user_id") Integer userId);

    @FormUrlEncoded
    @POST("{API_KEY}/create-magik-art")
    LiveData<ApiResponse<List<ItemGenMagicArt>>> createMagicArt(@Path("API_KEY") String apiKey,
                                                                @Field("user_id") Integer userId,
                                                                @Field("description") String description,
                                                                @Field("style") String style,
                                                                @Field("medium") String medium,
                                                                @Field("number_of_images") String noOfImage,
                                                                @Field("resolution") String resolution);

    @FormUrlEncoded
    @POST("{API_KEY}/delete-magik-art")
    Call<ApiStatus> deleteArt(@Path("API_KEY") String apiKey,
                              @Field("id") Integer artId);

    @GET("{API_KEY}/support-request")
    LiveData<ApiResponse<List<ItemSupportReq>>> getSupportReqList(@Path("API_KEY") String apiKey,
                                                                  @Query("user_id") Integer userId);


    @Multipart
    @POST("{API_KEY}/create-support-request")
    LiveData<ApiResponse<ItemSupportReq>> createSupport(@Path("API_KEY") String apiKey,
                                                        @Part("image") RequestBody finalFullName,
                                                        @Part MultipartBody.Part finalBody,
                                                        @Part("user_id") RequestBody useIdRB,
                                                        @Part("subject") RequestBody subjectRB,
                                                        @Part("category") RequestBody categoryRB,
                                                        @Part("priority") RequestBody priorityRB,
                                                        @Part("message") RequestBody massageRB);

    @GET("{API_KEY}/support-request-detail")
    LiveData<ApiResponse<SupportDetails>> getSupportDetails(@Path("API_KEY") String apiKey,
                                                            @Query("ticket_id") String ticketId);

    @Multipart
    @POST("{API_KEY}/support-request-reply")
    LiveData<ApiResponse<SupportDetails>> sendMessage(@Path("API_KEY") String apiKey,
                                                      @Part("attachment") RequestBody finalFullName,
                                                      @Part MultipartBody.Part finalBody,
                                                      @Part("user_id") RequestBody useIdRB,
                                                      @Part("message") RequestBody messageRB,
                                                      @Part("ticket_id") RequestBody ticket_idRB);

    @GET("{API_KEY}/subscription-plan")
    LiveData<ApiResponse<List<ItemSubsPlan>>> getSubsPlans(@Path("API_KEY") String apiKey);


    @FormUrlEncoded
    @POST("{API_KEY}/create-payment")
    Call<ApiStatus> purchaseData(@Path("API_KEY") String apiKey,
                                 @Field("user_id") Integer userId,
                                 @Field("plan_id") Integer id,
                                 @Field("payment_id") String paymentId,
                                 @Field("payment_type")String type);

    @FormUrlEncoded
    @POST("{API_KEY}/contact-massage")
    Call<ApiStatus> contactUs(@Path("API_KEY") String apiKey,
                              @Field("name") String name,
                              @Field("email") String email,
                              @Field("message") String message,
                              @Field("subject") String category);

    @FormUrlEncoded
    @POST("{API_KEY}/user-chat")
    LiveData<ApiResponse<List<ChatItem>>> getChatItems(@Path("API_KEY")String apiKey,
                                                       @Field("user_id")Integer userID,
                                                       @Field("text")String text,
                                                       @Field("chat_id") String chatId);

    @FormUrlEncoded
    @POST("{API_KEY}/chat-history")
    LiveData<ApiResponse<List<ItemChatHistory>>> getChatHistory(@Path("API_KEY")String apiKey,
                                                              @Field("user_id")Integer userID);

    @FormUrlEncoded
    @POST("{API_KEY}/stripe-payment")
    Call<StripeResponse> createStripePayment(@Path("API_KEY") String apiKey,
                                             @Field("order_amount") String order_amount);

    @FormUrlEncoded
    @POST("{API_KEY}/rewarded-add")
    Call<ApiStatus> uploadReward(@Path("API_KEY") String apiKey,
                              @Field("user_id") Integer userId);


    /**
     * SOCIAL CATEGORY START
     */
    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> linkedin_post(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("instruction") String instruction,
                                 @Field("outputs") String outputs,
                                 @Field("voice") String voice,
                                 @Field("words") String words,
                                 @Field("emoji") boolean emoji,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> instagram_caption(@Path("API_KEY") String apiKey,
                                     @Field("document_name") String document_name,
                                     @Field("template_id") Integer template_id,
                                     @Field("user_id") Integer user_id,
                                     @Field("description") String description,
                                     @Field("outputs") String outputs,
                                     @Field("voice") String voice,
                                     @Field("emoji") boolean emoji,
                                     @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> twitter_tweets(@Path("API_KEY") String apiKey,
                                  @Field("document_name") String document_name,
                                  @Field("template_id") Integer template_id,
                                  @Field("user_id") Integer user_id,
                                  @Field("topic") String topic,
                                  @Field("outputs") String outputs,
                                  @Field("emoji") boolean emoji,
                                  @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> instagram_hashtags(@Path("API_KEY") String apiKey,
                                      @Field("document_name") String documentName,
                                      @Field("template_id") Integer templateId,
                                      @Field("user_id") Integer user_id,
                                      @Field("topic") String instruction,
                                      @Field("no_of_hashtags") String output);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> youtube_video_description(@Path("API_KEY") String apiKey,
                                             @Field("document_name") String document_name,
                                             @Field("template_id") Integer template_id,
                                             @Field("user_id") Integer user_id,
                                             @Field("video_title") String instruction,
                                             @Field("keywords") String keywords,
                                             @Field("outputs") String outputs,
                                             @Field("voice") String voice,
                                             @Field("words") String words,
                                             @Field("emoji") boolean emoji,
                                             @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> youtube_video_idea(@Path("API_KEY") String apiKey,
                                      @Field("document_name") String document_name,
                                      @Field("template_id") Integer template_id,
                                      @Field("user_id") Integer user_id,
                                      @Field("video_topic") String video_topic,
                                      @Field("search_term") String search_term,
                                      @Field("outputs") String outputs,
                                      @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> youtube_video_outline(@Path("API_KEY") String apiKey,
                                         @Field("document_name") String document_name,
                                         @Field("template_id") Integer template_id,
                                         @Field("user_id") Integer user_id,
                                         @Field("video_topic") String video_topic,
                                         @Field("search_term") String search_term,
                                         @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> youtube_video_title(@Path("API_KEY") String apiKey,
                                       @Field("document_name") String document_name,
                                       @Field("template_id") Integer template_id,
                                       @Field("user_id") Integer user_id,
                                       @Field("video_description") String video_description,
                                       @Field("search_term") String search_term,
                                       @Field("outputs") String outputs,
                                       @Field("emoji") boolean emoji,
                                       @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> tiktok_video_script(@Path("API_KEY") String apiKey,
                                       @Field("document_name") String document_name,
                                       @Field("template_id") Integer template_id,
                                       @Field("user_id") Integer user_id,
                                       @Field("description") String video_description,
                                       @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> youtube_video_script(@Path("API_KEY") String apiKey,
                                        @Field("document_name") String document_name,
                                        @Field("template_id") Integer template_id,
                                        @Field("user_id") Integer user_id,
                                        @Field("title") String video_description,
                                        @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> instagram_reel_script(@Path("API_KEY") String apiKey,
                                         @Field("document_name") String document_name,
                                         @Field("template_id") Integer template_id,
                                         @Field("user_id") Integer user_id,
                                         @Field("title") String video_description,
                                         @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> youtube_short_script(@Path("API_KEY") String apiKey,
                                        @Field("document_name") String document_name,
                                        @Field("template_id") Integer template_id,
                                        @Field("user_id") Integer user_id,
                                        @Field("title") String video_description,
                                        @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> pin_title_description(@Path("API_KEY") String apiKey,
                                         @Field("document_name") String document_name,
                                         @Field("template_id") Integer template_id,
                                         @Field("user_id") Integer user_id,
                                         @Field("topic") String topic,
                                         @Field("about") String about,
                                         @Field("keywords") String keywords,
                                         @Field("outputs") String outputs,
                                         @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> social_media_content_plan(@Path("API_KEY") String apiKey,
                                             @Field("document_name") String document_name,
                                             @Field("template_id") Integer template_id,
                                             @Field("user_id") Integer user_id,
                                             @Field("objective") String description,
                                             @Field("outputs") String outputs,
                                             @Field("platform") String voice,
                                             @Field("emoji") boolean emoji);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> pin_board_idea(@Path("API_KEY") String apiKey,
                                  @Field("document_name") String document_name,
                                  @Field("template_id") Integer template_id,
                                  @Field("user_id") Integer user_id,
                                  @Field("topic_idea") String topic,
                                  @Field("outputs") String outputs,
                                  @Field("language") String language);
    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> board_description(@Path("API_KEY") String apiKey,
                                  @Field("document_name") String document_name,
                                  @Field("template_id") Integer template_id,
                                  @Field("user_id") Integer user_id,
                                  @Field("title") String topic,
                                  @Field("outputs") String outputs,
                                  @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> pin_group_idea(@Path("API_KEY") String apiKey,
                                     @Field("document_name") String document_name,
                                     @Field("template_id") Integer template_id,
                                     @Field("user_id") Integer user_id,
                                     @Field("theme") String topic,
                                     @Field("outputs") String outputs,
                                     @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> snapchat_series(@Path("API_KEY") String apiKey,
                                     @Field("document_name") String document_name,
                                     @Field("template_id") Integer template_id,
                                     @Field("user_id") Integer user_id,
                                     @Field("theme") String topic,
                                     @Field("language") String language);
    /**
     * SOCIAL CATEGORY END
     */

    /**
     * COPY WRITE CATEGORY START
     */

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> copy_writing(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("product_name") String product_name,
                                @Field("about_product") String about_product,
                                @Field("language") String language);

    /**
     * COPY WRITE CATEGORY END
     */

    /**
     * MARKETING CATEGORY END
     */
    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> facebook_ads(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("description") String description,
                                @Field("outputs") String outputs,
                                @Field("voice") String voice,
                                @Field("product_name") String product_name,
                                @Field("promotion") String promotion,
                                @Field("emoji") boolean emoji,
                                @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> linkedin_google_ads_headline(@Path("API_KEY") String apiKey,
                                                @Field("document_name") String document_name,
                                                @Field("template_id") Integer template_id,
                                                @Field("user_id") Integer user_id,
                                                @Field("description") String description,
                                                @Field("outputs") String outputs,
                                                @Field("product_name") String product_name,
                                                @Field("promotion") String promotion,
                                                @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> linkedin_ads_description(@Path("API_KEY") String apiKey,
                                            @Field("document_name") String document_name,
                                            @Field("template_id") Integer template_id,
                                            @Field("user_id") Integer user_id,
                                            @Field("product_name") String product_name,
                                            @Field("description") String description,
                                            @Field("promotion") String promotion,
                                            @Field("keywords") String keywords,
                                            @Field("outputs") String outputs,
                                            @Field("language") String language,
                                            @Field("no_of_characters") String no_of_characters);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> google_ads_description(@Path("API_KEY") String apiKey,
                                          @Field("document_name") String document_name,
                                          @Field("template_id") Integer template_id,
                                          @Field("user_id") Integer user_id,
                                          @Field("description") String description,
                                          @Field("outputs") String outputs,
                                          @Field("product_name") String product_name,
                                          @Field("keywords") String promotion,
                                          @Field("emoji") boolean emoji,
                                          @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> app_sms_notification(@Path("API_KEY") String apiKey,
                                        @Field("document_name") String document_name,
                                        @Field("template_id") Integer template_id,
                                        @Field("user_id") Integer user_id,
                                        @Field("description") String description,
                                        @Field("outputs") String outputs,
                                        @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> pin_ads(@Path("API_KEY") String apiKey,
                               @Field("document_name") String document_name,
                               @Field("template_id") Integer template_id,
                               @Field("user_id") Integer user_id,
                               @Field("product_service") String product_name,
                               @Field("language") String language);

    /**
     * MARKETING CATEGORY END
     */

    /**
     * ECOMMERCE CATEGORY START
     */

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> product_description(@Path("API_KEY") String apiKey,
                                       @Field("document_name") String document_name,
                                       @Field("template_id") Integer template_id,
                                       @Field("user_id") Integer user_id,
                                       @Field("product_about") String product_about,
                                       @Field("product_name") String product_name,
                                       @Field("keywords") String keywords,
                                       @Field("emoji") boolean emoji,
                                       @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> aida_framework(@Path("API_KEY") String apiKey,
                                  @Field("document_name") String document_name,
                                  @Field("template_id") Integer template_id,
                                  @Field("user_id") Integer user_id,
                                  @Field("description") String description,
                                  @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> amazon_product_description(@Path("API_KEY") String apiKey,
                                              @Field("document_name") String document_name,
                                              @Field("template_id") Integer template_id,
                                              @Field("user_id") Integer user_id,
                                              @Field("product_name") String product_name,
                                              @Field("keywords") String keywords,
                                              @Field("no_of_paragraph") String no_of_paragraph,
                                              @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> amazon_product_feature_bullet(@Path("API_KEY") String apiKey,
                                                 @Field("document_name") String document_name,
                                                 @Field("template_id") Integer template_id,
                                                 @Field("user_id") Integer user_id,
                                                 @Field("product_name") String product_name,
                                                 @Field("no_of_bullet") String no_of_paragraph,
                                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> seo_title_meta_description(@Path("API_KEY") String apiKey,
                                              @Field("document_name") String document_name,
                                              @Field("template_id") Integer template_id,
                                              @Field("user_id") Integer user_id,
                                              @Field("product_name") String product_name,
                                              @Field("keywords") String keywords,
                                              @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> content_improver(@Path("API_KEY") String apiKey,
                                    @Field("document_name") String document_name,
                                    @Field("template_id") Integer template_id,
                                    @Field("user_id") Integer user_id,
                                    @Field("content") String content);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> amazon_product_title(@Path("API_KEY") String apiKey,
                                        @Field("document_name") String document_name,
                                        @Field("template_id") Integer template_id,
                                        @Field("user_id") Integer user_id,
                                        @Field("product_description") String product_description,
                                        @Field("outputs") String outputs,
                                        @Field("product_name") String product_name,
                                        @Field("keywords") String keywords,
                                        @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> amazon_headline(@Path("API_KEY") String apiKey,
                                   @Field("document_name") String document_name,
                                   @Field("template_id") Integer template_id,
                                   @Field("user_id") Integer user_id,
                                   @Field("product_name") String product_name,
                                   @Field("keywords") String keywords,
                                   @Field("outputs") String outputs,
                                   @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> product_name(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("product_about") String product_about,
                                @Field("outputs") String outputs,
                                @Field("product_name") String product_name,
                                @Field("keywords") String keywords,
                                @Field("language") String language);

    /**
     * ECOMMERCE CATEGORY END
     */

    /**
     * ARTICLE & BLOG CATEGORY START
     */

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> article_write(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("title") String title,
                                 @Field("keywords") String keywords,
                                 @Field("words") String words,
                                 @Field("emoji") boolean emoji,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> blog_post_topic(@Path("API_KEY") String apiKey,
                                   @Field("document_name") String document_name,
                                   @Field("template_id") Integer template_id,
                                   @Field("user_id") Integer user_id,
                                   @Field("title") String title,
                                   @Field("keywords") String keywords,
                                   @Field("no_of_ideas") String no_of_ideas,
                                   @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> blog_outline(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("title") String title,
                                @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> faq_generator(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("title") String title,
                                 @Field("no_of_faq") String no_of_faq,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> paragraph_generator(@Path("API_KEY") String apiKey,
                                       @Field("document_name") String document_name,
                                       @Field("template_id") Integer template_id,
                                       @Field("user_id") Integer user_id,
                                       @Field("title") String title,
                                       @Field("keywords") String keywords,
                                       @Field("keywords_frequency") String keywords_frequency,
                                       @Field("words") String words,
                                       @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> seo_meta_tag_blog(@Path("API_KEY") String apiKey,
                                     @Field("document_name") String document_name,
                                     @Field("template_id") Integer template_id,
                                     @Field("user_id") Integer user_id,
                                     @Field("blog_title") String blog_title,
                                     @Field("blog_description") String blog_description,
                                     @Field("search_term") String search_term,
                                     @Field("outputs") String outputs,
                                     @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> landing_page_headline(@Path("API_KEY") String apiKey,
                                         @Field("document_name") String document_name,
                                         @Field("template_id") Integer template_id,
                                         @Field("user_id") Integer user_id,
                                         @Field("topic") String blog_title,
                                         @Field("description") String blog_description,
                                         @Field("outputs") String outputs,
                                         @Field("language") String language);

    /**
     * ARTICLE & BLOG CATEGORY END
    */

    /**
     * GENERAL WRITING CATEGORY START
     */
    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> text_extender(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("text") String text,
                                 @Field("keywords") String keywords,
                                 @Field("words") String words,
                                 @Field("emoji") boolean emoji,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> company_bio(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("information") String information,
                                 @Field("company_name") String company_name,
                                 @Field("platform") String platform,
                                 @Field("outputs") String outputs,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> quora_answers(@Path("API_KEY") String apiKey,
                               @Field("document_name") String document_name,
                               @Field("template_id") Integer template_id,
                               @Field("user_id") Integer user_id,
                               @Field("question") String question,
                               @Field("outputs") String outputs,
                               @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> bullet_point_answer(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("question") String question,
                                 @Field("no_of_points") String no_of_points,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> answers(@Path("API_KEY") String apiKey,
                                       @Field("document_name") String document_name,
                                       @Field("template_id") Integer template_id,
                                       @Field("user_id") Integer user_id,
                                       @Field("question") String question,
                                       @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> sentence_sim(@Path("API_KEY") String apiKey,
                                    @Field("document_name") String document_name,
                                    @Field("template_id") Integer template_id,
                                    @Field("user_id") Integer user_id,
                                    @Field("sentence") String sentence);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> pros_and_cons(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("description") String instruction,
                                 @Field("emoji") boolean emoji,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> question_gen(@Path("API_KEY") String apiKey,
                           @Field("document_name") String document_name,
                           @Field("template_id") Integer template_id,
                           @Field("user_id") Integer user_id,
                           @Field("topic") String topic,
                           @Field("no_of_question") String no_of_question,
                           @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> company_mission(@Path("API_KEY") String apiKey,
                               @Field("document_name") String document_name,
                               @Field("template_id") Integer template_id,
                               @Field("user_id") Integer user_id,
                               @Field("company_about") String company_about,
                               @Field("company_name") String company_name,
                               @Field("platform") String platform,
                               @Field("outputs") String outputs,
                               @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> synonym_generator(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("word") String word,
                                 @Field("no_of_synonym") String no_of_synonym);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> antonym_generator(@Path("API_KEY") String apiKey,
                                     @Field("document_name") String document_name,
                                     @Field("template_id") Integer template_id,
                                     @Field("user_id") Integer user_id,
                                     @Field("word") String word,
                                     @Field("no_of_antonym") String no_of_antonym);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> freelancer_project(@Path("API_KEY") String apiKey,
                               @Field("document_name") String document_name,
                               @Field("template_id") Integer template_id,
                               @Field("user_id") Integer user_id,
                               @Field("client_name") String client_name,
                               @Field("client_about") String client_about,
                               @Field("project") String project,
                               @Field("goal_of_project") String goal_of_project,
                               @Field("freelancer_about") String freelancer_about,
                               @Field("freelancer_experience") String freelancer_experience,
                               @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> cover_latter(@Path("API_KEY") String apiKey,
                                      @Field("document_name") String document_name,
                                      @Field("template_id") Integer template_id,
                                      @Field("user_id") Integer user_id,
                                      @Field("role") String role,
                                      @Field("experience_of_candidate") String experience_of_candidate,
                                      @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> call_action(@Path("API_KEY") String apiKey,
                                          @Field("document_name") String document_name,
                                          @Field("template_id") Integer template_id,
                                          @Field("user_id") Integer user_id,
                                          @Field("product_name") String product_name,
                                          @Field("product_about") String product_about,
                                          @Field("outputs") String outputs,
                                          @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> checklist_ad_copy(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("product_about") String product_about,
                                @Field("product_name") String product_name,
                                @Field("promotion") String promotion,
                                @Field("occasion") String occasion,
                                @Field("emoji") boolean emoji,
                                @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> listicle_idea(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("topic") String topic,
                                @Field("outputs") String outputs,
                                @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> startup_idea(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("instruction") String instruction,
                                 @Field("no_of_ideas") String no_of_ideas,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> definition(@Path("API_KEY") String apiKey,
                                @Field("document_name") String document_name,
                                @Field("template_id") Integer template_id,
                                @Field("user_id") Integer user_id,
                                @Field("word") String word,
                                @Field("no_of_definition") String no_of_definition,
                                @Field("language") String language);

    /**
     * GENERAL WRITING CATEGORY END
     */

    /**
     * ASO CATEGORY START
     */
    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> play_store_title(@Path("API_KEY") String apiKey,
                                 @Field("document_name") String document_name,
                                 @Field("template_id") Integer template_id,
                                 @Field("user_id") Integer user_id,
                                 @Field("about") String about,
                                 @Field("keywords") String keywords,
                                 @Field("outputs") String outputs,
                                 @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> play_store_description(@Path("API_KEY") String apiKey,
                                    @Field("document_name") String document_name,
                                    @Field("template_id") Integer template_id,
                                    @Field("user_id") Integer user_id,
                                    @Field("about") String about,
                                    @Field("keywords") String keywords,
                                    @Field("emoji") boolean emoji,
                                    @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> app_store_description(@Path("API_KEY") String apiKey,
                                          @Field("document_name") String document_name,
                                          @Field("template_id") Integer template_id,
                                          @Field("user_id") Integer user_id,
                                          @Field("about") String about,
                                          @Field("keywords") String keywords,
                                          @Field("language") String language);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> app_review(@Path("API_KEY") String apiKey,
                                         @Field("document_name") String document_name,
                                         @Field("template_id") Integer template_id,
                                         @Field("user_id") Integer user_id,
                                         @Field("title") String title,
                                         @Field("keywords") String keywords,
                                         @Field("no_of_reviews") String no_of_reviews,
                                         @Field("language") String language);

    /**
     * ASO CATEGORY END
     */

    /**
     * EMAIL WRITING CATEGORY START
     */

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> write_cold_email(@Path("API_KEY") String apiKey,
                                      @Field("document_name") String document_name,
                                      @Field("template_id") Integer template_id,
                                      @Field("user_id") Integer user_id,
                                      @Field("sender_name") String sender_name,
                                      @Field("recipient_name") String recipient_name,
                                      @Field("sender_information") String sender_information,
                                      @Field("outputs") String outputs,
                                      @Field("language") String language,
                                      @Field("emoji") boolean emoji);

    @FormUrlEncoded
    @POST("{API_KEY}/generate-templates")
    Call<Document> cancel_email(@Path("API_KEY") String apiKey,
                                    @Field("document_name") String document_name,
                                    @Field("template_id") Integer template_id,
                                    @Field("user_id") Integer user_id,
                                    @Field("sender_name") String sender_name,
                                    @Field("recipient_name") String recipient_name,
                                    @Field("product_name") String product_name,
                                    @Field("product_description") String product_description,
                                    @Field("outputs") String outputs,
                                    @Field("language") String language);


}
