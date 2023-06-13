<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

Route::namespace('Api')->middleware(['throttle'])->group(function () {
    Route::post('google-registration', 'HomeApi@google_registration');
	Route::get('user', 'HomeApi@user_data');
    Route::get('template', 'HomeApi@template');
    Route::post('favorite-template', 'HomeApi@favorite_template');

    Route::get('document', 'HomeApi@document');
    Route::get('document-detail', 'HomeApi@document_detail');
    Route::post('update-document', 'HomeApi@update_document');
    Route::post('delete-document', 'HomeApi@delete_document');

    Route::get('blog', 'HomeApi@blog');
    Route::get('subscription-plan', 'HomeApi@subscription_plan');

    Route::get('magik-art', 'HomeApi@magik_art');
    Route::post('create-magik-art', 'HomeApi@create_magik_art');
    Route::post('delete-magik-art', 'HomeApi@delete_magik_art');
    Route::Post('account-delete-request','HomeApi@account_delete_request');

    Route::post('create-payment', 'HomeApi@create_payment');
    Route::post('stripe-payment', 'HomeApi@stripePayment');
    Route::get('purchase-history', 'HomeApi@purchase_history');
    Route::post('profile-update', 'HomeApi@profile_update');
    Route::post('contact-massage', 'HomeApi@postContacts');

    Route::post('create-support-request', 'HomeApi@create_support_request');
    Route::post('support-request-reply', 'HomeApi@support_request_reply');
    Route::get('support-request', 'HomeApi@support_request');
    Route::get('support-request-detail', 'HomeApi@support_request_detail');
    Route::post('support-request-delete', 'HomeApi@support_request_delete');

    Route::get('app-about', 'HomeApi@getAppAbout');
    Route::get('get-documents','HomeApi@get_documents');
    // Route::post('app-about', 'HomeApi@getAppAbout');

    Route::post('generate-templates', 'HomeApi@generate_templates');

    Route::post('user-chat','HomeApi@user_chat');
    Route::post('chat-history','HomeApi@chat_history');
    
    Route::post('rewarded-add','HomeApi@rewarded_add');
});