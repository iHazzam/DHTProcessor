<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class PagesController extends Controller
{
    //
    public function getreport()
    {
        return view('pages.report');
    }
    public function getsourcecode()
    {
        return view('pages.sourcecode');
    }
}
