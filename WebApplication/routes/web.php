<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| This file is where you may define all of the routes that are handled
| by your application. Just tell Laravel the URIs it should respond
| to using a Closure or controller method. Build something great!
|
*/

Route::get('/', function () {
    return view('welcome');
});

Auth::routes();

Route::get('/home', 'HomeController@index');
Route::get('/makerequest', 'RequestController@makerequest')->middleware('auth');;
Route::get('/myrequests', 'RequestController@myrequests')->middleware('auth');;
Route::get('/report', 'PagesController@getreport')->middleware('auth');;
Route::get('/sourcecode', 'PagesController@getsourcecode')->middleware('auth');;
Route::post('/request/post/{uid}', 'RequestController@handleRequest');
