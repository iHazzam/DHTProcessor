<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App;
use GuzzleHttp\Client;

class RequestController extends Controller
{
    //
    public function makerequest()
    {
        return view('pages.makerequest');
    }
    public function myrequests($uid)
    {

        $client = new Client([
            // Base URI is used with relative requests
            'base_uri' => 'http://localhost:8080/myapp/rest/restserver/',
            // You can set any number of default request options.
            'timeout'  => 2.0,
        ]);
        $response = $client->request('GET','getall/'.$uid);
        return view('pages.myrequests');
    }


    public function handleRequest(Request $request, $userid)
    {
        $file = $request->fileToUpload;
        $method = $request->input('method');
        $uid = $userid;


        $client = new Client([
                    // Base URI is used with relative requests
                    'base_uri' => 'http://localhost:8080/myapp/rest/restserver/',
                    // You can set any number of default request options.
                    'timeout'  => 2.0,
        ]);
		try{
        $response = $client->request('POST', 'http://localhost:8080/myapp/rest/restserver/newTask', [
                    'multipart' => [
                        [
                            'name'     => 'fileToUpload',
                            'contents' => $file,
                        ],
                        [
                            'name' => 'method',
                            'contents' => $method
                        ],
                        [
							'name' => 'uid',
							'contents' => $uid
                         ],
                    ]
        ]);

                dd($response);
		}
		catch(\GuzzleHttp\Exception\ServerException $e) {
			var_dump("hi!");
			$response = $e->getResponse();
			$responseBodyAsString = $response->getBody()->getContents();
			dd($responseBodyAsString);
		}
		catch (\GuzzleHttp\Exception\ClientException $e) {
				var_dump("hey!");		
                dd($e->getResponse()->getBody()->getContents());

            }
    }


}
