<?php

Route::group(['middleware' => ['IsInstalled']], function () {  
    Auth::routes(['register' => false]);
});

Route::post('login', 'Auth\LoginController@authenticate')->middleware(['IsInstalled'])->name('login');

Route::namespace('Admin')->group(function () {
    Route::post("logout", 'HomeController@logout')->middleware(['IsInstalled'])->name('logout');
    Route::get('/', 'HomeController@index')->middleware(['IsInstalled'])->name('admin');

    Route::get("/clear_cache", function () {
        Cache::flush();
        Artisan::call('optimize:clear');
        Artisan::call('clear-compiled');
        
        return back();
    })->middleware(['IsInstalled']);
    Route::get('get-details','HomeController@get_details');

    Route::group(['middleware' => ['auth','admin','IsInstalled']], function () {
        Route::get('update-profile','HomeController@userProfile');
        Route::Post('update-profile','HomeController@userProfileUpdate');
        Route::Post('get-monthly-payment-report','HomeController@getMonthlyPaymentReport');
        
        Route::resource('user', 'UserController');
        Route::Post('user-status','UserController@user_status');
        Route::Post('ai-image-delete','UserController@ai_image_delete');
        Route::Post('document-delete','UserController@document_delete');

        Route::get('uses','HomeController@uses_report');
        Route::get('today-generated-document','HomeController@today_generated_document');
        Route::get('today-generated-image','HomeController@today_generated_image');

        Route::Post('subscription-update','UserController@subscription_update');
        Route::get('user-get-package', 'UserController@get_package');
        Route::get('user-get-plan', 'UserController@get_plan');

        // Route::resource('subscription-plan', 'SubscriptionController');
        // Route::Post('subscription-plan-status','SubscriptionController@subscription_status');
        // Route::Post('subscription-most-popular','SubscriptionController@subscription_most_popular');

        Route::resource('plan', 'PlanController');
        Route::Post('plan-status','PlanController@plan_status');
        Route::Post('plan-most-popular','PlanController@plan_most_popular');

        Route::resource('coupon-code', 'CouponCodeController');
        Route::Post('coupon-code-status','CouponCodeController@coupon_code_status');

        Route::get('template', 'HomeController@template');
        Route::get('template/{id}/edit', 'HomeController@template_edit');
        Route::Post('template-update', 'HomeController@template_update');
        Route::Post('template-status','HomeController@template_status');

        Route::get('transaction','HomeController@transaction');
        Route::get('invoice-download/{id}','HomeController@invoice_download');
        Route::post('transaction-delete','HomeController@transaction_delete');

        Route::get('payment-completed/{id}','HomeController@payment_completed');
        Route::get('user-delete-request','HomeController@user_delete_request');
        Route::post('delete-user','HomeController@delete_user');
        Route::post('multi-delete-user','HomeController@multi_delete_user');

        Route::resource('blog-category', 'BlogCategoryController');
        Route::Post('blog-category-status','BlogCategoryController@blog_category_status');

        Route::resource('blog', 'BlogController');
        Route::Post('blog-status','BlogController@blog_status');

        Route::get('support-requests', 'SupportRequestsController@index');
        Route::post('multi-delete-support-request','SupportRequestsController@multi_delete_support_request');
        Route::post('support-requests/response', 'SupportRequestsController@response')->name('admin.support.response');
        Route::DELETE('support-requests/{ticket_id}', 'SupportRequestsController@destroy');
        Route::get('support-requests/{ticket_id}', 'SupportRequestsController@show');

        Route::resource('inquiry', 'InquiryController');

        Route::get('setting','SettingController@setting');
        Route::post('ads-setting','SettingController@ads_setting');
        Route::post('app-setting','SettingController@app_setting');
        Route::post('openai-setting','SettingController@openai_setting');
        Route::post('check-model-available','SettingController@check_model_available');
        Route::post('add-openai-model','SettingController@add_openai_model');
        Route::post('email-setting','SettingController@email_setting');
        Route::post('payment-setting','SettingController@payment_setting');
        Route::post('app-update-setting','SettingController@app_update_setting');
        Route::post('other-setting','SettingController@other_setting');
        Route::post('invoice-setting','SettingController@invoice_setting');
        Route::post('social-login-setting','SettingController@social_login_setting');

        Route::get('useful-links','HomeController@useful_links');
        Route::post('useful-links','HomeController@post_useful_links');

        Route::get('main-section','HomeController@main_section');
        Route::post('main-section','HomeController@post_main_section');

        Route::post('upload-zip','HomeController@upload_zip');
    });
});
