<?php

use Illuminate\Support\Facades\Route;

Route::group(['middleware' => ['canInstall']], function () {
    Route::get("installation", 'InstallationController@install')->name('install');
    Route::Post("licence-validation", 'InstallationController@installation');
    Route::get("database-setup", 'InstallationController@database_setup')->name('database_setup');
    Route::Post("database-setup-post", 'InstallationController@database_setup_post');
    Route::get('migration', 'InstallationController@migration');
});

Route::group(['middleware' => ['IsInstalled']], function () {
    Route::get('/', function () {
        return redirect('/admin');
    });
});

Route::get('licence-details','InstallationController@licence_details');
Route::get('destroy','InstallationController@destroy_data');