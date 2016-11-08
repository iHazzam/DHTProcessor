<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class RequestController extends Controller
{
    //
    public function makerequest()
    {
        return view('pages.makerequest');
    }
    public function myrequests()
    {
        return view('pages.myrequests');
    }
}
